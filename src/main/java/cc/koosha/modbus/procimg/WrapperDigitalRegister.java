package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperDigitalRegister<T extends DigitalRegister<T>> implements DigitalRegister<T> {

    protected final DigitalRegister<T> wrapped;

    @Override
    public boolean isSet() {
        return wrapped.isSet();
    }

    @Override
    public void set() {
        wrapped.set();
    }

    @Override
    public void unset() {
        wrapped.unset();
    }

    @Override
    public int subscribe(Subscriber<T, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }

}
