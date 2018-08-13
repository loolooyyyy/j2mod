package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperRegister implements Register {

    protected final Register wrapped;

    @Override
    public void setValue(int v) {
        wrapped.setValue(v);
    }

    @Override
    public void setValue(short v) {
        wrapped.setValue(v);
    }

    @Override
    public void setValue(byte[] v) {
        wrapped.setValue(v);
    }

    @Override
    public void setValue(byte hi, byte lo) {
        wrapped.setValue(hi, lo);
    }

    @Override
    public void setHiByte(int v) {
        wrapped.setHiByte(v);
    }

    @Override
    public void setLoByte(int v) {
        wrapped.setLoByte(v);
    }

    @Override
    public int subscribe(Subscriber<Register, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }

    @Override
    public int getValue() {
        return wrapped.getValue();
    }

    @Override
    public byte[] getBytes() {
        return wrapped.getBytes();
    }

    @Override
    public int toUnsignedShort() {
        return wrapped.toUnsignedShort();
    }

    @Override
    public short toShort() {
        return wrapped.toShort();
    }

    @Override
    public byte[] toBytes() {
        return wrapped.toBytes();
    }

}
