package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperDigitalIn implements DigitalIn {

    protected final DigitalIn wrapped;

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
    public int subscribe(Subscriber<DigitalIn, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }

}
