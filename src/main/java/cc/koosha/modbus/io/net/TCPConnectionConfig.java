package cc.koosha.modbus.io.net;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.net.InetAddress;


@Value
@Builder
@Wither
public final class TCPConnectionConfig {

    private final boolean useRtuOverTcp;

    /**
     * useUrgentData - sent a byte of urgent data when testing the TCP
     * connection.
     */
    private final boolean useUrgentData;

    private final int timeout;

    private final int port;

    private final InetAddress addr;

}
