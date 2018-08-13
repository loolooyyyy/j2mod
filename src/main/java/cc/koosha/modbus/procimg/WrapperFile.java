package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class WrapperFile implements File {

    protected final File wrapped;

    @Override
    public int getFileNumber() {
        return wrapped.getFileNumber();
    }

    @Override
    public int getRecordCount() {
        return wrapped.getRecordCount();
    }

    @Override
    public Record getRecord(int i) {
        return wrapped.getRecord(i);
    }

    @Override
    public File setRecord(int i, Record record) {
        return wrapped.setRecord(i, record);
    }

    @Override
    public int subscribe(Subscriber<File, ValueEvent> subscriber) {
        return wrapped.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return wrapped.unsubscribe(id);
    }
}
