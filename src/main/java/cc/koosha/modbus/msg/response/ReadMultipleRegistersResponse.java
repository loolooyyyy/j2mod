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
package cc.koosha.modbus.msg.response;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.xinternal.J2ModCollections;
import cc.koosha.modbus.procimg.Register;
import cc.koosha.modbus.procimg.SimpleRegister;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;


/**
 * Class implementing a <tt>ReadMultipleRegistersResponse</tt>. The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 3)</i>. It encapsulates the corresponding response message.
 *
 * @author Dieter Wimberger
 * @author Julie (jfh@ghgande.com)
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public final class ReadMultipleRegistersResponse extends AbstractModbusResponse {

    // instance attributes
    private int byteCount;
    private List<Register> registers;

    /**
     * Constructs a new <tt>ReadMultipleRegistersResponse</tt> instance.
     */
    public ReadMultipleRegistersResponse() {
        super();
        setFunctionCode(Modbus.READ_MULTIPLE_REGISTERS);
    }

    /**
     * Constructs a new <tt>ReadInputRegistersResponse</tt> instance.
     *
     * @param registers the Register[] holding response registers.
     */
    public ReadMultipleRegistersResponse(List<Register> registers) {
        super();

        setFunctionCode(Modbus.READ_MULTIPLE_REGISTERS);
        setDataLength(registers == null ? 0 : (registers.size() * 2 + 1));

        this.registers = registers == null ? null : J2ModCollections.modifiableCopy(registers);
        byteCount = registers == null ? 0 : (registers.size() * 2);
    }

    /**
     * Returns the number of bytes that have been read.
     *
     * @return the number of bytes that have been read as <tt>int</tt>.
     */
    public int getByteCount() {
        return byteCount;
    }

    /**
     * Returns the number of words that have been read. The returned value should be half of the the byte count of this
     * <tt>ReadMultipleRegistersResponse</tt>.
     *
     * @return the number of words that have been read as <tt>int</tt>.
     */
    public int getWordCount() {
        return byteCount / 2;
    }

    /**
     * Returns the <tt>Register</tt> at the given position (relative to the reference used in the request).
     *
     * @param index the relative index of the <tt>Register</tt>.
     * @return the register as <tt>Register</tt>.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public Register getRegister(int index) {
        if (registers == null) {
            throw new IndexOutOfBoundsException("No registers defined!");
        }

        if (index < 0) {
            throw new IndexOutOfBoundsException("Negative index: " + index);
        }

        if (index >= getWordCount()) {
            throw new IndexOutOfBoundsException(index + " > " + getWordCount());
        }

        return registers.get(index);
    }

    /**
     * Returns the value of the register at the given position (relative to the reference used in the request)
     * interpreted as unsigned short.
     *
     * @param index the relative index of the register for which the value should be retrieved.
     * @return the value as <tt>int</tt>.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public int getRegisterValue(int index) throws IndexOutOfBoundsException {
        return getRegister(index).toUnsignedShort();
    }

    /**
     * Returns the reference to the array of registers read.
     *
     * @return a <tt>Register[]</tt> instance.
     */
    public synchronized List<Register> getRegisters() {
        // TODO needs copy?
        return J2ModCollections.copy(registers);
    }

    /**
     * Sets the entire block of registers for this response
     *
     * @param registers Array of registers to use
     */
    public void setRegisters(List<Register> registers) {
        byteCount = registers == null ? 0 : registers.size() * 2;
        this.registers = registers == null ? null : J2ModCollections.modifiableCopy(registers);
        setDataLength(byteCount + 1);
    }

    public void writeData(DataOutput dout) throws IOException {
        dout.writeByte(byteCount);

        for (int k = 0; k < getWordCount(); k++) {
            dout.write(registers.get(k).toBytes());
        }
    }

    public void readData(DataInput din) throws IOException {
        byteCount = din.readUnsignedByte();

        registers = J2ModCollections.newModifiableList();

        for (int k = 0; k < getWordCount(); k++) {
            registers.set(k, new SimpleRegister(din.readByte(), din.readByte()));
        }

        setDataLength(byteCount + 1);
    }

    public byte[] getMessage() {
        byte result[];

        result = new byte[getWordCount() * 2 + 1];

        int offset = 0;
        result[offset++] = (byte) byteCount;

        for (Register register : registers) {
            byte[] data = register.toBytes();

            result[offset++] = data[0];
            result[offset++] = data[1];
        }
        return result;
    }
}