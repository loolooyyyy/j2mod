package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;


/**
 * A wrapper for making a thread safe instance of {@link FIFO}.
 *
 * <p>
 * TODO is it worth using {@link java.util.concurrent.locks.ReadWriteLock} and ->
 * also, access register values directly for {@link SimpleFIFO}?
 *
 * @author Koosha Hosseiny
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ThreadSafe
final class SynchronizedFIFO implements FIFO {

    private final FIFO wrapped;


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
    public List<Register> getRegisters() {
        return wrapped.getRegisters();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void pushRegister(Register register) {
        wrapped.pushRegister(register);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public void resetRegisters() {
        wrapped.resetRegisters();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    @Deprecated
    public int getMaxSize() {
        return wrapped.getMaxSize();
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int getAddress() {
        return wrapped.getAddress();
    }


    /**
     * {@inheritDoc}
     */
    @Synchronized
    @Override
    public int subscribe(Subscriber<FIFO, ValueEvent> subscriber) {
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
            sb.append("fifo{?}");
        }
        return sb.append('>').toString();
    }

}
