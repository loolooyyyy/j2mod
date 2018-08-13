package cc.koosha.modbus.modbus.master;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;


@SuppressWarnings("WeakerAccess")
@Value
@Builder
@Wither
public final class ModbusMasterConfig {

    private final boolean validityCheck;
    private final boolean rtuOverTcp;
    private final boolean reconnecting;
    private final int retries;

}
