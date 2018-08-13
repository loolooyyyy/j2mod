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
final class SynchronizedDigitalOut implements DigitalOut {

    private final DigitalOut digitalOut;


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public boolean isSet() {
        return digitalOut.isSet();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void set() {
        digitalOut.set();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void unset() {
        digitalOut.unset();
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<DigitalOut, ValueEvent> subscriber) {
        return digitalOut.subscribe(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public boolean unsubscribe(int id) {
        return digitalOut.unsubscribe(id);
    }


    /**
     * ATTENTION: No synchronization done.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32).append("synchronized<");
        try {
            sb.append(digitalOut.toString());
        }
        catch (Exception e) {
            sb.append("DigitalOut{?}");
        }
        return sb.append('>').toString();
    }

}

