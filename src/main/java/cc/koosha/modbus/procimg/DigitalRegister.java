package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscribable;
import cc.koosha.modbus.util.Subscriber;


/**
 * Represents a boolean registers (coils in term of Modbus).
 * <p>
 * TODO use enum? SET, UNSET, INVALID?
 *
 * @author Koosha Hosseiny
 */
public interface DigitalRegister<T extends DigitalRegister<T>> extends Subscribable<T, ValueEvent> {

    /**
     * Tests if this register is set to true or false.
     *
     * @return true if set, false otherwise.
     */
    boolean isSet();

    /**
     * Sets the state of this digital register to high (true).
     *
     * <b>NOTE</b>: in case of Digital Input register, According to modbus
     * contract, and for complying with it, this method should only be used from master/device side. see {@link
     * DigitalIn#set()} (boolean)}.
     *
     * @see #unset()
     */
    void set();

    /**
     * Sets the state of this digital register to low (false).
     *
     * <b>NOTE</b>: in case of Digital Input register, According to modbus
     * contract, and for complying with it, this method should only be used from master/device side. see {@link
     * DigitalIn#unset()} (boolean)}.
     *
     * @see #set()
     */
    void unset();


    @Override
    int subscribe(Subscriber<T, ValueEvent> subscriber);

}
