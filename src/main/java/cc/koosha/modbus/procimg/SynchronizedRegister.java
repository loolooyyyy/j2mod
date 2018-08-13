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
package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;


/**
 * A wrapper for making a thread safe instance of {@link Register}.
 *
 * <p>
 * TODO is it worth using {@link java.util.concurrent.locks.ReadWriteLock} and ->
 * also, access register values directly for {@link SimpleRegister}?
 *
 * @author Koosha Hosseiny
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ThreadSafe
final class SynchronizedRegister implements Register {

    private final Register wrapped;


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void setValue(int v) {
        wrapped.setValue(v);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public void setValue(short s) {
        wrapped.setValue(s);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public void setValue(byte[] bytes) {
        wrapped.setValue(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void setValue(byte hi, byte lo) {
        wrapped.setValue(hi, lo);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void setHiByte(int v) {
        wrapped.setHiByte(v);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void setLoByte(int v) {
        wrapped.setLoByte(v);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int getValue() {
        return wrapped.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Synchronized
    @Override
    @Deprecated
    public int toUnsignedShort() {
        return wrapped.toUnsignedShort();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public short toShort() {
        return wrapped.toShort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    @Deprecated
    public byte[] toBytes() {
        return this.getBytes();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public byte[] getBytes() {
        return wrapped.getBytes();
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<Register, ValueEvent> subscriber) {
        return this.wrapped.subscribe(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void unsubscribe(int id) {
        this.wrapped.unsubscribe(id);
    }


    /**
     * ATTENTION: No synchronization done.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("synchronized<");
        try {
            sb.append(wrapped.toString());
        }
        catch (Exception e) {
            sb.append("Register{?}");
        }
        return sb.append('>').toString();
    }

}
