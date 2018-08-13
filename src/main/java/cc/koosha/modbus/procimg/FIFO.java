package cc.koosha.modbus.procimg;

import cc.koosha.modbus.util.Subscribable;

import java.util.List;


/**
 * @author Koosha Hosseiny.
 * @author Julie
 * <p>
 * FIFO -- an abstraction of a Modbus FIFO, as supported by the
 * READ FIFO command.
 * <p>
 * The FIFO class is only intended to be used for testing purposes and does
 * not reflect the actual behavior of a FIFO in a real Modbus device.  In an
 * actual Modbus device, the FIFO is mapped within a fixed address.
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface FIFO extends Subscribable<FIFO, ValueEvent> {

    /**
     * Maximum number of elements this FIFO can hold.
     *
     * @return maximum number of elements this FIFO can hold.
     */
    @Deprecated
    int getMaxSize();

    @Deprecated
    int getAddress();

    int getRegisterCount();

    List<Register> getRegisters();

    void pushRegister(Register register);

    void resetRegisters();

}
