package cc.koosha.modbus.modbus.transport;

import java.io.IOException;


public class SerialIOException extends IOException {

    public SerialIOException() {
        super();
    }

    public SerialIOException(String message) {
        super(message);
    }

    public SerialIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialIOException(Throwable cause) {
        super(cause);
    }

}
