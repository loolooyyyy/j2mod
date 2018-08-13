package cc.koosha.modbus.io.serial;

import com.fazecast.jSerialComm.SerialPort;
import cc.koosha.modbus.xinternal.J2ModCollections;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import lombok.val;

import java.util.Set;


/**
 * Same as {@link GeneralSerialKonf} but values are adjusted for fazecast library.
 *
 * Static method prefixed with g -> globalValueOf...<br>
 * Static method prefixed with g -> fazecastValueOf...
 */
@Builder
@Wither
@Value
public final class FazecastSerialKonf {

    private final String path;
    private final String encoding;
    private final boolean echo;

    private final int stopBits;
    private final int parity;
    private final int baudRate;
    private final int dataBits;

    private final int openDelay;
    private final int timeoutMode;
    private final int readTimeout;
    private final int writeTimeout;

    private final int flowControlIn;
    private final int flowControlOut;

    private final boolean enabled;
    private final boolean dump;
    private final int index;


    // =========================================================================

    @SuppressWarnings("WeakerAccess")
    public GeneralSerialKonf toGeneralSerialKonf() {
        return GeneralSerialKonf.builder()
                                .encoding(gEncoding(this.encoding))
                                .echo(gEcho(this.echo))
                                .stopBits(gStopBits(this.stopBits))
                                .parity(gParity(this.parity))
                                .baudRate(gBaudRate(this.baudRate))
                                .dataBits(gDataBits(this.dataBits))
                                .openDelay(gOpenDelay(this.openDelay))
                                .timeoutMode(gTimeoutMode(this.timeoutMode))
                                .readTimeout(gReadTimeout(this.readTimeout))
                                .writeTimeout(gWriteTimeout(this.writeTimeout))
                                .flowControlIn(gFlowControlIn(this.flowControlIn))
                                .flowControlOut(gFlowControlOut(this.flowControlOut))
                                .enabled(gEnabled(this.enabled))
                                .dump(gDump(this.dump))
                                .index(gIndex(this.index))
                                .build();
    }

    public static FazecastSerialKonf valueOf(GeneralSerialKonf generalSerialKonf) {
        return FazecastSerialKonf.builder()
                                 .encoding(fEncoding(generalSerialKonf.getEncoding()))
                                 .echo(fEcho(generalSerialKonf.isEcho()))
                                 .stopBits(fStopBits(generalSerialKonf.getStopBits()))
                                 .parity(fParity(generalSerialKonf.getParity()))
                                 .baudRate(fBaudRate(generalSerialKonf.getBaudRate()))
                                 .dataBits(fDataBits(generalSerialKonf.getDataBits()))
                                 .openDelay(fOpenDelay(generalSerialKonf.getOpenDelay()))
                                 .timeoutMode(fTimeoutMode(generalSerialKonf.getTimeoutMode()))
                                 .readTimeout(fReadTimeout(generalSerialKonf.getReadTimeout()))
                                 .writeTimeout(fWriteTimeout(generalSerialKonf.getWriteTimeout()))
                                 .flowControlIn(fFlowControlIn(generalSerialKonf.getFlowControlIn()))
                                 .flowControlOut(fFlowControlOut(generalSerialKonf.getFlowControlOut()))
                                 .enabled(fEnabled(generalSerialKonf.isEnabled()))
                                 .dump(fDump(generalSerialKonf.isDump()))
                                 .index(fIndex(generalSerialKonf.getIndex()))
                                 .build();
    }

    // =========================================================================


    private static int gIndex(int index) {
        return index;
    }

    private static boolean gDump(boolean dump) {
        return dump;
    }

    private static boolean gEnabled(boolean enabled) {
        return enabled;
    }

