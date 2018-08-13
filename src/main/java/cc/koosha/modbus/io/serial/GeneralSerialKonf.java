package cc.koosha.modbus.io.serial;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Set;

import static cc.koosha.modbus.xinternal.J2ModCollections.setOf;


/**
 * A generic serial parameters class, hopefully covering all serial comm libraries for java.
 *
 * @author Koosha Hosseiny
 */
@Builder
@Wither
@Value
public final class GeneralSerialKonf {

    public static final String PARITY_NONE = "NONE";
    public static final String PARITY_ODD = "ODD";
    public static final String PARITY_EVEN = "EVEN";
    public static final String PARITY_MARK = "MARK";
    public static final String PARITY_SPACE = "SPACE";
    public static final Set<String> PARITY = setOf(
            PARITY_NONE, PARITY_EVEN, PARITY_ODD, PARITY_MARK, PARITY_SPACE);

    public static final int DATA_BITS_5 = 5;
    public static final int DATA_BITS_6 = 6;
    public static final int DATA_BITS_7 = 7;
    public static final int DATA_BITS_8 = 8;
    public static final Set<Integer> DATA_BITS = setOf(
            DATA_BITS_5, DATA_BITS_6, DATA_BITS_7, DATA_BITS_8);

    public static final String STOP_BITS_1 = "1";
    public static final String STOP_BITS_2 = "2";
    public static final String STOP_BITS_1_5 = "1.5";
    public static final Set<String> STOP_BITS = setOf(
            STOP_BITS_1, STOP_BITS_1_5, STOP_BITS_2);

    public static final String FLOW_DISABLED = "DISABLED";
    public static final String FLOW_RTS = "RTS";
    public static final String FLOW_CTS = "CTS";
    public static final String FLOW_DSR = "DSR";
    public static final String FLOW_DTR = "DTR";
    public static final String FLOW_XONXOFF_IN = "XONXOFF_IN";
    public static final String FLOW_XONXOFF_OUT = "XONXOFF_OUT";
    public static final Set<String> FLOW_CONTROL = setOf(
            FLOW_RTS, FLOW_CTS, FLOW_DSR, FLOW_DTR, FLOW_XONXOFF_IN, FLOW_XONXOFF_OUT);

    public static final int TIMEOUT_MODE_NON_BLOCKING = 0x00000000;
    public static final int TIMEOUT_MODE_READ_SEMI_BLOCKING = 0x00000001;
    public static final int TIMEOUT_MODE_WRITE_SEMI_BLOCKING = 0x00000010;
    public static final int TIMEOUT_MODE_READ_BLOCKING = 0x00000100;
    public static final int TIMEOUT_MODE_WRITE_BLOCKING = 0x00001000;
    public static final int TIMEOUT_MODE_SCANNER = 0x00010000;
    public static final Set<Integer> TIMEOUTS = setOf(
            TIMEOUT_MODE_NON_BLOCKING,
            TIMEOUT_MODE_READ_SEMI_BLOCKING,
            TIMEOUT_MODE_WRITE_SEMI_BLOCKING,
            TIMEOUT_MODE_READ_BLOCKING,
            TIMEOUT_MODE_WRITE_BLOCKING,
            TIMEOUT_MODE_SCANNER);

    private final String path;
    private final String encoding;
    private final boolean echo;

    private final String stopBits;
    private final String parity;
    private final int baudRate;
    private final int dataBits;

    private final int openDelay;
    private final int timeoutMode;
    private final int readTimeout;
    private final int writeTimeout;

    private final Set<String> flowControlIn;
    private final Set<String> flowControlOut;


    private final boolean enabled;
    private final boolean dump;
    private final int index;
}
