package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscribable;


/**
 * @author Koosha Hosseiny
 * @author Julie
 *         File -- an abstraction of a Modbus File, as supported by the
 *         READ FILE RECORD and WRITE FILE RECORD commands.
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface File extends Subscribable<File, ValueEvent> {

    @Deprecated
    int getFileNumber();

    int getRecordCount();

    Record getRecord(int i);

    File setRecord(int i, Record record);

}
