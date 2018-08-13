package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Range;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public abstract class WrapperProcessImage implements ProcessImage {

    protected final ProcessImage wrapped;

    @Override
    public List<DigitalOut> getDigitalOutRange(Range<Integer> range) {
        return wrapped.getDigitalOutRange(range);
    }

    @Override
    public List<DigitalOut> getDigitalOutRange(int offset, int count) {
        return wrapped.getDigitalOutRange(offset, count);
    }

    @Override
    public DigitalOut getDigitalOut(int ref) {
        return wrapped.getDigitalOut(ref);
    }

    @Override
    public int getDigitalOutCount() {
        return wrapped.getDigitalOutCount();
    }

    @Override
    public List<DigitalIn> getDigitalInRange(Range<Integer> range) {
        return wrapped.getDigitalInRange(range);
    }

    @Override
    public List<DigitalIn> getDigitalInRange(int offset, int count) {
        return wrapped.getDigitalInRange(offset, count);
    }

    @Override
    public DigitalIn getDigitalIn(int ref) {
        return wrapped.getDigitalIn(ref);
    }

    @Override
    public int getDigitalInCount() {
        return wrapped.getDigitalInCount();
    }

    @Override
    public List<InputRegister> getInputRegisterRange(Range<Integer> range) {
        return wrapped.getInputRegisterRange(range);
    }

    @Override
    public List<InputRegister> getInputRegisterRange(int offset, int count) {
        return wrapped.getInputRegisterRange(offset, count);
    }

    @Override
    public InputRegister getInputRegister(int ref) {
        return wrapped.getInputRegister(ref);
    }

    @Override
    public int getInputRegisterCount() {
        return wrapped.getInputRegisterCount();
    }

    @Override
    public List<Register> getRegisterRange(Range<Integer> range) {
        return wrapped.getRegisterRange(range);
    }

    @Override
    public List<Register> getRegisterRange(int offset, int count) {
        return wrapped.getRegisterRange(offset, count);
    }

    @Override
    public Register getRegister(int ref) {
        return wrapped.getRegister(ref);
    }

    @Override
    public int getRegisterCount() {
        return wrapped.getRegisterCount();
    }

    @Override
    public File getFile(int ref) {
        return wrapped.getFile(ref);
    }

    @Override
    public File getFileByNumber(int ref) {
        return wrapped.getFileByNumber(ref);
    }

    @Override
    public int getFileCount() {
        return wrapped.getFileCount();
    }

    @Override
    public FIFO getFIFO(int ref) {
        return wrapped.getFIFO(ref);
    }

    @Override
    public FIFO getFIFOByAddress(int ref) {
        return wrapped.getFIFOByAddress(ref);
    }

    @Override
    public int getFIFOCount() {
        return wrapped.getFIFOCount();
    }

    @Override
    public ProcessImageEditor editor() {
        return wrapped.editor();
    }

    @Override
    public RegisterFactory registerFactory() {
        return wrapped.registerFactory();
    }

}
