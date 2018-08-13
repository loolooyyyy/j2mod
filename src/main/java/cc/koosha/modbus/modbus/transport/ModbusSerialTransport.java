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
import cc.koosha.modbus.xinternal.J2ModDataUtil;
import cc.koosha.modbus.xinternal.J2ModThreadSafeSubscriptionManager;
import cc.koosha.modbus.xinternal.J2ModUtils;
import cc.koosha.modbus.xinternal.SubscriptionManager;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static cc.koosha.modbus.xinternal.J2ModDebugUtils.toHex;
import static java.lang.String.format;


/**
 * @author Dieter Wimberger
 * @author John Charlton
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class ModbusSerialTransport implements ModbusTransport {

    @NonNull
    private final SerialConnection commPort;

    /**
     * require RS-485 echo processing
     */
    @Getter
    private final boolean echo;

    private final SubscriptionManager<ModbusSerialTransport, TransportEvent> subscriptionManager
            = new J2ModThreadSafeSubscriptionManager<ModbusSerialTransport, TransportEvent>(this);

    @Override
    public final void writeMessage(ModbusMessage msg) throws SerialIOException {
        if (msg instanceof ModbusResponse) {
            val r = (ModbusResponse) msg;
            // Slave ID mis-match message
            // if (r.getAuxiliaryType()
            //      .equals(ModbusResponse.AuxiliaryMessageTypes.UNIT_ID_MISMATCH)) {
            //     log.debug("Ignoring response not meant for us");
            //     return;
            // }
            commPort.waitBetweenFrames(0, 0);
        }

        if (!this.commPort.getManager().isOpen())
            this.commPort.getManager().open();

        subscriptionManager.publish(TransportEvent.beforeWrite(msg));
        writeMessageOut(msg);
        commPort.waitBetweenFrames(msg.getOutputLength());
        subscriptionManager.publish(TransportEvent.afterWrite(msg));
    }

    @Override
    public final ModbusRequest readRequest() throws SerialIOException {
        if (!this.commPort.getManager().isOpen())
            this.commPort.getManager().open();
        subscriptionManager.publish(new TransportEvent(TransportEventType.BEFORE_REQUEST, null));
        ModbusRequest req = readRequestIn();
        subscriptionManager.publish(new TransportEvent(TransportEventType.AFTER_REQUEST, null));
        return req;
    }

    @Override
    public final ModbusResponse readResponse() throws SerialIOException {
        if (!this.commPort.getManager().isOpen())
            this.commPort.getManager().open();
        subscriptionManager.publish(new TransportEvent(TransportEventType.BEFORE_RESPONSE, null));
        ModbusResponse res = readResponseIn();
        subscriptionManager.publish(new TransportEvent(TransportEventType.AFTER_RESPONSE, null));
        return res;
    }


    /**
     * Listens continuously on the serial input stream for master request
     * messages and replies if the request slave ID matches its own set in
     * process image
     */
    abstract protected ModbusRequest readRequestIn() throws SerialIOException;

    /**
     * Reads a response message from the slave responding to a master
     * writeRequest request.
     */
    abstract protected ModbusResponse readResponseIn() throws SerialIOException;


    // ================== read

    /**
     * Reads the own message echo produced in RS485 Echo Mode within the given
     * time frame.
     * <p>
     * TODO move to serial connection
     *
     * @param len is the length of the echo to read.  Timeout will occur if the
     *            echo is not received in the time specified in the
     *            SerialConnection.
     */
    final boolean readEcho(int len) throws SerialIOException {
        val echoBuf = new byte[len];
        val echoLen = commPort.readBytes(echoBuf, len);

        if (log.isDebugEnabled())
            log.debug("echo: {}", toHex(echoBuf, 0, echoLen));

        if (echoLen != len)
            log.warn("echo not received, read=" + echoLen + " expecting=" + len);

        return echoLen == len;
    }

    /**
     * Reads a byte from the com port
     *
     * @throws SerialIOException if it cannot read or times out
     */
    final int readByte() throws SerialIOException {
        val buf = new byte[1];
        int cnt = commPort.readBytes(buf, 1);

        if (cnt != 1)
            throw new SerialIOException("cannot read from serial port, read="
                                                + cnt + " expecting=1");

        return buf[0] & 0xff;
    }

    /**
     * Reads the specified number of bytes from the input stream
     *
     * @param buffer      buffer to put data into.
     * @param bytesToRead number of bytes to read.
     * @throws SerialIOException if the port is invalid or if the number of
     *                           bytes returned is not equal to that asked for.
     */
    final void readBytes(byte[] buffer, long bytesToRead) throws SerialIOException {
        int cnt = commPort.readBytes(buffer, bytesToRead);

        if (cnt != bytesToRead)
            throw new SerialIOException("cannot read from serial port, read="
                                                + cnt + " expecting=" + bytesToRead);
    }

    /**
     * Reads an ascii byte from the input stream. It handles the special start
     * and end frame markers
     *
     * @return Byte value of the next ASCII couplet
     */
    final int readAsciiByte() throws IOException {
        val b0 = readByte();
        switch (b0) {
            case ':':
                return Modbus.ASCII_FRAME_START_FLAG;
            case '\r':
            case '\n':
                return Modbus.ASCII_FRAME_END_FLAG;
            default:
                val b1 = readByte();
                val combined = (Character.digit(b0, 16) << 4) + Character.digit(b1, 16);
                if (log.isDebugEnabled())
                    log.debug(format("read two single bytes: [%c=%02X, %c=%02X] as [%d=%02X]",
                                     b0, b0, b1, b1, combined, combined));
                return combined;
        }
    }


    // ================== write

    /**
     * Writes out a byte value as an ascii character If the value is the special
     * start/end characters, then allowance is made for these
     *
     * @param value Value to write
     * @return Number of bytes written
     * @throws IOException If a problem with the port
     */
    final int writeAsciiByte(int value) throws IOException {
        byte[] buffer;

        if (value == Modbus.ASCII_FRAME_START_FLAG) {
            log.debug("FRAME_START");
            buffer = new byte[]{Modbus.ASCII_FRAME_START};
        }
        else if (value == Modbus.ASCII_FRAME_END_FLAG) {
            log.debug("FRAME_END");
            buffer = new byte[]{Modbus.ASCII_FRAME_END_0, Modbus.ASCII_FRAME_END_1};
        }
        else {
            val hex = toHex(value);
            if (log.isDebugEnabled())
                log.debug("write {}", value, hex);
            buffer = J2ModDataUtil.toHex(value);
        }

        return commPort.writeBytes(buffer, buffer.length);
    }

    /**
     * Writes an array of bytes out as a stream of ascii characters
     *
     * @param buffer       Buffer of bytes to write
     * @param bytesToWrite Number of characters to write
     * @throws IOException If a problem with the port
     */
    final void writeAsciiBytes(byte[] buffer, long bytesToWrite) throws IOException {
        for (int i = 0, cnt = 0; i < bytesToWrite; i++, cnt++)
            // if not 2 bytes are written then:
            if (writeAsciiByte(buffer[i]) != 2)
                return;
    }

    /**
     * Writes the bytes to the output stream
     *
     * @param buffer       Buffer to write
     * @param bytesToWrite Number of bytes to write
     */
    final void writeBytes(byte[] buffer, long bytesToWrite) throws SerialIOException {
        commPort.writeBytes(buffer, bytesToWrite);
    }

    /**
     * calls {@link SerialConnection#discard()} on the underlying port.
     */
    final void discard() throws SerialIOException {
        commPort.discard();
    }

    // TODO make internal

    /**
     * Injects a delay dependent on the last time we received a response or if a
     * fixed delay has been specified
     * <p>
     * 0 -> Injects a delay dependent on the baud rate
     *
     * @param transDelayMS             Fixed transaction delay (milliseconds)
     * @param lastTransactionTimestamp Timestamp of last transaction
     */
    public final void waitBetweenFrames(int transDelayMS, long lastTransactionTimestamp) {
        // If a fixed delay has been set
        if (transDelayMS > 0) {
            J2ModUtils.sleep(transDelayMS);
        }
        else {
            // Make use we have a gap of 3.5 characters between adjacent requests
            // We have to do the calculations here because it is possible that the caller may have changed
            // the connection characteristics if they provided the connection instance
            int delay = (int) (Modbus.INTER_MESSAGE_GAP * (commPort.getNumDataBits() + commPort
                    .getNumStopBits()) * 1000 / commPort
                    .getBaudRate());

            // If the delay is below the miimum, set it to the minimum
            if (delay > Modbus.MINIMUM_TRANSMIT_DELAY) {
                delay = Modbus.MINIMUM_TRANSMIT_DELAY;
            }

            // How long since the last message we received
            long gapSinceLastMessage = System.currentTimeMillis() - lastTransactionTimestamp;
            if (delay > gapSinceLastMessage) {
                J2ModUtils.sleep(delay - gapSinceLastMessage);
            }
        }
    }

}
