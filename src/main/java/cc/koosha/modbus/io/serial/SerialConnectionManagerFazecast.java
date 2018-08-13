package cc.koosha.modbus.io.serial;

import cc.koosha.modbus.modbus.transport.SerialIOException;
import com.fazecast.jSerialComm.SerialPort;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@ThreadSafe
public final class SerialConnectionManagerFazecast implements SerialConnectionManager {

    @Getter
    private FazecastSerialKonf konf;

    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;

    private volatile boolean closed = true;
    private volatile boolean valid = true;

    private void ensureState() {
        if (!valid)
            throw new IllegalStateException("serial port manager is not in " +
                                                    "valid state, close it first " +
                                                    "to put it in valid state");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    public void open() throws SerialIOException {
        ensureState();
        if (!closed)
            throw new SerialIOException("port is not closed, can not open again");

        try {
            serialPort = SerialPort.getCommPort(konf.getPath());
            if (serialPort.getDescriptivePortName()
                          .toLowerCase()
                          .contains("bad port"))
                throw new IOException("invalid serial port: " + konf.getPath());

            serialPort.closePort();
            reconfigure();
            if (!serialPort.openPort(konf.getOpenDelay())) {
                close();
                val sb = new StringBuilder("can not open port: [")
                        .append(konf.getPath())
                        .append("] valid ports: [");
                val commPorts = SerialPort.getCommPorts();
                for (int i = 0; i < commPorts.length; i++) {
                    sb.append(commPorts[i].getDescriptivePortName());
                    if (i != commPorts.length - 1)
                        sb.append(", ");
                }
                sb.append("]");
                throw new IOException(sb.toString());
            }

            this.inputStream = serialPort.getInputStream();
            this.outputStream = serialPort.getOutputStream();
        }
        catch (SerialIOException e) {
            close();
            throw e;
        }
        catch (Exception e) {
            close();
            throw new SerialIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    public void close() throws SerialIOException {
        valid = false;
        closed = true;
        List<Exception> error = new ArrayList<Exception>();
        try {
            if (inputStream != null)
                inputStream.close();
        }
        catch (Exception e) {
            log.error("can not close input stream", e);
            error.add(e);
        }
        finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            }
            catch (Exception e) {
                log.error("can not close output stream", e);
                error.add(e);
            }
            finally {
                try {
                    if (serialPort != null)
                        serialPort.closePort();
                }
                catch (Exception e) {
                    log.error("can not close serial port", e);
                    error.add(e);
                }
            }
        }
        if (error.size() == 1) {
            throw new SerialIOException("could not close serial port [1 error]",
                                        error.get(0));
        }
        else if (!error.isEmpty()) {
            val sb = new StringBuilder();
            for (int i = 0; i < error.size(); i++) {
                sb.append("error ")
                  .append(i)
                  .append(": ")
                  .append(error.get(i).getMessage());
                if (i + 1 != error.size())
                    sb.append(", ");
            }
            throw new SerialIOException("could not close serial port [" + error.size() + " errors]: "
                                                + sb.toString(), error.get(error.size() - 1));
        }
        inputStream = null;
        outputStream = null;
        serialPort = null;
        valid = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    public boolean isOpen() {
        ensureState();
        return serialPort != null && serialPort.isOpen();
    }

    @Override
    @Synchronized
    public void clear() {
        this.valid = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    public void reconfigure() {
        ensureState();
        if (serialPort == null)
            throw new IllegalStateException("serial port not available");
        serialPort.setNumStopBits(konf.getStopBits());
        serialPort.setParity(konf.getParity());
        serialPort.setBaudRate(konf.getBaudRate());
        serialPort.setNumDataBits(konf.getDataBits());
        serialPort.setComPortTimeouts(konf.getTimeoutMode(),
                                      konf.getReadTimeout(),
                                      konf.getWriteTimeout());
        serialPort.setFlowControl(konf.getFlowControlIn() & konf.getFlowControlOut());
    }

    @Synchronized
    public void reconfigure(FazecastSerialKonf konf) {
        ensureState();
        this.setKonf(konf);
        this.reconfigure();
    }

    @SuppressWarnings("WeakerAccess")
    @Synchronized
    public void setKonf(@NonNull FazecastSerialKonf konf) {
        ensureState();
        this.konf = konf;
    }


    @Synchronized
    InputStream getInputStream() {
        ensureState();
        if (this.inputStream == null)
            throw new IllegalStateException("input stream not available");
        return this.inputStream;
    }

    @Synchronized
    OutputStream getOutputStream() {
        ensureState();
        if (this.outputStream == null)
            throw new IllegalStateException("output stream not available");
        return this.outputStream;
    }


}
