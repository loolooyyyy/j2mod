package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscribable;


/**
 * @author Koosha Hosseiny.
 * @author Julie
 * <p>
 * File -- an abstraction of a Modbus Record, as supported by the
 * READ FILE RECORD and WRITE FILE RECORD commands.
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface Record extends Subscribable<Record, ValueEvent> {

    @Deprecated
    int getRecordNumber();

    int getRegisterCount();

    Register getRegister(int register);

    Record setRegister(int ref, Register register);

}
