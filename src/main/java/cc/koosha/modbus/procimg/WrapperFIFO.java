package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public abstract class WrapperFIFO implements FIFO {

    protected final FIFO wrapped;

    @Override
    public int getMaxSize() {
        return wrapped.getMaxSize();
    }

    @Override
    public int getAddress() {
        return wrapped.getAddress();
    }

    @Override
    public int getRegisterCount() {
        return wrapped.getRegisterCount();
    }

    @Override
    public List<Register> getRegisters() {
        return wrapped.getRegisters();
    }

    @Override
    public void pushRegister(Register register) {
        wrapped.pushRegister(register);
    }

    @Override
    public void resetRegisters() {
        wrapped.resetRegisters();
    }

    @Override
    public int subscribe(Subscriber<FIFO, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }
}
