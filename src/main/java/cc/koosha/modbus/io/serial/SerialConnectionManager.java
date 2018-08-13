package cc.koosha.modbus.io.serial;

import cc.koosha.modbus.modbus.transport.SerialIOException;


/**
 * Manages low level details of a serial connection.
 */
public interface SerialConnectionManager {

    /**
     * Open the underlying serial port.
     *
     * @throws SerialIOException If the port is not available or cannot be
     *                           opened
     */
    void open() throws SerialIOException;

    /**
     * Close the underlying serial port and clean up associated elements.
     *
     * @throws SerialIOException If the port could not be closed.
     */
    void close() throws SerialIOException;

    /**
     * Reports the open status of the underlying port.
     *
     * @return true if port is open, false if port is closed or the port is
     * invalid.
     */
    boolean isOpen();

    /**
     * Put port into valid state, regardless of anything else.
     */
    void clear();

    /**
     * Re-configures serial port, with new parameters.
     * <p>
     * If this operation is not implemented, it will throw a {@link
     * UnsupportedOperationException}.
     * <p>
     * How to pass new parameters to an implementation of this class is
     * implementation specific.
     */
    void reconfigure();

}
