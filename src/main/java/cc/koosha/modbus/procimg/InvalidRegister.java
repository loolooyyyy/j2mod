package cc.koosha.modbus.procimg;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Subscriber;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Attempting to access this register will result in an
 * {@link IllegalAddressException}.
 * <p>
 * It may be used to create "holes" in a Modbus register map.
 *
 * @author Koosha Hosseiny
 */
@ThreadSafe
final class InvalidRegister implements Register {

    private static final InvalidRegister INSTANCE = new InvalidRegister();

    static InvalidRegister getInstance() {
        return INSTANCE;
    }


    @Override
    public void setValue(int v) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public void setValue(short v) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public void setValue(byte[] v) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public void setValue(byte hi, byte lo) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public void setHiByte(int v) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public void setLoByte(int v) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public int subscribe(Subscriber<Register, ValueEvent> subscriber) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public boolean unsubscribe(int id) {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public int getValue() {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public byte[] getBytes() {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public int toUnsignedShort() {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public short toShort() {
        throw new IllegalAddressException("invalid register");
    }

    @Override
    public byte[] toBytes() {
        throw new IllegalAddressException("invalid register");
    }


    @Override
    public String toString() {
        return "Register{invalid}";
    }

}
