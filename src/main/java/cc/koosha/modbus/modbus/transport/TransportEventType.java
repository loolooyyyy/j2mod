package cc.koosha.modbus.modbus.transport;


public enum TransportEventType {

    AFTER_RESPONSE,
    BEFORE_RESPONSE,

    AFTER_REQUEST,
    BEFORE_REQUEST,

    BEFORE_WRITE,
    AFTER_WRITE,

    BEFORE_READ,
    AFTER_READ,

}
