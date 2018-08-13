/*
 * Copyright 2002-2016 jamod & j2mod development teams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.koosha.modbus.modbus.transport;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.io.serial.SerialConnection;
import cc.koosha.modbus.msg.ModbusMessage;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.ModbusResponseFactory;
import cc.koosha.modbus.msg.request.DefaultModbusRequestFactory;
import cc.koosha.modbus.msg.response.DefaultModbusResponseFactory;
import cc.koosha.modbus.util.Checksum;
import cc.koosha.modbus.xinternal.AccessibleDataInput;
import cc.koosha.modbus.xinternal.AccessibleDataOutput;
import cc.koosha.modbus.xinternal.J2ModUtils;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static cc.koosha.modbus.xinternal.J2ModDebugUtils.toHex;


/**
 * Class that implements the Modbus/ASCII transport flavor.
 *
 * @author Dieter Wimberger
 * @author John Charlton
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public final class ModbusASCIITransport extends ModbusSerialTransport {

    private final byte[] inBuffer = new byte[Modbus.MAX_MESSAGE_LENGTH];

    //to read message from
    private final AccessibleDataInput in = J2ModUtils.dataInput(inBuffer);

    //to buffer message to
    private final AccessibleDataOutput buffer = J2ModUtils.dataOutput(inBuffer);

    //write frames
    private final AccessibleDataOutput out = J2ModUtils.dataOutput(Modbus.MAX_MESSAGE_LENGTH);

    @NonNull
    private final ModbusResponseFactory responseFactory =
            DefaultModbusResponseFactory.getInstance();

    public ModbusASCIITransport(SerialConnection port, boolean echo) {
        super(port, echo);
    }

    @Synchronized("out")
    protected void writeMessageOut(ModbusMessage msg) throws IOException {
        // write message to byte out
        msg.setHeadless(true);
        msg.writeTo(out);
        val buf = out.getBufferCopy();
        val len = out.size();

        // write message
        writeAsciiByte(Modbus.ASCII_FRAME_START_FLAG);

        // PDU
        writeAsciiBytes(buf, len);
        val lrc = Checksum.lrc(buf, 0, len);

        if (log.isDebugEnabled())
            log.debug("writing: {} lrc={}", toHex(buf, 0, len), lrc);

        writeAsciiByte(lrc);
        writeAsciiByte(Modbus.ASCII_FRAME_END_FLAG);
        out.reset();

        // for RS485
        // clears out the echoed message and read back the echoed message
        if (isEcho())
            if (!readEcho(len + 3))
                throw new IllegalStateException("todo");
    }

    @Override
    public ModbusRequest readRequestIn() throws IOException {
        boolean done = false;
        ModbusRequest request = null;
        int in;

        do {
            // 1. Skip to FRAME_START
            int garbage = 0;
            while ((readAsciiByte()) != Modbus.ASCII_FRAME_START_FLAG) {
                // Nothing to do
                garbage++;
            }
            log.trace("garbage bytes=" + garbage);

            // 2. Read to FRAME_END
            synchronized (inBuffer) {
                buffer.reset();
                while ((in = readAsciiByte()) != Modbus.ASCII_FRAME_END_FLAG) {
                    if (in == -1)
                        throw new IOException("I/O exception - Serial port timeout");
                    buffer.writeByte(in);
                }
                //check LRC
                val checksum = Checksum.lrc(inBuffer, 0, buffer.size(), 1);
                if (inBuffer[buffer.size() - 1] != checksum) {
                    log.warn("bad lrc, frame skipped");
                    continue;
                }
                this.in.reset(inBuffer, buffer.size());
                int unitID = this.in.readUnsignedByte();

                int functionCode = this.in.readUnsignedByte();
                //create request
                request = DefaultModbusRequestFactory
                        .getInstance()
                        .create(functionCode);
                request.setHeadless(true);
                //read message
                this.in.reset(inBuffer, buffer.size());
                request.readFrom(this.in);
            }
            done = true;
        } while (!done);
        return request;
    }

    @Override
    protected ModbusResponse readResponseIn() throws IOException {
        boolean done = false;
        ModbusResponse response = null;
        int in;

        do {
            //1. Skip to FRAME_START
            while ((in = readAsciiByte()) != Modbus.ASCII_FRAME_START_FLAG) {
                if (in == -1) {
                    throw new IOException("I/O exception - Serial port timeout");
                }
            }
            //2. Read to FRAME_END
            synchronized (inBuffer) {
                buffer.reset();
                while ((in = readAsciiByte()) != Modbus.ASCII_FRAME_END_FLAG) {
                    if (in == -1) {
                        throw new IOException("I/O exception - Serial port timeout");
                    }
                    buffer.writeByte(in);
                }
                int len = buffer.size();
                if (log.isDebugEnabled()) {
                    log.debug("Received: {}", toHex(inBuffer, 0, len));
                }
                //check LRC
                if (inBuffer[len - 1] != Checksum.lrc(inBuffer, 0, len, 1)) {
                    continue;
                }

                this.in.reset(inBuffer, buffer.size());
                this.in.readUnsignedByte();
                // JDC: To check slave unit identifier in a response we need to know
                // the slave id in the request.  This is not tracked since slaves
                // only respond when a master request is made and there is only one
                // master.  We are the only master, so we can assume that this
                // response message is from the slave responding to the last request.
                in = this.in.readUnsignedByte();
                //create request
                response = responseFactory.create(in);
                response.setHeadless(true);
                //read message
                this.in.reset(inBuffer, buffer.size());
                response.readFrom(this.in);
            }
            done = true;
        } while (!done);
        return response;
    }

    private void readFrame(boolean checkTimeout) throws IOException {
        boolean done = false;
        ModbusRequest request = null;
        int in;

        do {
            // 1. Skip to FRAME_START
            int garbage = 0;
            while ((in = readAsciiByte()) != Modbus.ASCII_FRAME_START_FLAG) {
                garbage++;
                if (in == -1) {
                    log.warn("I/O exception - Serial port timeout");
                    return null;
                }
            }
            log.trace("garbage bytes=" + garbage);

            // 2. Read to FRAME_END
            synchronized (inBuffer) {
                buffer.reset();
                while ((in = readAsciiByte()) != Modbus.ASCII_FRAME_END_FLAG) {
                    if (in == -1)
                        throw new IOException("I/O exception - Serial port timeout");
                    buffer.writeByte(in);
                }
                //check LRC
                val checksum = Checksum.lrc(inBuffer, 0, buffer.size(), 1);
                if (inBuffer[buffer.size() - 1] != checksum) {
                    log.warn("bad lrc, frame skipped");
                    continue;
                }
                this.in.reset(inBuffer, buffer.size());
                int unitID = this.in.readUnsignedByte();

                int functionCode = this.in.readUnsignedByte();
                //create request
                request = DefaultModbusRequestFactory
                        .getInstance()
                        .create(functionCode);
                request.setHeadless(true);
                //read message
                this.in.reset(inBuffer, buffer.size());
                request.readFrom(this.in);
            }
            done = true;
        } while (!done);
        return request;
    }

}
