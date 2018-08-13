package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperRecord implements Record {

    protected final Record wrapped;

    @Override
    public int getRecordNumber() {
        return wrapped.getRecordNumber();
    }

    @Override
    public int getRegisterCount() {
        return wrapped.getRegisterCount();
    }

    @Override
    public Register getRegister(int register) {
        return wrapped.getRegister(register);
    }

    @Override
    public Record setRegister(int ref, Register register) {
        return wrapped.setRegister(ref, register);
    }

    @Override
    public int subscribe(Subscriber<Record, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }

}
