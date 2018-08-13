package cc.koosha.modbus.msg.response;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

// import static cc.koosha.modbus.msg.ModbusResponse.AuxiliaryMessageTypes.NONE;


/**
 * Abstract class implementing a <tt>ModbusResponse</tt>. This class provides
 * specialised implementations with the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @author Julie Haugh
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
abstract class AbstractModbusResponse implements ModbusResponse {

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
    @Setter
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
    @Override
    public void setDataLength(int length) {
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


    // TODO 9 - Architecture: try to deprecate, it behaves as a boolean flag only.
    /**
     * The auxiliary type of this response message Useful for adding extra
     * information to the message that can be used by downstream processing
     * <p>
     * TODO 9 - Architecture: try to deprecate, it behaves as a boolean flag
     * only.
     *
     * @return Auxiliary type
     */
    // private AuxiliaryMessageTypes auxiliaryType = NONE;

    /*
     * Utility method to set the raw data of the message. Should not be used
     * except under rare circumstances.
     * <p>
     * <p>
     * TODOx 0 - Deprecation: move to a static method in a utility class.
     *
     * @param msg the <tt>byte[]</tt> resembling the raw modbus response
     *            message.
    protected void setMessage(byte[] msg) {
    try {
    readData(new DataInputStream(new ByteArrayInputStream(msg)));
    }
    catch (IOException ex) {
    logger.error("Problem setting response message - {}", ex.getMessage());
    }
    } */

}
