package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;


/**
 * {@inheritDoc}
 */
@EqualsAndHashCode
@RequiredArgsConstructor
@ThreadSafe
final class SynchronizedDigitalIn implements DigitalIn {

    private final DigitalIn digitalIn;


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public boolean isSet() {
        return digitalIn.isSet();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void set() {
        digitalIn.set();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void unset() {
        digitalIn.unset();
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<DigitalIn, ValueEvent> subscriber) {
        return digitalIn.subscribe(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public boolean unsubscribe(int id) {
        return digitalIn.unsubscribe(id);
    }


    /**
     * ATTENTION: No synchronization done.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32).append("synchronized<");
        try {
            sb.append(digitalIn.toString());
        }
        catch (Exception e) {
            sb.append("DigitalIn{?}");
        }
        return sb.append('>').toString();
    }

}
