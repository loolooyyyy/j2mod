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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Interface defining a <tt>UDPTerminal</tt>.
 * <p>
 * TODO 0 - Abstraction: create an interface.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public abstract class AbstractUDPTerminal {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private InetAddress address;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private int port = Modbus.DEFAULT_PORT;

    @Getter(AccessLevel.PROTECTED)
    private int timeout = Modbus.DEFAULT_TIMEOUT;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private DatagramSocket socket;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private volatile boolean active;

    AbstractUDPTerminal(InetAddress address) {
        this.address = address;
    }

    boolean hasSocket() {
        return getSocket() != null;
    }

    /**
     * Activate this UDPTerminal.
     *
     * @throws Exception on network failure.
     */
    public abstract void activate() throws Exception;

    /**
     * Deactivates this UDPTerminal>.
     */
    public abstract void deactivate() throws Exception;


    /**
     * Sends the given message.
     *
     * @param msg the message as byte[].
     * @throws Exception on failure.
     */
    public abstract void sendMessage(byte[] msg) throws Exception;

    /**
     * Receives and returns a message.
     *
     * @return the message as a newly allocated byte[].
     * @throws Exception on failure.
     */
    public abstract byte[] receiveMessage() throws Exception;

}