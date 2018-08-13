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
import cc.koosha.modbus.msg.ModbusMessage;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.ModbusResponseFactory;
import cc.koosha.modbus.msg.request.DefaultModbusRequestFactory;
import cc.koosha.modbus.msg.response.DefaultModbusResponseFactory;
import cc.koosha.modbus.util.Checksum;
import cc.koosha.modbus.xinternal.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Class that implements the ModbusRTU transport flavor.
 *
 * @author John Charlton
 * @author Dieter Wimberger
 * @author Julie Haugh
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public class ModbusRTUTransport extends ModbusSerialTransport {

    private static final Logger logger = LoggerFactory.getLogger(ModbusRTUTransport.class);

    private final byte[] inBuffer = new byte[Modbus.MAX_MESSAGE_LENGTH];
    private final AccessibleDataInput
            byteInputStream = J2ModUtils.dataInput(inBuffer); // to read message from
    private final AccessibleDataOutput byteInputOutputStream =
            J2ModUtils.dataOutput(inBuffer); // to buffer message to
    private final AccessibleDataOutput byteOutputStream =
            J2ModUtils.dataOutput(Modbus.MAX_MESSAGE_LENGTH); // write frames
    private byte[] lastRequest = null;

    @NonNull
    private final ModbusResponseFactory responseFactory = new DefaultModbusResponseFactory();

    public ModbusRTUTransport() {
        super(commPort);
    }

    /**
     * Read the data for a request of a given fixed size
     *
     * @param byteCount Byte count excluding the 2 byte CRC
     * @param out       Output buffer to populate
     * @throws IOException If data cannot be read from the port
     */
    private void readRequestData(int byteCount, AccessibleDataOutput out) throws
                                                                          IOException {
        byteCount += 2;
        byte inpBuf[] = new byte[byteCount];
        readBytes(inpBuf, byteCount);
        out.write(inpBuf, 0, byteCount);
    }

    /**
     * getRequest - Read a request, after the unit and function code
     *
     * @param function - Modbus function code
     * @param out      - Byte stream buffer to hold actual message
     */
    private void getRequest(int function, AccessibleDataOutput out) throws
                                                                    IOException {
        int byteCount;
        byte inpBuf[] = new byte[256];
        try {
            if ((function & 0x80) == 0) {
                switch (function) {
                    case Modbus.READ_EXCEPTION_STATUS:
                    case Modbus.READ_COMM_EVENT_COUNTER:
                    case Modbus.READ_COMM_EVENT_LOG:
                    case Modbus.REPORT_SLAVE_ID:
                        readRequestData(0, out);
                        break;

                    case Modbus.READ_FIFO_QUEUE:
                        readRequestData(2, out);
                        break;

                    case Modbus.READ_MEI:
                        readRequestData(3, out);
                        break;

                    case Modbus.READ_COILS:
                    case Modbus.READ_INPUT_DISCRETES:
                    case Modbus.READ_MULTIPLE_REGISTERS:
                    case Modbus.READ_INPUT_REGISTERS:
                    case Modbus.WRITE_COIL:
                    case Modbus.WRITE_SINGLE_REGISTER:
                        readRequestData(4, out);
                        break;
                    case Modbus.MASK_WRITE_REGISTER:
                        readRequestData(6, out);
                        break;

                    case Modbus.READ_FILE_RECORD:
                    case Modbus.WRITE_FILE_RECORD:
                        byteCount = readByte();
                        out.write(byteCount);
                        readRequestData(byteCount, out);
                        break;

                    case Modbus.WRITE_MULTIPLE_COILS:
                    case Modbus.WRITE_MULTIPLE_REGISTERS:
                        readBytes(inpBuf, 4);
                        out.write(inpBuf, 0, 4);
                        byteCount = readByte();
                        out.write(byteCount);
                        readRequestData(byteCount, out);
                        break;

                    case Modbus.READ_WRITE_MULTIPLE:
                        readRequestData(8, out);
                        byteCount = readByte();
                        out.write(byteCount);
                        readRequestData(byteCount, out);
                        break;

                    default:
                        throw new IOException(String.format("getResponse unrecognised function code [%s]", function));
                }
            }
        }
        catch (IOException e) {
            throw new IOException("getResponse serial port exception");
        }
    }

    /**
     * getResponse - Read a <tt>ModbusResponse</tt> from a slave.
     *
     * @param function The function code of the request
     * @param out      The output buffer to put the result
     * @throws IOException If data cannot be read from the port
     */
    private void getResponse(int function, AccessibleDataOutput out) throws
                                                                     IOException {
        byte inpBuf[] = new byte[256];
        try {
            if ((function & 0x80) == 0) {
                switch (function) {
                    case Modbus.READ_COILS:
                    case Modbus.READ_INPUT_DISCRETES:
                    case Modbus.READ_MULTIPLE_REGISTERS:
                    case Modbus.READ_INPUT_REGISTERS:
                    case Modbus.READ_COMM_EVENT_LOG:
                    case Modbus.REPORT_SLAVE_ID:
                    case Modbus.READ_FILE_RECORD:
                    case Modbus.WRITE_FILE_RECORD:
                    case Modbus.READ_WRITE_MULTIPLE:
                        // Read the data payload byte count. There will be two
                        // additional CRC bytes afterwards.
                        int cnt = readByte();
                        out.write(cnt);
                        readRequestData(cnt, out);
                        break;

                    case Modbus.WRITE_COIL:
                    case Modbus.WRITE_SINGLE_REGISTER:
                    case Modbus.READ_COMM_EVENT_COUNTER:
                    case Modbus.WRITE_MULTIPLE_COILS:
                    case Modbus.WRITE_MULTIPLE_REGISTERS:
                    case Modbus.READ_SERIAL_DIAGNOSTICS:
                        // read status: only the CRC remains after the two data
                        // words.
                        readRequestData(4, out);
                        break;

                    case Modbus.READ_EXCEPTION_STATUS:
                        // read status: only the CRC remains after exception status
                        // byte.
                        readRequestData(1, out);
                        break;

                    case Modbus.MASK_WRITE_REGISTER:
                        // eight bytes in addition to the address and function codes
                        readRequestData(6, out);
                        break;

                    case Modbus.READ_FIFO_QUEUE:
                        int b1, b2;
                        b1 = (byte) (readByte() & 0xFF);
                        out.write(b1);
                        b2 = (byte) (readByte() & 0xFF);
                        out.write(b2);
                        int byteCount = J2ModDataUtil.makeWord(b1, b2);
                        readRequestData(byteCount, out);
                        break;

                    case Modbus.READ_MEI:
                        // read the subcode. We only support 0x0e.
                        int sc = readByte();
                        if (sc != 0x0e) {
                            throw new IOException("Invalid subfunction code");
                        }
                        out.write(sc);
                        // next few bytes are just copied.
                        int id, fieldCount;
                        readBytes(inpBuf, 5);
                        out.write(inpBuf, 0, 5);
                        fieldCount = (int) inpBuf[4];
                        for (int i = 0; i < fieldCount; i++) {
                            id = readByte();
                            out.write(id);
                            int len = readByte();
                            out.write(len);
                            readBytes(inpBuf, len);
                            out.write(inpBuf, 0, len);
                        }
                        if (fieldCount == 0) {
                            int err = readByte();
                            out.write(err);
                        }
                        // now get the 2 CRC bytes
                        readRequestData(0, out);
                        break;

                    default:
                        throw new IOException(String.format("getResponse unrecognised function code [%s]", function));

                }
            }
            else {
                // read the exception code, plus two CRC bytes.
                readRequestData(1, out);

            }
        }
        catch (IOException e) {
            throw new IOException(String.format("getResponse serial port exception - %s", e
                    .getMessage()));
        }
    }

    /**
     * Writes the Modbus message to the comms port
     *
     * @param msg a <code>ModbusMessage</code> value
     * @throws IOException If an error occurred bundling the message
     */
    protected void writeMessageOut(ModbusMessage msg) throws IOException {
        try {
            int len;
            synchronized (byteOutputStream) {
                // first clear any input from the receive buffer to prepare
                // for the reply since RTU doesn't have message delimiters
                discard();
                // write message to byte out
                byteOutputStream.reset();
                msg.setHeadless(true);
                msg.writeTo(byteOutputStream);
                len = byteOutputStream.size();
                int[] crc = Checksum.crc(byteOutputStream.getBufferCopy(), 0, len);
                byteOutputStream.writeByte(crc[0]);
                byteOutputStream.writeByte(crc[1]);
                // write message
                writeBytes(byteOutputStream.getBufferCopy(), byteOutputStream.size());
                if (logger.isDebugEnabled()) {
                    logger.debug("Sent: {}", J2ModDebugUtils.toHex(byteOutputStream
                                                                           .getBufferCopy(), 0, byteOutputStream
                                                                           .size()));
                }
                // clears out the echoed message
                // for RS485
                if (isEcho()) {
                    if (!readEcho(len + 2))
                        throw new IllegalStateException("todo");
                }
                lastRequest = new byte[len];
                System.arraycopy(byteOutputStream.getBufferCopy(), 0, lastRequest, 0, len);
            }
        }
        catch (IOException ex) {
            throw new IOException("I/O failed to write");
        }
    }

    @Override
    protected ModbusRequest readRequestIn() throws IOException {
        boolean done;
        ModbusRequestX request;
        int dlength;

        try {
            do {
                // 1. read to function code, create request and read function
                // specific bytes
                synchronized (byteInputStream) {
                    int uid = readByte();
                    if (uid != -1) {
                        int fc = readByte();
                        byteInputOutputStream.reset();
                        byteInputOutputStream.writeByte(uid);
                        byteInputOutputStream.writeByte(fc);

                        // create response to acquire length of message
                        request = DefaultModbusRequestFactory.getInstance()
                                                             .create(fc);
                        request.setHeadless(true);

                        /*
                         * With Modbus RTU, there is no end frame. Either we
                         * assume the message is complete as is or we must do
                         * function specific processing to know the correct
                         * length. To avoid moving frame timing to the serial
                         * input functions, we set the timeout and to message
                         * specific parsing to read a response.
                         */
                        getRequest(fc, byteInputOutputStream);
                        dlength = byteInputOutputStream.size() - 2; // less the crc
                        if (logger.isDebugEnabled()) {
                            logger.debug("Response: {}", J2ModDebugUtils.toHex(byteInputOutputStream
                                                                                       .getBufferCopy(), 0, dlength + 2));
                        }

                        byteInputStream.reset(inBuffer, dlength);

                        // check CRC
                        int[] crc = Checksum.crc(inBuffer, 0, dlength); // does not include CRC
                        if (J2ModDataUtil.unsignedByteToInt(inBuffer[dlength]) != crc[0] || J2ModDataUtil
                                .unsignedByteToInt(inBuffer[dlength + 1]) != crc[1]) {
                            logger.debug("CRC should be {}, {}", crc[0], crc[1]);

                            // Drain the input in case the frame was misread and more
                            // was to follow.
                            discard();
                            throw new IOException("CRC Error in received frame: "
                                                          + dlength);
                        }
                    }
                    else {
                        throw new IOException("Error reading response");
                    }

                    // read response
                    byteInputStream.reset(inBuffer, dlength);
                    request.readFrom(byteInputStream);
                    done = true;
                }
            } while (!done);
            return request;
        }
        catch (IOException ex) {
            // An exception mostly means there is no request. The master should
            // retry the request.
            return null;
        }
    }

    /**
     * readResponse - Read the bytes for the response from the slave.
     *
     * @return a <tt>ModbusRespose</tt>
     * @throws IOException If the response cannot be read from the socket/port
     */
    protected ModbusResponse readResponseIn() throws IOException {
        boolean done;
        ModbusResponse response;
        int dlength;

        try {
            do {
                // 1. read to function code, create request and read function
                // specific bytes
                synchronized (byteInputStream) {
                    int uid = readByte();
                    if (uid != -1) {
                        int fc = readByte();
                        byteInputOutputStream.reset();
                        byteInputOutputStream.writeByte(uid);
                        byteInputOutputStream.writeByte(fc);

                        // create response to acquire length of message
                        response = responseFactory.create(fc);
                        response.setHeadless(true);

                        /*
                         * With Modbus RTU, there is no end frame. Either we
                         * assume the message is complete as is or we must do
                         * function specific processing to know the correct
                         * length. To avoid moving frame timing to the serial
                         * input functions, we set the timeout and to message
                         * specific parsing to read a response.
                         */
                        getResponse(fc, byteInputOutputStream);
                        dlength = byteInputOutputStream.size() - 2; // less the crc
                        if (logger.isDebugEnabled()) {
                            logger.debug("Response: {}", J2ModDebugUtils.toHex(byteInputOutputStream
                                                                                       .getBufferCopy(), 0, dlength + 2));
                        }
                        byteInputStream.reset(inBuffer, dlength);

                        // check CRC
                        int[] crc = Checksum.crc(inBuffer, 0, dlength); // does not include CRC
                        if (J2ModDataUtil.unsignedByteToInt(inBuffer[dlength]) != crc[0] || J2ModDataUtil
                                .unsignedByteToInt(inBuffer[dlength + 1]) != crc[1]) {
                            logger.debug("CRC should be {}, {}", crc[0], crc[1]);
                            throw new IOException("CRC Error in received frame: " + dlength);
                        }
                    }
                    else {
                        throw new IOException("Error reading response");
                    }

                    // read response
                    byteInputStream.reset(inBuffer, dlength);
                    response.readFrom(byteInputStream);
                    done = true;
                }
            } while (!done);
            return response;
        }
        catch (IOException ex) {
            throw new IOException("I/O exception - failed to read response for request", ex);
        }
    }
}
