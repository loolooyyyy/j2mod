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
import java.io.IOException;
import cc.koosha.modbus.io.net.AbstractUDPTerminal;
import cc.koosha.modbus.msg.ModbusMessage;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.ModbusResponseFactory;
import cc.koosha.modbus.msg.request.DefaultModbusRequestFactory;
import cc.koosha.modbus.msg.response.DefaultModbusResponseFactory;
import cc.koosha.modbus.xinternal.AccessibleDataInput;
import cc.koosha.modbus.xinternal.AccessibleDataOutput;
import cc.koosha.modbus.xinternal.J2ModUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.InterruptedIOException;
import java.util.Arrays;


/**
 * Class that implements the Modbus UDP transport flavor.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public final class ModbusUDPTransport implements ModbusTransport {

    //instance attributes
    @Getter
    private AbstractUDPTerminal terminal;
    private final AccessibleDataOutput byteOutputStream =
            J2ModUtils.dataOutput(Modbus.MAX_MESSAGE_LENGTH);
    private final AccessibleDataInput byteInputStream =
            J2ModUtils.dataInput(Modbus.MAX_MESSAGE_LENGTH);

    /**
     * Constructs a new <tt>ModbusTransport</tt> instance, for a given
     * <tt>UDPTerminal</tt>.
     * <p>
     *
     * @param terminal the <tt>UDPTerminal</tt> used for message transport.
     */
    public ModbusUDPTransport(AbstractUDPTerminal terminal) {
        this.terminal = terminal;
    }

    private int timeout = Modbus.DEFAULT_TIMEOUT;

    @NonNull
    private final ModbusResponseFactory responseFactory = new DefaultModbusResponseFactory();

    @Override
    public void setTimeout(int time) {
        this.timeout = time;
        if (terminal != null) {
            terminal.setTimeout(timeout);
        }
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public ModbusRequest readRequest() throws IOException {
        try {
            ModbusRequestX req;
            synchronized (byteInputStream) {
                byteInputStream.reset(terminal.receiveMessage());
                byteInputStream.skip(7);
                int functionCode = byteInputStream.readUnsignedByte();
                byteInputStream.reset();
                req = DefaultModbusRequestFactory.getInstance()
                                                 .create(functionCode);
                req.readFrom(byteInputStream);
            }
            return req;
        }
        catch (Exception ex) {
            throw new IOException("I/O exception - failed to read", ex);
        }
    }

    @Override
    public ModbusResponse readResponse() throws IOException {

        try {
            ModbusResponse res;
            synchronized (byteInputStream) {
                byteInputStream.reset(terminal.receiveMessage());
                byteInputStream.skip(7);
                int functionCode = byteInputStream.readUnsignedByte();
                byteInputStream.reset();
                res = responseFactory.create(functionCode);
                res.readFrom(byteInputStream);
            }
            return res;
        }
        catch (InterruptedIOException ioex) {
            throw new IOException("Socket was interrupted", ioex);
        }
        catch (Exception ex) {
            log.debug("I/O exception while reading modbus response.", ex);
            throw new IOException("I/O exception - failed to read", ex);
        }
    }

    /**
     * Writes the request/response message to the port
     *
     * @param msg Message to write
     * @throws IOException If the port cannot be written to
     */
    @Override
    public void writeMessage(ModbusMessage msg) throws IOException {
        try {
            synchronized (byteOutputStream) {
                int len = msg.getOutputLength();
                byteOutputStream.reset();
                msg.writeTo(byteOutputStream);
                byte data[] = byteOutputStream.getBufferCopy();
                data = Arrays.copyOf(data, len);
                terminal.sendMessage(data);
            }
        }
        catch (Exception ex) {
            throw new IOException("I/O exception - failed to write", ex);
        }
    }

}