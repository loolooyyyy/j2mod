package cc.koosha.modbus.modbus.transaction;

import cc.koosha.modbus.ModbusException;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;


public interface ModbusTransaction {

    ModbusRequest getRequest();

    void setRequest(ModbusRequest request);

    ModbusResponse getResponse();

    /**
     * Executes this <tt>ModbusTransaction</tt>. Locks the
     * <tt>ModbusTransport</tt> for sending the <tt>ModbusRequest</tt> and
     * reading the related <tt>ModbusResponse</tt>. If reconnecting is activated
     * the connection will be opened for the transaction and closed afterwards.
     * <p>
     *
     * @throws ModbusException if an I/O error occurs, or the response is a
     *                         modbus protocol exception.
     */
    void execute() throws ModbusException;

}
