package cc.koosha.modbus.procimg;

import cc.koosha.modbus.xinternal.J2ModDataUtil;
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import cc.koosha.modbus.util.Subscriber;
import cc.koosha.modbus.xinternal.SubscriptionManager;
import cc.koosha.modbus.xinternal.J2ModThreadSafeSubscriptionManager;

import javax.annotation.concurrent.NotThreadSafe;


/**
 * Acts as java 8's interface with default implementations.
 * <p>
 * not so important notice, in case you get confused about what this class actually is:
 * This class does extend {@link SimpleInputRegister} but is actually a {@link Register}.
 * The reason is, {@link Register} implementations must be a {@link InputRegister} too,
 * but multiple inheritance is not an option, an extending this class, the can not inherit
 * from {@link SimpleInputRegister} anymore.
 *
 * <b>IMPORTANT</b> these operations are <b>NOT</b> atomic, they are 2 subsequent
 * operations: one get, and one set, and bad things may happen in between.
 */
@NotThreadSafe
final class SimpleRegister extends SimpleInputRegister implements Register {

    private final SubscriptionManager<Register, ValueEvent> subscriptionManager =
            new J2ModThreadSafeSubscriptionManager<Register, ValueEvent>(this);


    SimpleRegister(byte initialHiByte, byte initialLoByte) {
        super(initialHiByte, initialLoByte);
    }

    SimpleRegister(int initialValue) {
        super(initialValue);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(byte hi, byte lo) {
        subscriptionManager.publish(ValueEvent.BEFORE_SET);
        register[0] = hi;
        register[1] = lo;
        subscriptionManager.publish(ValueEvent.AFTER_SET);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(int v) {
        J2ModPrecondition.ensureFitsInShort(v, "register hi byte");
        setValue(J2ModDataUtil.hiByte(v), J2ModDataUtil.loByte(v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(short v) {
        setValue(J2ModDataUtil.hiByte(v), J2ModDataUtil.loByte(v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(byte[] v) {
        J2ModPrecondition.ensureCapacity("register bytes", v, 2);
        setValue(v[0], v[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHiByte(int v) {
        J2ModPrecondition.ensureFitsInByte(v, "register hi byte");
        byte lo = getBytes()[0];
        setValue((byte) v, lo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoByte(int v) {
        J2ModPrecondition.ensureFitsInByte(v, "register lo byte");
        byte lo = getBytes()[1];
        setValue(lo, (byte) v);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int subscribe(Subscriber<Register, ValueEvent> subscriber) {
        return this.subscriptionManager.subscribe(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unsubscribe(int id) {
        return this.subscriptionManager.unsubscribe(id);
    }


    @Override
    public String toString() {
        return "Register{hi=" + register[0] + ", lo=" + register[1] + "}";
    }

}
