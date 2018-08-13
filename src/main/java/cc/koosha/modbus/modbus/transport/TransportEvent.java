package cc.koosha.modbus.modbus.transport;

import cc.koosha.modbus.msg.ModbusMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class TransportEvent {

    private final TransportEventType type;
    private final ModbusMessage msg;

    public static TransportEvent beforeWrite(ModbusMessage msg) {
        return new TransportEvent(TransportEventType.BEFORE_WRITE, msg);
    }

    public static TransportEvent afterWrite(ModbusMessage msg) {
        return new TransportEvent(TransportEventType.AFTER_WRITE, msg);
    }

}
