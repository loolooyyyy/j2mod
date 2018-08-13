package cc.koosha.modbus.modbus.transport;

import cc.koosha.modbus.msg.ModbusMessage;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;

import java.io.IOException;


// TODO timeout

/**
 * The I/O mechanisms for sending/receiving messages to/from other endpoints.
 *
 * <p>
 * It is not concerned with details of the message details, and usually it only
 * handles the communication means, such as serial port or TCP socket.
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface ModbusTransport { // extends Closeable, Openable {

    /**
     * Writes a {@link ModbusMessage} to the output stream of this
     * <tt>ModbusTransport</tt>.
     */
    void writeMessage(ModbusMessage msg) throws IOException;

    /**
     * Reads a {@link ModbusResponse} from the input stream.
     */
    ModbusResponse readResponse() throws IOException;

    /**
     * Reads a {@link ModbusRequest} from the input stream.
     */
    ModbusRequest readRequest() throws IOException;

}
