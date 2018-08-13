package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;


/**
 * A wrapper for making a thread safe instance of {@link File}.
 *
 * <p>
 * TODO is it worth using {@link java.util.concurrent.locks.ReadWriteLock} and ->
 * also, access register values directly for {@link SimpleFile}?
 *
 * @author Koosha Hosseiny
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ThreadSafe
public final class SynchronizedFile implements File {

    private final File wrapped;


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public int getFileNumber() {
        return wrapped.getFileNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int getRecordCount() {
        return wrapped.getRecordCount();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public Record getRecord(int i) {
        return wrapped.getRecord(i);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public File setRecord(int i, Record record) {
        return wrapped.setRecord(i, record);
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<File, ValueEvent> subscriber) {
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
            sb.append("file{?}");
        }
        return sb.append('>').toString();
    }

}
