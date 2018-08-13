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
import cc.koosha.modbus.msg.response.ReadCoilsResponse;
import cc.koosha.modbus.procimg.DigitalOut;
import cc.koosha.modbus.procimg.ProcessImage;
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;


/**
 * Class implementing a <tt>ReadCoilsRequest</tt>. The implementation directly
 * correlates with the class 1 function
 * <i>read coils (FC 1)</i>. It encapsulates the corresponding request message.
 *
 * <p>
 * Coils are understood as bits that can be manipulated (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @author jfhaugh
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public final class ReadCoilsRequest extends AbstractModbusRequest {

    /**
     * The reference of the register to to start reading from with this
     * <tt>ReadCoilsRequest</tt>.
     */
    @Getter
    @Setter
    private int reference;

    /**
     * Returns the number of bits (i.e. coils) to be read with this
     * <tt>ReadCoilsRequest</tt>.
     */
    @Getter
    private int bitCount;

    private ReadCoilsRequest(int funcode, int len, int ref, int count) {
        setFunctionCode(Modbus.READ_COILS);
        setDataLength(4);
        setReference(ref);
        setBitCount(count);
    }

    public ReadCoilsRequest() {
        this(Modbus.READ_COILS, 4, 0, 0);
    }

    /**
     * Constructs a new <tt>ReadCoilsRequest</tt> instance with a given
     * reference and count of coils (i.e. bits) to be read.
     * <p>
     *
     * @param ref   the reference number of the register to read from.
     * @param count the number of bits to be read.
     */
    public ReadCoilsRequest(int unitId, int ref, int count) {
        this(Modbus.READ_COILS, 4, ref, count);
        super.setUnitID(unitId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusResponse getResponse() {
        return updateResponseWithHeader(new ReadCoilsResponse(bitCount));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusResponse createResponse(ProcessImage pi) {
        ModbusResponse response;
        List<DigitalOut> douts;

        // 1. get process image

        // 2. get input discretes range
        try {
            douts = pi.getDigitalOutRange(getReference(), getBitCount());
        }
        catch (IllegalAddressException e) {
            return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
        }
        response = getResponse();

        // Populate the discrete values from the process image.
        for (int i = 0; i < douts.size(); i++) {
            ((ReadCoilsResponse) response).setCoilStatus(i, douts.get(i)
                                                                 .isSet());
        }

        return response;
    }

    /**
     * Sets the number of bits (i.e. coils) to be read with this
     * <tt>ReadCoilsRequest</tt>.
     * <p>
     *
     * @param count the number of bits to be read.
     */
    public void setBitCount(int count) {
        J2ModPrecondition.ensureIsInRange(count, 0, Modbus.MAX_BITS, "Maximum bit-count exceeded");
        bitCount = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(DataOutput dout) throws IOException {
        dout.write(getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData(DataInput din) throws IOException {
        reference = din.readUnsignedShort();
        bitCount = din.readUnsignedShort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getMessage() {
        byte result[] = new byte[4];
        result[0] = (byte) ((reference >> 8) & 0xff);
        result[1] = (byte) ((reference & 0xff));
        result[2] = (byte) ((bitCount >> 8) & 0xff);
        result[3] = (byte) ((bitCount & 0xff));
        return result;
    }

}
