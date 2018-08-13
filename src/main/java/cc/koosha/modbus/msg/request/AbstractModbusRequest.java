package cc.koosha.modbus.msg.request;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.response.ExceptionResponse;
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * Abstract class implementing a <tt>ModbusRequest</tt>. This class provides
 * specialised implementations with the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @author jfhaugh (jfh@ghgande.com)
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
abstract class AbstractModbusRequest implements ModbusRequest {

    // instance attributes
    private int transactionID = Modbus.DEFAULT_TRANSACTION_ID;

    /**
     * Sets the protocol identifier of this <tt>ModbusMessage</tt>.
     * <p>
     * The identifier should be a 2-byte (short) non negative integer value
     * valid in the range of 0-65535.<br>
     */
    @Getter
    @Setter
    private int protocolID = Modbus.DEFAULT_PROTOCOL_ID;

    @Getter
    private int dataLength;

    /**
     * Sets the unit identifier of this <tt>ModbusMessage</tt>.<br> The
     * identifier should be a 1-byte non negative integer value valid in the
     * range of 0-255.
     */
    @Getter
    @Setter
    private int unitID = Modbus.DEFAULT_UNIT_ID;

    /**
     * Sets the function code of this <tt>ModbusMessage</tt>.<br> The function
     * code should be a 1-byte non negative integer value valid in the range of
     * 0-127.<br> Function codes are ordered in conformance classes their values
     * are specified in <tt>cc.koosha.modbus.Modbus</tt>.
     * <p>
     * <p>
     * TODO 9 - Architecture: It is possible to deprecate it in favour of
     * constructor.
     *
     * @see Modbus
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int functionCode;

    /**
     * Sets the headless flag of this message (serial).
     */
    @Setter
    @Getter
    private boolean headless = false;

    @Override
    public int getTransactionID() {
        return transactionID & 0x0000FFFF;
    }

    /**
     * Sets the transaction identifier of this <tt>ModbusMessage</tt>.
     *
     * <p>
     * The identifier must be a 2-byte (short) non negative integer value valid
     * in the range of 0-65535.<br>
     *
     * @param tid the transaction identifier as <tt>int</tt>.
     */
    @Override
    public void setTransactionID(int tid) {
        transactionID = tid & 0x0000FFFF;
    }

    /**
     * Sets the length of the data appended after the protocol header.
     *
     * <p>
     * Note that this library, a bit in contrast to the specification, counts
     * the unit identifier and the function code in the header, because it is
     * part of each and every message. Thus this method will add two (2) to the
     * passed in integer value.
     *
     * <p>
     * This method does not include the length of a final CRC/LRC for those
     * protocols which requirement.
     *
     * @param length the data length as <tt>int</tt>.
     */
    public void setDataLength(int length) {
        J2ModPrecondition.ensureIsInRange(length, 0, 253, "invalid length");
        if (length < 0 || length + 2 > 255) {
            throw new IllegalArgumentException("Invalid length: " + length);
        }
        dataLength = length + 2;
    }

    @Override
    public int getOutputLength() {
        int l = 2 + getDataLength();
        if (!isHeadless()) {
            l = l + 4;
        }
        return l;
    }

    // TODO 1 - ?: make this method final. it calls another method, overwriting this method will be confusing ->
    // classes need to overwrite this method, must begin with a fresh implementation OR we could create
    // a common base class,
    @Override
    public void writeTo(DataOutput dout) throws IOException {
        if (!isHeadless()) {
            dout.writeShort(getTransactionID());
            dout.writeShort(getProtocolID());
            dout.writeShort(getDataLength());
        }
        dout.writeByte(getUnitID());
        dout.writeByte(getFunctionCode());

        writeData(dout);
    }

    // TODO 1 - ?: make this method final. it calls another method, overwriting this method will be confusing ->
    // classes need to overwrite this method, must begin with a fresh implementation OR we could create
    // a common base class,
    @Override
    public void readFrom(DataInput din) throws IOException {
        if (!isHeadless()) {
            setTransactionID(din.readUnsignedShort());
            setProtocolID(din.readUnsignedShort());
            dataLength = din.readUnsignedShort();
        }
        setUnitID(din.readUnsignedByte());
        setFunctionCode(din.readUnsignedByte());
        readData(din);
    }

    /**
     * Writes the subclass specific data to the given DataOutput.
     *
     * @param dout the DataOutput to be written to.
     * @throws IOException if an I/O related error occurs.
     */
    public abstract void writeData(DataOutput dout) throws IOException;

    /**
     * Reads the subclass specific data from the given DataInput instance.
     *
     * @param din the DataInput to read from.
     * @throws IOException if an I/O related error occurs.
     */
    public abstract void readData(DataInput din) throws IOException;

    /**
     * Returns the <tt>ModbusResponse</tt> that correlates with this
     * <tt>ModbusRequest</tt>.
     *
     * <p>
     * The response must include the unit number, function code, as well as any
     * transport-specific header information.
     *
     * <p>
     * This method is used to create an empty response which must be populated
     * by the caller. It is commonly used to un-marshal responses from Modbus
     * slaves.
     *
     * @return the corresponding <tt>ModbusResponse</tt>.
     */
    public abstract ModbusResponse getResponse();

    /**
     * Factory method for creating exception responses with the given exception
     * code.
     *
     * @param code the code of the exception.
     * @return a ModbusResponse instance representing the exception response.
     */
    @Override
    public ModbusResponse createExceptionResponse(int code) {
        return updateResponseWithHeader(new ExceptionResponse(getFunctionCode(), code), true);
    }

    /**
     * Updates the response with the header information to match the request
     * <p>
     * TODO 0 - Readability: see {@link #updateResponseWithHeader(ModbusResponse,
     * boolean)}.
     *
     * @param response Response to update
     * @return Updated response
     */
    ModbusResponse updateResponseWithHeader(ModbusResponse response) {
        return updateResponseWithHeader(response, false);
    }

    ModbusResponse updateResponseWithHeaderIgnoreFuncode(ModbusResponse response) {
        return updateResponseWithHeader(response, true);
    }

    /**
     * Updates the response with the header information to match the request
     *
     * @param response           Response to update
     * @param ignoreFunctionCode True if the function code should stay
     *                           unmolested
     * @return Updated response
     */
    private ModbusResponse updateResponseWithHeader(ModbusResponse response,
                                                    boolean ignoreFunctionCode) {
        // transfer header data
        this.setHeadless(isHeadless());
        if (!isHeadless()) {
            response.setTransactionID(getTransactionID());
            response.setProtocolID(getProtocolID());
        }
        else {
            this.setHeadless(true);
        }
        response.setUnitID(getUnitID());
        if (!ignoreFunctionCode) {
            response.setFunctionCode(getFunctionCode());
        }
        return response;
    }
}
