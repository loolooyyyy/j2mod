package cc.koosha.modbus.procimg;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperInputRegister implements InputRegister {

    protected InputRegister wrapped;

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
