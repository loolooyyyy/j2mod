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

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Class implementing a <tt>UDPMasterTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
final class UDPMasterTerminal extends AbstractUDPTerminal {

    private static final int SEND_BUFFER_SIZE = 1024;
    private static final int RECV_BUFFER_SIZE = SEND_BUFFER_SIZE;

    public UDPMasterTerminal(InetAddress addr) {
        super(addr);
    }

    public UDPMasterTerminal() {
        super(null);
    }

    @Synchronized
    @Override
    public void activate() throws Exception {
        if (!isActive()) {
            if (!hasSocket()) {
                setSocket(new DatagramSocket());
            }

            if (log.isDebugEnabled()) {
                log.debug("socket={}", getSocket().toString());
                log.debug("addr={}, port={}", getAddress().toString(), getPort());
            }

            getSocket().setReceiveBufferSize(RECV_BUFFER_SIZE);
            getSocket().setSendBufferSize(SEND_BUFFER_SIZE);
            getSocket().setSoTimeout(getTimeout());

            setActive(true);
        }
        log.debug("UDPMasterTerminal::activated");
    }

    @Synchronized
    @Override
    public void deactivate() {
        try {
            if (hasSocket())
                getSocket().close();
            setActive(false);
        }
        catch (Exception ex) {
            log.error("Error closing socket", ex);
        }
    }

    @Synchronized
    @Override
    public void sendMessage(byte[] msg) throws Exception {
        DatagramPacket req = new DatagramPacket(msg, msg.length, getAddress(), getPort());
        getSocket().send(req);
    }

    @Synchronized
    @Override
    public byte[] receiveMessage() throws Exception {

        // The longest possible DatagramPacket is 256 bytes (Modbus message
        // limit) plus the 6 byte header.
        byte[] buffer = new byte[262];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        getSocket().setSoTimeout(getTimeout());
        getSocket().receive(packet);
        return buffer;
    }

}
