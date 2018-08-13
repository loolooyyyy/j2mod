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
package cc.koosha.modbus.util;

import lombok.Cleanup;
import lombok.val;

import javax.annotation.concurrent.ThreadSafe;
import java.util.BitSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * A wrapper around {@link BitSet}, which enforces a size.
 *
 * TODO deprecated make internal
 *
 * @author Koosha Hosseiny.
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@ThreadSafe
public final class BitVector {

    public static BitVector valueOf(int size) {
        return new BitVector(size);
    }

    public static BitVector valueOf(byte[] data, int size) {
        return new BitVector(data, size);
    }

    public static BitVector valueOf(byte[] data) {
        return new BitVector(data);
    }

    // =============================================

    private static final byte[] ZERO = new byte[0];

    private int size;
    private final BitSet bitSet;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private void setBytes0(byte[] data) {
        for (int i = 0; i < data.length; i++)
            for (int j = 0, bitSetIndex = i * 8 + j, currBitIndex = 1 << j; j < 8; j++)
                if ((data[i] & currBitIndex) != 0)
                    bitSet.set(bitSetIndex);
                else
                    bitSet.clear(bitSetIndex);
    }

    private byte[] getBytes0() {
        val data = new byte[size];
        for (int i = 0; i < data.length; i++)
            for (int j = 0, bitSetIndex = i * 8 + j, currBitIndex = 1 << j; j < 8; j++)
                data[i] = (byte) (data[i] & ~(
                        ((bitSet.get(bitSetIndex)) ? 0 : 1) << currBitIndex
                ));
        return data;
    }


    private BitVector(int size) {
        this(ZERO, size);
    }

    private BitVector(byte[] data) {
        this(data, data.length);
    }

    private BitVector(byte[] data, int size) {
        this.size = size;
        this.bitSet = new BitSet(size);
        setBytes0(data);
    }


    public byte[] getBytes() {
        @Cleanup("unlock")
        val lock = LOCK.readLock();
        lock.lock();
        return getBytes0();
    }

    public void setBytes(byte[] data) {
        @Cleanup("unlock")
        val lock = LOCK.readLock();
        lock.lock();
        setBytes0(data);
    }

    public boolean getBit(int index) throws IndexOutOfBoundsException {
        @Cleanup("unlock")
        val lock = LOCK.readLock();
        lock.lock();
        if (index > size)
            throw new IndexOutOfBoundsException("" + index);
        return bitSet.get(index);
    }

    public void setBit(int index, boolean b) throws IndexOutOfBoundsException {
        @Cleanup("unlock")
        val lock = LOCK.writeLock();
        lock.lock();
        if (index > size)
            throw new IndexOutOfBoundsException("" + index);
        bitSet.set(index, b);
    }

    public int size() {
        @Cleanup("unlock")
        val lock = LOCK.readLock();
        lock.lock();
        return size;
    }

    public void trim(int trim) throws IndexOutOfBoundsException {
        @Cleanup("unlock")
        val lock = LOCK.writeLock();
        lock.lock();
        if (trim > this.size)
            throw new IndexOutOfBoundsException("" + trim);
        this.size = trim;
    }

    public int byteSize() {
        @Cleanup("unlock")
        val lock = LOCK.readLock();
        lock.lock();
        return this.size * 8;
    }

    /**
     * <b>IMPORTANT</b>: not thread-safe.
     */
    public String toString() {
        return "BitVector<" + bitSet.toString() + ">";
    }

}