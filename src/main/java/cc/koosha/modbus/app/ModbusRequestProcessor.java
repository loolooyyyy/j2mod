package cc.koosha.modbus.app;

import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;


public interface ModbusRequestProcessor {

    ModbusResponse apply(ModbusRequest request);

}
