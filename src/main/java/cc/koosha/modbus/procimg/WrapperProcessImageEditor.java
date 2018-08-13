package cc.koosha.modbus.procimg;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperProcessImageEditor implements ProcessImageEditor {

    protected final ProcessImageEditor wrapped;

    @Override
    public void setDigitalOut(int ref, DigitalOut out) {
        wrapped.setDigitalOut(ref, out);
    }

    @Override
    public void addDigitalOut(DigitalOut out) {
        wrapped.addDigitalOut(out);
    }

    @Override
    public void addDigitalOut(int ref, DigitalOut out) {
        wrapped.addDigitalOut(ref, out);
    }

    @Override
    public void removeDigitalOut(DigitalOut out) {
        wrapped.removeDigitalOut(out);
    }

    @Override
    public void setDigitalIn(int ref, DigitalIn di) {
        wrapped.setDigitalIn(ref, di);
    }

    @Override
    public void addDigitalIn(DigitalIn di) {
        wrapped.addDigitalIn(di);
    }

    @Override
    public void addDigitalIn(int ref, DigitalIn di) {
        wrapped.addDigitalIn(ref, di);
    }

    @Override
    public void removeDigitalIn(DigitalIn di) {
        wrapped.removeDigitalIn(di);
    }

    @Override
    public void setInputRegister(int ref, InputRegister reg) {
        wrapped.setInputRegister(ref, reg);
    }

    @Override
    public void addInputRegister(InputRegister reg) {
        wrapped.addInputRegister(reg);
    }

    @Override
    public void addInputRegister(int ref, InputRegister reg) {
        wrapped.addInputRegister(ref, reg);
    }

    @Override
    public void removeInputRegister(InputRegister reg) {
        wrapped.removeInputRegister(reg);
    }

    @Override
    public void setRegister(int ref, Register reg) {
        wrapped.setRegister(ref, reg);
    }

    @Override
    public void addRegister(Register reg) {
        wrapped.addRegister(reg);
    }

    @Override
    public void addRegister(int ref, Register reg) {
        wrapped.addRegister(ref, reg);
    }

    @Override
    public void removeRegister(Register reg) {
        wrapped.removeRegister(reg);
    }

    @Override
    public void setFile(int ref, File reg) {
        wrapped.setFile(ref, reg);
    }

    @Override
    public void addFile(File reg) {
        wrapped.addFile(reg);
    }

    @Override
    public void addFile(int ref, File reg) {
        wrapped.addFile(ref, reg);
    }

    @Override
    public void removeFile(File reg) {
        wrapped.removeFile(reg);
    }

    @Override
    public void setFIFO(int ref, FIFO reg) {
        wrapped.setFIFO(ref, reg);
    }

    @Override
    public void addFIFO(FIFO reg) {
        wrapped.addFIFO(reg);
    }

    @Override
    public void addFIFO(int ref, FIFO reg) {
        wrapped.addFIFO(ref, reg);
    }

    @Override
    public void removeFIFO(FIFO reg) {
        wrapped.removeFIFO(reg);
    }

}
