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
package cc.koosha.modbus.io.net;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.modbus.transport.ModbusRTUTCPTransport;
import cc.koosha.modbus.modbus.transport.ModbusTCPTransport;
import cc.koosha.modbus.xinternal.J2ModUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Class that implements a TCPMasterConnection.
 * <p>
 * TODO 9 - Architecture: move to 'master' package.
 *
 * @author Dieter Wimberger
 * @author Julie Haugh
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public final class TCPMasterConnection implements Closeable {

    private Socket socket;
    private int timeout = Modbus.DEFAULT_TIMEOUT;
    private boolean connected;

    @Setter
    private InetAddress address;

    private int port = Modbus.DEFAULT_PORT;

    @Setter
    @Getter
    private ModbusTCPTransport transport;

    private boolean useRtuOverTcp = false;

    /**
     * useUrgentData - sent a byte of urgent data when testing the TCP connection.
     */
    private boolean useUrgentData = false;

    /**
     * Prepares the associated <tt>ModbusTransport</tt> of this
     * <tt>TCPMasterConnection</tt> for use.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws IOException if an I/O related error occurs.
     */
    private void prepareTransport(boolean useRtuOverTcp) throws IOException {
        // If we don't have a transport, or the transport type has changed
        if (transport == null || (this.useRtuOverTcp != useRtuOverTcp)) {
            // Save the flag to tell us which transport type to use
            this.useRtuOverTcp = useRtuOverTcp;

            // Select the correct transport
            if (useRtuOverTcp) {
                log.trace("prepareTransport() -> using RTU over TCP transport.");
                transport = new ModbusRTUTCPTransport(socket);
                transport.setMaster(this);
            }
            else {
                log.trace("prepareTransport() -> using standard TCP transport.");
                transport = new ModbusTCPTransport(socket);
                transport.setMaster(this);
            }
        }
        else {
            log.trace("prepareTransport() -> using custom transport: {}", transport
                    .getClass()
                    .getSimpleName());
            transport.setSocket(socket);
        }
    }

    /**
     * Opens this <tt>TCPMasterConnection</tt>.
     *
     * @throws Exception if there is a network failure.
     */
    public synchronized void connect() throws Exception {
        connect(useRtuOverTcp);
    }

    /**
     * Opens this <tt>TCPMasterConnection</tt>.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws Exception if there is a network failure.
     */
    public synchronized void connect(boolean useRtuOverTcp) throws IOException {
        if (!isConnected()) {
            log.debug("connect()");

            // Create a socket without auto-connecting

            socket = new Socket();
            socket.setReuseAddress(true);
            socket.setSoLinger(true, 1);
            socket.setKeepAlive(true);

            // Connect - only wait for the timeout number of milliseconds

            socket.connect(new InetSocketAddress(address, port), timeout);

            // Prepare the transport

            prepareTransport(useRtuOverTcp);
            connected = true;
        }
    }

    /**
     * Tests if this <tt>TCPMasterConnection</tt> is connected.
     *
     * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
     */
    public synchronized boolean isConnected() {
        if (connected && socket != null) {
            if (!socket.isConnected() || socket.isClosed() || socket.isInputShutdown() || socket
                    .isOutputShutdown()) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    log.error("Socket exception", e);
                }
                finally {
                    connected = false;
                }
            }
            else {
                /*
                 * When useUrgentData is set, a byte of urgent data
                 * will be sent to the server to test the connection. If
                 * the connection is actually broken, an IException will
                 * occur and the connection will be closed.
                 *
                 * Note: RFC 6093 has decreed that we stop using urgent
                 * data.
                 */
                if (useUrgentData) {
                    try {
                        socket.sendUrgentData(0);
                        J2ModUtils.sleep((long) 5);
                    }
                    catch (IOException e) {
                        connected = false;
                        try {
                            socket.close();
                        }
                        catch (IOException e1) {
                            // Do nothing.
                        }
                    }
                }
            }
        }
        return connected;
    }

    @Override
    public void close() throws IOException {
        try {
            transport.close();
        }
        finally {
            connected = false;
        }
    }

    /**
     * Sets the transport type to use Normally set during the connection but can also be set after a connection has been
     * established
     *
     * @param useRtuOverTcp True if the transport should be interpreted as RTU over tCP
     * @throws Exception If the connection is not valid
     */
    public void setUseRtuOverTcp(boolean useRtuOverTcp) throws Exception {
        this.useRtuOverTcp = useRtuOverTcp;
        if (isConnected()) {
            prepareTransport(useRtuOverTcp);
        }
    }

}