    private static Set<String> gFlowControlOut(int flowControlOut) {
        if (flowControlOut == SerialPort.FLOW_CONTROL_DISABLED)
            return J2ModCollections.singletonSet(GeneralSerialKonf.FLOW_DISABLED);

        val f = J2ModCollections.<String>newModifiableSet();
        if ((flowControlOut & SerialPort.FLOW_CONTROL_CTS_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_CTS);
        if ((flowControlOut & SerialPort.FLOW_CONTROL_DSR_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_DSR);
        if ((flowControlOut & SerialPort.FLOW_CONTROL_DTR_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_DTR);
        if ((flowControlOut & SerialPort.FLOW_CONTROL_RTS_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_RTS);
        if ((flowControlOut & SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_XONXOFF_IN);
        if ((flowControlOut & SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED) > 0)
            f.add(GeneralSerialKonf.FLOW_XONXOFF_OUT);

        return J2ModCollections.unmodifiable(f);
    }


    private static Set<String> gFlowControlIn(int flowControlIn) {
        return gFlowControlOut(flowControlIn);
    }

    private static int gWriteTimeout(int writeTimeout) {
        return writeTimeout;
    }

    private static int gReadTimeout(int readTimeout) {
        return readTimeout;
    }

    private static int gTimeoutMode(int timeoutMode) {
        return timeoutMode;
    }

    private static int gOpenDelay(int openDelay) {
        return openDelay;
    }

    private static int gDataBits(int dataBits) {
        return dataBits;
    }

    private static int gBaudRate(int baudRate) {
        return baudRate;
    }

    private static boolean gEcho(boolean echo) {
        return echo;
    }

    private static String gEncoding(String encoding) {
        return encoding;
    }

    private static String gStopBits(int stopBits) {
        if (SerialPort.ONE_STOP_BIT == stopBits)
            return GeneralSerialKonf.STOP_BITS_1;
        else if (SerialPort.TWO_STOP_BITS == stopBits)
            return GeneralSerialKonf.STOP_BITS_2;
        else if (SerialPort.ONE_POINT_FIVE_STOP_BITS == stopBits)
            return GeneralSerialKonf.STOP_BITS_1_5;
        else
            throw new IllegalArgumentException("unknown stop bit: " + stopBits);
    }

    private static String gParity(int parity) {
        switch (parity) {
            case SerialPort.EVEN_PARITY:
                return GeneralSerialKonf.PARITY_EVEN;
            case SerialPort.ODD_PARITY:
                return GeneralSerialKonf.PARITY_ODD;
            case SerialPort.MARK_PARITY:
                return GeneralSerialKonf.PARITY_MARK;
            case SerialPort.SPACE_PARITY:
                return GeneralSerialKonf.PARITY_SPACE;
            case SerialPort.NO_PARITY:
                return GeneralSerialKonf.PARITY_NONE;
        }
        throw new IllegalArgumentException("unknown parity: " + parity);
    }


    // =========================================================================

    private static int fIndex(int index) {
        return index;
    }

    private static boolean fDump(boolean dump) {
        return dump;
    }

    private static boolean fEnabled(boolean enabled) {
        return enabled;
    }

    private static int fFlowControlOut(Set<String> control) {
        if (control.contains(GeneralSerialKonf.FLOW_DISABLED) && control.size() > 1)
            throw new IllegalArgumentException("can not combine disable flow control with other flow controls");

        int flow = SerialPort.FLOW_CONTROL_DISABLED;
        for (final String s : control) {
            if(GeneralSerialKonf.FLOW_DISABLED.equals(s) && control.size() > 1)
                throw new IllegalArgumentException("too many flow control flags");
            if (GeneralSerialKonf.FLOW_CTS.equals(s))
                flow |= SerialPort.FLOW_CONTROL_CTS_ENABLED;
            else if (GeneralSerialKonf.FLOW_RTS.equals(s))
                flow |= SerialPort.FLOW_CONTROL_RTS_ENABLED;
            else if (GeneralSerialKonf.FLOW_DSR.equals(s))
                flow |= SerialPort.FLOW_CONTROL_DSR_ENABLED;
            else if (GeneralSerialKonf.FLOW_DTR.equals(s))
                flow |= SerialPort.FLOW_CONTROL_DTR_ENABLED;
            else if (GeneralSerialKonf.FLOW_XONXOFF_IN.equals(s))
                flow |= SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED;
            else if (GeneralSerialKonf.FLOW_XONXOFF_OUT.equals(s))
                flow |= SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED;
        }
        return flow;
    }

    private static int fFlowControlIn(Set<String> control) {
        // They do not really differ.
        return fFlowControlOut(control);
    }

    private static int fWriteTimeout(int writeTimeout) {
        return writeTimeout;
    }

    private static int fReadTimeout(int readTimeout) {
        return readTimeout;
    }

    private static int fTimeoutMode(int timeoutMode) {
        return timeoutMode;
    }

    private static int fOpenDelay(int openDelay) {
        return openDelay;
    }

    private static int fDataBits(int dataBits) {
        return dataBits;
    }

    private static int fBaudRate(int baudRate) {
        return baudRate;
    }

    private static boolean fEcho(boolean echo) {
        return echo;
    }

    private static String fEncoding(String encoding) {
        return encoding;
    }

    private static int fStopBits(String stopBits) {
        if (GeneralSerialKonf.STOP_BITS_1.equals(stopBits))
            return SerialPort.ONE_STOP_BIT;
        else if (GeneralSerialKonf.STOP_BITS_1_5.equals(stopBits))
            return SerialPort.ONE_POINT_FIVE_STOP_BITS;
        else if (GeneralSerialKonf.STOP_BITS_2.equals(stopBits))
            return SerialPort.TWO_STOP_BITS;
        else
            throw new IllegalArgumentException("unknown stop bit: " + stopBits);
    }

    private static int fParity(String parity) {
        if (GeneralSerialKonf.PARITY_EVEN.equals(parity)) {
            return SerialPort.EVEN_PARITY;
        }
        if (GeneralSerialKonf.PARITY_ODD.equals(parity)) {
            return SerialPort.ODD_PARITY;
        }
        if (GeneralSerialKonf.PARITY_NONE.equals(parity)) {
            return SerialPort.NO_PARITY;
        }
        if (GeneralSerialKonf.PARITY_SPACE.equals(parity)) {
            return SerialPort.SPACE_PARITY;
        }
        if (GeneralSerialKonf.PARITY_MARK.equals(parity)) {
            return SerialPort.MARK_PARITY;
        }
        throw new IllegalArgumentException("unknown parity: " + parity);
    }

}
