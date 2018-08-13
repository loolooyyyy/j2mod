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
package cc.koosha.modbus.io.serial;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.xinternal.J2ModDebugUtils;
import cc.koosha.modbus.xinternal.J2ModUtils;
import com.fazecast.jSerialComm.SerialPort;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.System.nanoTime;


// TODO ensure open and mark and flag, as specified by java.io.Closeable

/**
 * Class that implements a serial connection which can be used for master and
 * slave implementations.
 *
 * @author Dieter Wimberger
 * @author John Charlton
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
@NotThreadSafe
public final class SerialConnectionFazecast implements SerialConnection {

    @Getter
    private final SerialConnectionManagerFazecast manager;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private SerialPort serialPort;

    private SerialPort ensurePort() {
        if (serialPort == null)
            throw new IllegalStateException("serial connection not available");
        if (!serialPort.isOpen())
            throw new IllegalStateException("serial connection is not open");
        return serialPort;
    }


    public SerialConnectionFazecast(@NonNull FazecastSerialKonf konf) {
        manager = new SerialConnectionManagerFazecast();
        manager.setKonf(konf);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int readBytes(byte[] buffer, long bytesToRead) {
        return ensurePort().readBytes(buffer, bytesToRead);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int writeBytes(byte[] buffer, long bytesToWrite) {
        return ensurePort().writeBytes(buffer, bytesToWrite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int bytesAvailable() {
        return ensurePort().bytesAvailable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void discard() {
        val available = bytesAvailable();
        if (available > 0) {
            val buf = new byte[available];
            readBytes(buf, available);
            if (log.isDebugEnabled())
                log.debug("discarded: {}", J2ModDebugUtils.toHex(buf, 0, available));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        if (serialPort == null)
            throw new IllegalStateException("serial connection is not valid");
        return getManager().getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream() {
        if (serialPort == null)
            throw new IllegalStateException("serial connection is not valid");
        return getManager().getOutputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitBetweenFrames(int transDelayMS, long lastTransactionTimestamp) {
        // If a fixed delay has been set
        if (transDelayMS > 0) {
            J2ModUtils.sleep(transDelayMS);
            return;
        }

        // Make use we have a gap of 3.5 characters between adjacent requests
        // We have to do the calculations here because it is possible that the
        // caller may have changed the connection characteristics if they
        // provided the connection instance.
        val k = this.getManager().getKonf();
        int delay = (int) (Modbus.INTER_MESSAGE_GAP *
                (k.getDataBits() + k.getStopBits()) * 1000 / k.getBaudRate());

        // If the delay is below the minimum, set it to the minimum.
        if (delay > Modbus.MINIMUM_TRANSMIT_DELAY)
            delay = Modbus.MINIMUM_TRANSMIT_DELAY;

        // How long since the last message we received.
        val gapSinceLastMessage = System.currentTimeMillis() - lastTransactionTimestamp;
        if (delay > gapSinceLastMessage)
            J2ModUtils.sleep(delay - gapSinceLastMessage);
    }

    /**
     * Wait here for the message to have been sent
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void waitBetweenFrames(int len) {
        long startTime = nanoTime();

        val k = manager.getKonf();
        val db = k.getDataBits() == 0 ? 8 : k.getDataBits();
        val sb = k.getStopBits() == 0 ? 8 : k.getStopBits();
        val kb = k.toGeneralSerialKonf()
                  .getParity()
                  .equals(GeneralSerialKonf.PARITY_NONE) ? 0 : 1;
        val sum = db + sb + kb;
        // TODO ? compare with origin
        double bytesPerSec = k.getBaudRate() / (sum == 0 ? 1 : sum);
        double delay = 1000000000.0 * len / bytesPerSec;
        double delayMilliSeconds = Math.floor(delay / 1000000);
        double delayNanoSeconds = delay % 1000000;

        if (delayMilliSeconds == 0.0) {
            // For delays less than a millisecond, we need to chew CPU cycles unfortunately
            // There are some fiddle factors here to allow for some oddities in the hardware
            int priority = Thread.currentThread().getPriority();
            try {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            }
            catch (Exception e) {
                log.debug("could not set thread priority");
            }
            long end = startTime + ((int) (delayNanoSeconds * 1.3));
            while (nanoTime() < end) {
                // noop
            }
            try {
                Thread.currentThread().setPriority(priority);
            }
            catch (Exception e) {
                log.debug("could not restore/set thread priority");
            }
        }
        else {
            try {
                Thread.sleep((int) (delayMilliSeconds * 1.4), (int) delayNanoSeconds);
            }
            catch (Exception e) {
                log.debug("could not make delay", e);
            }
        }
    }

}
