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

import cc.koosha.modbus.xinternal.J2ModCollections;
import cc.koosha.modbus.xinternal.J2ModUtils;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static cc.koosha.modbus.xinternal.J2ModDataUtil.registerValueToInt;


/**
 * Class implementing a <tt>UDPSlaveTerminal</tt>.
 * <p>
 * TODO use callback instead of receive. TODO call events
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
final class UDPSlaveTerminal extends AbstractUDPTerminal {

    private static final int SEND_BUFFER_SIZE = 1024;
    private static final int RECV_BUFFER_SIZE = SEND_BUFFER_SIZE;

    private final Map<Integer, DatagramPacket> requests = J2ModCollections.newModifiableMap();

    private final LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<byte[]>();
    private final LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

    private PacketSender packetSender;
    private Thread senderThread = null;

    private PacketReceiver packetReceiver;
    private Thread receiverThread = null;

    /**
     * Creates a slave terminal on the specified adapter address Use 0.0.0.0 to
     * listen on all adapters
     *
     * @param localAddress Local address to bind to
     */
    UDPSlaveTerminal(InetAddress localAddress) {
        super(localAddress);
    }


    @Override
    @Synchronized
    public void activate() throws Exception {
        if (isActive()) {
            log.warn("already active");
        }
        else {
            if (getAddress() != null && getPort() != -1) {
                setSocket(new DatagramSocket(getPort(), getAddress()));
            }
            else {
                setSocket(new DatagramSocket());
                setPort(getSocket().getLocalPort());
                setAddress(getSocket().getLocalAddress());
            }

            // Let's not call these methods if not debugging.
            if (log.isDebugEnabled()) {
                log.debug("socket: {}", getSocket().toString());
                log.debug("addr={}, port={}", getAddress().toString(), getPort());
            }

            getSocket().setReceiveBufferSize(RECV_BUFFER_SIZE);
            getSocket().setSendBufferSize(SEND_BUFFER_SIZE);

            // Never timeout the receive
            getSocket().setSoTimeout(0);

            // Start a sender
            this.packetSender = new PacketSender(getSocket());
            this.senderThread = new Thread(packetSender);
            this.senderThread.start();
            log.debug("sender started");

            // Start a receiver

            this.packetReceiver = new PacketReceiver(getSocket());
            this.receiverThread = new Thread(packetReceiver);
            this.receiverThread.start();
            log.debug("receiver started");

            setActive(true);
            log.debug("activated");
        }
    }

    @Override
    @Synchronized
    public void deactivate() throws Exception {
        if (!isActive()) {
            log.warn("already inactive");
            return;
        }

        try {
            // Will stop and close the socket.
            try {
                packetReceiver.stop();
            }
            finally {
                try {
                    packetSender.stop();
                }
                finally {
                    try {
                        receiverThread.interrupt();
                    }
                    finally {
                        try {
                            senderThread.interrupt();
                        }
                        finally {
                            packetReceiver.stop();
                            packetSender.stop();
                            setActive(false);
                        }
                    }
                }
            }
            log.debug("deactivated");
        }
        catch (Exception ex) {
            log.error("deactivation failed", ex);
            throw ex;
        }
    }

    @Override
    public void sendMessage(byte[] msg) {
        if (!isActive())
            throw new IllegalStateException("not active");
        sendQueue.add(msg);
    }

    @Override
    public byte[] receiveMessage() throws Exception {
        if (!isActive())
            throw new IllegalStateException("not active");
        return receiveQueue.take();
    }


    @RequiredArgsConstructor
    abstract static class BackgroundSocketTask implements Runnable {

        private final static long SOCKET_CLOSE_LOOP_WAIT_TIME = 100;

        private volatile boolean running = true;
        private volatile boolean closed = false;
        private Thread thread;
        protected final DatagramSocket socket_;

        final void stop() {
            this.running = false;
            this.thread.interrupt();

            int closeCount = 10;
            while (!this.closed && closeCount > 0) {
                J2ModUtils.sleep(SOCKET_CLOSE_LOOP_WAIT_TIME);
                if (--closeCount == 0)
                    log.error("could not stop udp thread");
            }

            socket_.close();
            int stopCount = 10;
            log.debug("stopping udp socket");
            while (!socket_.isClosed() && stopCount > 0) {
                J2ModUtils.sleep(SOCKET_CLOSE_LOOP_WAIT_TIME);
                if (--stopCount == 0)
                    log.error("could not stop udp socket");
            }
        }

        public final void run() {
            this.thread = Thread.currentThread();
            do {
                try {
                    loop();
                }
                catch (InterruptedException ie) {
                    log.debug("interrupted");
                    this.running = false;
                }
                catch (Exception ex) {
                    // Ignore the error if we are no longer listening.
                    if (this.running)
                        log.error("udp background job error", ex);
                }
            } while (this.running);
            this.closed = true;
        }

        abstract void loop() throws IOException, InterruptedException;
    }

    /**
     * The background thread that is responsible for sending messages in
     * response to requests
     */
    final class PacketSender extends BackgroundSocketTask {

        PacketSender(DatagramSocket socket_) {
            super(socket_);
        }

        @Override
        void loop() throws IOException, InterruptedException {
            // Pickup the message and corresponding request.
            final byte[] message = sendQueue.take();

            final DatagramPacket req;
            synchronized (requests) {
                req = requests.remove(registerValueToInt(message, 0));
            }

            // Create new packet with corresponding address and port.
            if (req != null)
                socket_.send(new DatagramPacket(
                        message, message.length, req.getAddress(), req.getPort()));
        }
    }

    /**
     * The background thread that receives messages and adds them to the process
     * list for further analysis
     */
    final class PacketReceiver extends BackgroundSocketTask {

        private static final int RECV_BUFFER_MAX_SIZE = 256;

        PacketReceiver(DatagramSocket socket_) {
            super(socket_);
        }

        @Override
        void loop() throws IOException, InterruptedException {
            // 1. Prepare buffer and receive packet.
            val buffer = new byte[RECV_BUFFER_MAX_SIZE];
            val packet = new DatagramPacket(buffer, buffer.length);
            socket_.receive(packet);

            // 2. Extract TID and remember request.
            val tid = registerValueToInt(buffer, 0);
            synchronized (requests) {
                requests.put(tid, packet);
            }

            // 3. place the data buffer in the queue.
            receiveQueue.put(buffer);
        }
    }

}
