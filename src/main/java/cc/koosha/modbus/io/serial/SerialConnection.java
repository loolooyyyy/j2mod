package cc.koosha.modbus.io.serial;

import cc.koosha.modbus.modbus.transport.SerialIOException;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * Interface that represents a public abstract serial port connection
 *
 * @author Felipe Herranz
 * @version 2.0 (March 2016)
 */
public interface SerialConnection {

    /**
     * Serial port is managed by a manager class (open, close, setBaudRate, ...)
     * this method returns the associated manager.
     *
     * @return associated manager class.
     */
    SerialConnectionManager getManager();


    /**
     * Read a specified number of bytes from the serial port
     *
     * @param buffer      Buffer to receive bytes from the port
     * @param bytesToRead Number of bytes to read
     * @return number of currently bytes read.
     */
    int readBytes(byte[] buffer, long bytesToRead) throws SerialIOException;

    /**
     * Write a specified number of bytes to the serial port
     *
     * @param buffer       Bytes to send to the port
     * @param bytesToWrite How many bytes to send
     * @return number of currently bytes written
     */
    int writeBytes(byte[] buffer, long bytesToWrite) throws SerialIOException;


    /**
     * Bytes available to read
     *
     * @return number of bytes currently available to read
     */
    int bytesAvailable() throws SerialIOException;

    /**
     * Read and discard any bytes available to read.
     */
    void discard() throws SerialIOException;

    /**
     * Injects a delay dependent on the last time we received a response or if a
     * fixed delay has been specified
     * <p>
     * 0 -> Injects a delay dependent on the baud rate
     *
     * @param transDelayMS             Fixed transaction delay (milliseconds)
     * @param lastTransactionTimestamp Timestamp of last transaction
     */
    void waitBetweenFrames(int transDelayMS, long lastTransactionTimestamp);


    void waitBetweenFrames(int len);


    InputStream getInputStream();

    OutputStream getOutputStream();

}
