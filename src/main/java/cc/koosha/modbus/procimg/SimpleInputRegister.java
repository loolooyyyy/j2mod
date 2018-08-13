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

import cc.koosha.modbus.xinternal.J2ModDataUtil;
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import lombok.EqualsAndHashCode;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Class implementing a simple <tt>InputRegister</tt>.
 * <p>
 * The <tt>setValue()</tt> method is synchronized, which ensures atomic access, * but no specific access order.
 * <p>
 * <b>Important</b> do NOT extend this class, create your own implementation.
 *
 * @author Koosha Hosseiny.
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@EqualsAndHashCode
@NotThreadSafe
class SimpleInputRegister implements InputRegister {

    /**
     * The word (<tt>byte[2]</tt>) holding the register content.
     */
    final byte[] register = new byte[2];


    /**
     * Constructs a new <tt>SimpleRegister</tt> instance.
     *
     * @param hi the first (hi) byte of the word.
     * @param lo the second (low) byte of the word.
     */
    SimpleInputRegister(byte hi, byte lo) {
        register[0] = hi;
        register[1] = lo;
    }

    /**
     * Constructs a new <tt>SimpleRegister</tt> instance with the given value.
     *
     * @param value the value of this <tt>SimpleRegister</tt> as <tt>int</tt>.
     */
    SimpleInputRegister(int value) {
        J2ModPrecondition.ensureFitsInShort(value, "input register value");
        register[0] = J2ModDataUtil.hiByte(value);
        register[1] = J2ModDataUtil.loByte(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final int getValue() {
        return J2ModDataUtil.twoBytesToInt(register, 0);
    }

    @Override
    public byte[] getBytes() {
        return new byte[]{register[0], register[1]};
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final int toUnsignedShort() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final short toShort() {
        return J2ModDataUtil.twoBytesToShort(register, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final byte[] toBytes() {
        return getBytes();
    }


    public String toString() {
        return "InputRegister{hi=" + register[0] + ", lo=" + register[1] + "}";
    }

}
