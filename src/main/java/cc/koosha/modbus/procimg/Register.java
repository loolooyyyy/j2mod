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

import cc.koosha.modbus.util.Subscribable;
import cc.koosha.modbus.util.Subscriber;


/**
 * Interface defining a register.
 *
 * <p>
 * A register is read-write from slave and master or device side. Therefore
 * implementations have to be carefully designed for concurrency.
 * <p>
 * TODO 1 - Enhancement: add setHighByte/setLowByte methods
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface Register extends InputRegister, Subscribable<Register, ValueEvent> {

    /**
     * Sets the content of this <tt>Register</tt> from the given unsigned 16-bit
     * value (unsigned short).
     *
     * @param v the value as unsigned short (<tt>int</tt>).
     */
    void setValue(int v);

    /**
     * Sets the content of this register from the given signed 16-bit value
     * (short).
     *
     * @param v the value as <tt>short</tt>.
     * @deprecated use {@link #setValue(int)}
     */
    @Deprecated
    void setValue(short v);

    /**
     * Sets the content of this register from the given raw bytes.
     *
     * @param v the raw data as <tt>byte[]</tt>.
     * @deprecated use {@link #setValue(byte, byte)}
     */
    @Deprecated
    void setValue(byte[] v);

    /**
     * Sets the content of this register from the given raw bytes.
     *
     * @param hi high byte.
     * @param lo low byte.
     */
    void setValue(byte hi, byte lo);

    /**
     * Sets the content of this <tt>Register</tt> from the given unsigned 16-bit
     * value (unsigned short).
     *
     * @param v the value as unsigned short (<tt>int</tt>).
     */
    void setHiByte(int v);

    /**
     * Sets the content of this <tt>Register</tt> from the given unsigned 16-bit
     * value (unsigned short).
     *
     * @param v the value as unsigned short (<tt>int</tt>).
     */
    void setLoByte(int v);

}
