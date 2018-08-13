package cc.koosha.modbus.procimg;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperRegisterFactory implements RegisterFactory {

    protected final RegisterFactory wrapped;

    @Override
    public DigitalIn createDigitalIn(boolean initialState) {
        return wrapped.createDigitalIn(initialState);
    }

    @Override
    public DigitalOut createDigitalOut(boolean initialState) {
        return wrapped.createDigitalOut(initialState);
    }

    @Override
    public InputRegister createInputRegister(byte hi, byte lo) {
        return wrapped.createInputRegister(hi);
    }

    @Override
    public InputRegister createInputRegister(int initialValue) {
        return wrapped.createInputRegister(initialValue);
    }

    @Override
    public InputRegister createRegister(byte hi, byte lo) {
        return wrapped.createRegister(hi);
    }

    @Override
    public InputRegister createRegister(int initialValue) {
        return wrapped.createRegister(initialValue);
    }

    @Override
    public Record record(int recordNumber, int registers) {
        return wrapped.record(recordNumber, registers);
    }

    @Override
    public FIFO fifo(int address, int maxSize) {
        return wrapped.fifo(address, maxSize);
    }

    @Override
    public File file(int fileNumber, int records) {
        return wrapped.file(fileNumber, records);
    }

    @Override
    public DigitalIn threadSafe(DigitalIn value) {
        return wrapped.threadSafe(value);
    }

    @Override
    public DigitalOut threadSafe(DigitalOut value) {
        return wrapped.threadSafe(value);
    }

    @Override
    public Register threadSafe(Register value) {
        return wrapped.threadSafe(value);
    }

    @Override
    public Record threadSafe(Record value) {
        return wrapped.threadSafe(value);
    }

    @Override
    public FIFO threadSafe(FIFO value) {
        return wrapped.threadSafe(value);
    }

    @Override
    public File threadSafe(File value) {
        return wrapped.threadSafe(value);
    }

}
