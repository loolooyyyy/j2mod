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

import cc.koosha.modbus.io.net.TCPMasterConnection;
import cc.koosha.modbus.msg.ModbusMessage;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.ModbusResponseFactory;
import cc.koosha.modbus.msg.request.DefaultModbusRequestFactory;
import cc.koosha.modbus.msg.response.DefaultModbusResponseFactory;
import cc.koosha.modbus.util.Checksum;
import cc.koosha.modbus.xinternal.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static cc.koosha.modbus.xinternal.J2ModDataUtil.registerValueToShort;


/**
 * Class that implements the Modbus transport flavor.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public class ModbusTCPTransport implements ModbusTransport {

    // instance attributes
    private DataInputStream dataInputStream; // input stream
    private DataOutputStream dataOutputStream; // output stream
    private final J2ModFastInputStream byteInputStream =
            J2ModUtils.dataInput(Modbus.MAX_MESSAGE_LENGTH + 6);
    private final J2ModFastOutputStream byteOutputStream =
            J2ModUtils.dataOutput(Modbus.MAX_MESSAGE_LENGTH + 6); // write frames
    protected Socket socket = null;
    protected TCPMasterConnection master = null;
    private boolean headless = false; // Some TCP implementations are.
    private int timeout = Modbus.DEFAULT_TIMEOUT;

    @NonNull
    private final ModbusResponseFactory responseFactory =
            DefaultModbusResponseFactory.getInstance();

    /**
     * Default constructor
     */
    public ModbusTCPTransport() {
    }

    /**
     * Constructs a new <tt>ModbusTransport</tt> instance, for a given
     * <tt>Socket</tt>.
     * <p>
     *
     * @param socket the <tt>Socket</tt> used for message transport.
     */
    public ModbusTCPTransport(Socket socket) {
        try {
            setSocket(socket);
            socket.setSoTimeout(timeout);
        }
        catch (IOException ex) {
            log.debug("ModbusTCPTransport::Socket invalid");

            throw new IllegalStateException("Socket invalid", ex);
        }
    }

    /**
     * Sets the <tt>Socket</tt> used for message transport and prepares the
     * streams used for the actual I/O.
     *
     * @param socket the <tt>Socket</tt> used for message transport.
     * @throws IOException if an I/O related error occurs.
     */
    public void setSocket(Socket socket) throws IOException {
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
        }
        this.socket = socket;
        prepareStreams(socket);
    }

    /**
     * Set the transport to be headless
     */
    public void setHeadless() {
        headless = true;
    }

    /**
     * Set the transport to be headless
     *
     * @param headless True if headless
     */
    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    /**
     * Sets the master connection for the transport to use
     *
     * @param master Master
     */
    public void setMaster(TCPMasterConnection master) {
        this.master = master;
    }


    @Override
    public void close() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }

    @Override
    public ModbusRequest readRequest() throws IOException {
        ModbusRequest req;
        try {
            byteInputStream.reset();

            synchronized (byteInputStream) {
                byte[] buffer = byteInputStream.getBuffer();

                if (!headless) {
                    dataInputStream.readFully(buffer, 0, 6);

                    // The transaction ID must be treated as an unsigned short in
                    // order for validation to work correctly.

                    int transaction = registerValueToShort(buffer, 0) & 0x0000FFFF;
                    int protocol = registerValueToShort(buffer, 2);
                    int count = registerValueToShort(buffer, 4);

                    dataInputStream.readFully(buffer, 6, count);

                    if (log.isDebugEnabled()) {
                        log.debug("Read: {}", J2ModDebugUtils.toHex(buffer, 0, count + 6));
                    }

                    byteInputStream.reset(buffer, (6 + count));
                    byteInputStream.skip(6);

                    int unit = byteInputStream.readByte();
                    int functionCode = byteInputStream.readUnsignedByte();

                    byteInputStream.reset();
                    req = DefaultModbusRequestFactory.getInstance()
                                                     .create(functionCode);
                    req.setUnitID(unit);
                    req.setHeadless(false);

                    req.setTransactionID(transaction);
                    req.setProtocolID(protocol);
                    req.setDataLength(count);

                    req.readFrom(byteInputStream);
                }
                else {

                    // This is a headless request.

                    int unit = dataInputStream.readByte();
                    int function = dataInputStream.readByte();

                    req = DefaultModbusRequestFactory.getInstance()
                                                     .create(function);
                    req.setUnitID(unit);
                    req.setHeadless(true);
                    req.readData(dataInputStream);

                    // Discard the CRC. This is a TCP/IP connection, which has
                    // proper error correction and recovery.

                    dataInputStream.readShort();
                    if (log.isDebugEnabled()) {
                        log.debug("Read: {}", J2ModDebugUtils.toHex(req));
                    }
                }
            }
            return req;
        }
        catch (EOFException eoex) {
            throw new IOException("End of File");
        }
        catch (SocketTimeoutException x) {
            throw new IOException("Timeout reading request", x);
        }
        catch (SocketException sockex) {
            throw new IOException("Socket Exception", sockex);
        }
        catch (IOException ex) {
            throw new IOException("I/O exception - failed to read", ex);
        }
    }

    @Override
    public ModbusResponse readResponse() throws IOException {
        try {
            ModbusResponse response;

            synchronized (byteInputStream) {
                // use same buffer
                byte[] buffer = byteInputStream.getBuffer();
                log.debug("Reading response...");
                if (!headless) {
                    // All Modbus TCP transactions start with 6 bytes. Get them.
                    dataInputStream.readFully(buffer, 0, 6);

                    /*
                     * The transaction ID is the first word (offset 0) in the
                     * data that was just read. It will be echoed back to the
                     * requester.
                     *
                     * The protocol ID is the second word (offset 2) in the
                     * data. It should always be 0, but I don't check.
                     *
                     * The length of the payload is the third word (offset 4) in
                     * the data that was just read. That's what I need in order
                     * to read the rest of the response.
                     */
                    int transaction = registerValueToShort(buffer, 0) & 0x0000FFFF;
                    int protocol = registerValueToShort(buffer, 2);
                    int count = registerValueToShort(buffer, 4);

                    dataInputStream.readFully(buffer, 6, count);
                    byteInputStream.reset(buffer, (6 + count));
                    byteInputStream.reset();
                    byteInputStream.skip(7);
                    int function = byteInputStream.readUnsignedByte();
                    response = responseFactory.create(function);

                    // Rewind the input buffer, then read the data into the
                    // response.
                    byteInputStream.reset();
                    response.readFrom(byteInputStream);

                    response.setTransactionID(transaction);
                    response.setProtocolID(protocol);
                }
                else {
                    // This is a headless response. It has the same format as a
                    // RTU over Serial response.
                    int unit = dataInputStream.readByte();
                    int function = dataInputStream.readByte();

                    response = responseFactory.create(function);
                    response.setUnitID(unit);
                    response.setHeadless(true);
                    response.readData(dataInputStream);

                    // Now discard the CRC. Which hopefully wasn't needed
                    // because this is a TCP transport.
                    dataInputStream.readShort();
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully read: {}", J2ModDebugUtils.toHex(response));
            }
            return response;
        }
        catch (EOFException ex1) {
            throw new IOException("Premature end of stream (Message truncated)", ex1);
        }
        catch (SocketTimeoutException ex2) {
            throw new IOException("Socket timeout reading response", ex2);
        }
        catch (Exception ex3) {
            throw new IOException("General exception - failed to read", ex3);
        }
    }

    /**
     * Prepares the input and output streams of this <tt>ModbusTCPTransport</tt>
     * instance based on the given socket.
     *
     * @param socket the socket used for communications.
     * @throws IOException if an I/O related error occurs.
     */
    private void prepareStreams(Socket socket) throws IOException {

        // Close any open streams if I'm being called because a new socket was
        // set to handle this transport.
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        }
        catch (IOException x) {
            // Do nothing.
        }

        dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /**
     * Writes a <tt>ModbusMessage</tt> to the output stream of this
     * <tt>ModbusTransport</tt>.
     * <p>
     *
     * @param msg           a <tt>ModbusMessage</tt>.
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws IOException data cannot be written properly to the raw output
     *                     stream of this <tt>ModbusTransport</tt>.
     */
    void writeMessage(ModbusMessage msg, boolean useRtuOverTcp) throws
                                                                IOException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Sending: {}", J2ModDebugUtils.toHex(msg));
            }
            byte message[] = msg.getMessage();

            byteOutputStream.reset();
            if (!headless) {
                byteOutputStream.writeShort(msg.getTransactionID());
                byteOutputStream.writeShort(msg.getProtocolID());
                byteOutputStream.writeShort((message != null
                                             ? message.length
                                             : 0) + 2);
            }
            byteOutputStream.writeByte(msg.getUnitID());
            byteOutputStream.writeByte(msg.getFunctionCode());
            if (message != null && message.length > 0) {
                byteOutputStream.write(message);
            }

            // Add CRC for RTU over TCP
            if (useRtuOverTcp) {
                int len = byteOutputStream.size();
                int[] crc = Checksum.crc(byteOutputStream.getBufferCopy(), 0, len);
                byteOutputStream.writeByte(crc[0]);
                byteOutputStream.writeByte(crc[1]);
            }

            dataOutputStream.write(byteOutputStream.toByteArray());
            dataOutputStream.flush();
            if (log.isDebugEnabled()) {
                log.debug("Successfully sent: {}", J2ModDebugUtils.toHex(byteOutputStream
                                                                                 .toByteArray()));
            }
            // write more sophisticated exception handling
        }
        catch (SocketException ex1) {
            if (master != null && !master.isConnected()) {
                try {
                    master.connect(useRtuOverTcp);
                }
                catch (Exception e) {
                    // Do nothing.
                }
            }
            throw new IOException("I/O socket exception - failed to write", ex1);
        }
        catch (Exception ex2) {
            throw new IOException("General exception - failed to write", ex2);
        }
    }

}
