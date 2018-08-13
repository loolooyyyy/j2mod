package cc.koosha.modbus.procimg;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Subscriber;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Attempting to access this register will result in an
 * {@link IllegalAddressException}.
 *
 * @author Koosha Hosseiny
 */
@ThreadSafe
final class InvalidDigitalOut implements DigitalIn {

    private final static InvalidDigitalOut INSTANCE = new InvalidDigitalOut();

    public static InvalidDigitalOut getInstance() {
        return INSTANCE;
    }

    private InvalidDigitalOut() {
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
        return "DigitalOut{invalid}";
    }

}

