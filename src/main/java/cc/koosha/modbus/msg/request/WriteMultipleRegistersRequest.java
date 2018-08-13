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
package cc.koosha.modbus.msg.request;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.response.WriteMultipleRegistersResponse;
import cc.koosha.modbus.procimg.ProcessImage;
import cc.koosha.modbus.procimg.Register;
import cc.koosha.modbus.procimg.SimpleRegister;
import cc.koosha.modbus.xinternal.J2ModCollections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;


/**
 * Class implementing a <tt>WriteMultipleRegistersRequest</tt>. The implementation directly correlates with the class 0
 * function <i>write multiple registers (FC 16)</i>. It encapsulates the corresponding request message.
 *
 * @author Dieter Wimberger
 * @author jfhaugh
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public final class WriteMultipleRegistersRequest extends AbstractModbusRequest {

    private int reference;
    private List<Register> registers;

    /**
     * Constructs a new <tt>WriteMultipleRegistersRequest</tt> instance with a given starting reference and values to be
     * written.
     * <p>
     *
     * @param first     -- the address of the first register to write to.
     * @param registers -- the registers to be written.
     */
    public WriteMultipleRegistersRequest(int unitId, int first, List<Register> registers) {
        setFunctionCode(Modbus.WRITE_MULTIPLE_REGISTERS);

        setReference(first);
        setRegisters(registers);
    }

    /**
     * Constructs a new <tt>WriteMultipleRegistersRequest</tt> instance.
     */
    public WriteMultipleRegistersRequest() {
        setFunctionCode(Modbus.WRITE_MULTIPLE_REGISTERS);
    }

    @Override
    public ModbusResponse getResponse() {
        return updateResponseWithHeader(new WriteMultipleRegistersResponse());
    }

    /**
     * createResponse - Returns the <tt>WriteMultipleRegistersResponse</tt> that represents the answer to this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     * The implementation should take care about assembling the reply to this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     * This method is used to create responses from the process image associated with the listener. It is commonly used
     * to implement Modbus slave instances.
     *
     * @return the corresponding ModbusResponse.
     * <p>
     * <p>
     * createResponse() must be able to handle the case where the word data that is in the response is actually non-word
     * data. That is, where the slave device has data which are not actually
     * <tt>short</tt> values in the range of registers being processed.
     */
    @Override
    public ModbusResponse createResponse(ProcessImage pi) {
        WriteMultipleRegistersResponse response;

        List<Register> regs;
        // 1. get process image
        ProcessImage procimg = pi;
        // 2. get registers
        try {
            regs = procimg.getRegisterRange(getReference(), getWordCount());
            // 3. set Register values
            for (int i = 0; i < regs.size(); i++) {
                regs.get(i).setValue(this.getRegister(i).getValue());
            }
        }
        catch (IllegalAddressException iaex) {
            return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
        }
        response = (WriteMultipleRegistersResponse) getResponse();
        response.setReference(getReference());
        response.setWordCount(getWordCount());

        return response;
    }

    /**
     * setReference - Returns the reference of the register to start writing to with this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     *
     * @return the reference of the register to start writing to as <tt>int</tt> .
     */
    public int getReference() {
        return reference;
    }

    /**
     * setReference - Sets the reference of the register to write to with this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     *
     * @param ref the reference of the register to start writing to as an
     *            <tt>int</tt>.
     */
    public void setReference(int ref) {
        reference = ref;
    }

    /**
     * getRegisters - Returns the registers to be written with this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     *
     * @return the registers to be written as <tt>Register[]</tt>.
     */
    public synchronized List<Register> getRegisters() {
        return J2ModCollections.modifiableCopy(registers);
    }

    /**
     * setRegisters - Sets the registers to be written with this
     * <tt>WriteMultipleRegistersRequest</tt>.
     * <p>
     *
     * @param registers the registers to be written as <tt>Register[]</tt>.
     */
    public void setRegisters(List<Register> registers) {
        this.registers = registers == null ? null : J2ModCollections.modifiableCopy(registers);
    }

    /**
     * getRegister - Returns the <tt>Register</tt> at the given position.
     *
     * @param index the relative index of the <tt>Register</tt>.
     * @return the register as <tt>Register</tt>.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public Register getRegister(int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index + " < 0");
        }

        if (index >= getWordCount()) {
            throw new IndexOutOfBoundsException(index + " > " + getWordCount());
        }

        return registers.get(index);
    }

    /**
     * getRegisterValue - Returns the value of the specified register.
     * <p>
     *
     * @param index the index of the desired register.
     * @return the value as an <tt>int</tt>.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public int getRegisterValue(int index) throws IndexOutOfBoundsException {
        return getRegister(index).toUnsignedShort();
    }

    /**
     * getByteCount - Returns the number of bytes representing the values to be written.
     * <p>
     *
     * @return the number of bytes to be written as <tt>int</tt>.
     */
    public int getByteCount() {
        return getWordCount() * 2;
    }

    /**
     * getWordCount - Returns the number of words to be written.
     *
     * @return the number of words to be written as <tt>int</tt>.
     */
    public int getWordCount() {
        if (registers == null) {
            return 0;
        }

        return registers.size();
    }

    public void writeData(DataOutput output) throws IOException {
        output.write(getMessage());
    }

    public void readData(DataInput input) throws IOException {
        reference = input.readUnsignedShort();
        int registerCount = input.readUnsignedShort();
        int byteCount = input.readUnsignedByte();

        byte buffer[] = new byte[byteCount];
        input.readFully(buffer, 0, byteCount);

        int offset = 0;
        registers = J2ModCollections.newModifiableList();

        for (int register = 0; register < registerCount; register++) {
            registers.set(register, new SimpleRegister(buffer[offset], buffer[offset + 1]));
            offset += 2;
        }
    }

    public byte[] getMessage() {
        int len = 5;

        if (registers != null) {
            len += registers.size() * 2;
        }

        byte result[] = new byte[len];
        int registerCount = registers != null ? registers.size() : 0;

        result[0] = (byte) ((reference >> 8) & 0xff);
        result[1] = (byte) (reference & 0xff);
        result[2] = (byte) ((registerCount >> 8) & 0xff);
        result[3] = (byte) (registerCount & 0xff);
        result[4] = (byte) (registerCount * 2);

        int offset = 5;

        for (int i = 0; i < registerCount; i++) {
            byte bytes[] = registers.get(i).toBytes();
            result[offset++] = bytes[0];
            result[offset++] = bytes[1];
        }
        return result;
    }
}
