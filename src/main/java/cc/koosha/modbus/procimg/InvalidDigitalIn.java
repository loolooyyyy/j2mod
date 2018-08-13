package cc.koosha.modbus.procimg;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Subscriber;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Attempting to access this register will result in an {@link IllegalAddressException}.
 *
 * @author Koosha Hosseiny
 */
@ThreadSafe
final class InvalidDigitalIn implements DigitalIn {

    private final static InvalidDigitalIn INSTANCE = new InvalidDigitalIn();

    public static InvalidDigitalIn getInstance() {
        return INSTANCE;
    }

    private InvalidDigitalIn() {
    }

    @Override
    public boolean isSet() {
        throw new IllegalAddressException("InvalidRegister");
    }

    @Override
    public void set() {
        throw new IllegalAddressException("InvalidRegister");
    }

    @Override
    public void unset() {
        throw new IllegalAddressException("InvalidRegister");
    }

    @Override
    public int subscribe(Subscriber<DigitalIn, ValueEvent> subscriber) {
        throw new IllegalAddressException("InvalidRegister");
    }

    @Override
    public boolean unsubscribe(int id) {
        throw new IllegalAddressException("InvalidRegister");
    }


    @Override
    public String toString() {
        return "DigitalIn{invalid}";
    }

}
