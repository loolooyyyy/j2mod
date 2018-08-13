package cc.koosha.modbus.msg;

import cc.koosha.modbus.procimg.ProcessImage;


public interface ModbusRequest extends ModbusMessage {

    /**
     * Returns the <tt>ModbusResponse</tt> that represents the answer to this
     * <tt>ModbusRequest</tt>.
     *
     * <p>
     * The implementation should take care about assembling the reply to this
     * <tt>ModbusRequest</tt>.
     *
     * <p>
     * This method is used to create responses from the process image associated
     * with the listener. It is commonly used to implement Modbus slave
     * instances.
     *
     * @return the corresponding <tt>ModbusResponse</tt>.
     */
    ModbusResponse createResponse(ProcessImage pi);

    void setTransactionID(int tid);

    ModbusResponse createExceptionResponse(int code);

}
