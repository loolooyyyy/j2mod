package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Synchronizes operations on a {@link Record}. A wrapper for any non-thread-safe record.
 *
 * @author Koosha Hosseiny
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ThreadSafe
final class SynchronizedRecord implements Record {

    private final Record wrapped;


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public int getRecordNumber() {
        return wrapped.getRecordNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int getRegisterCount() {
        return wrapped.getRegisterCount();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public Register getRegister(int register) {
        return wrapped.getRegister(register);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public Record setRegister(int ref, Register register) {
        return wrapped.setRegister(ref, register);
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<Record, ValueEvent> subscriber) {
        return this.wrapped.subscribe(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public boolean unsubscribe(int id) {
        return this.wrapped.unsubscribe(id);
    }


    /**
     * ATTENTION: No synchronization done.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("synchronized<");
        try {
            sb.append(wrapped.toString());
        }
        catch (Exception e) {
            sb.append("record{?}");
        }
        return sb.append('>').toString();
    }

}
