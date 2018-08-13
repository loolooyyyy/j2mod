package cc.koosha.modbus.msg;


public interface ModbusResponseFactory {

    /**
     * Factory method creating the required specialized {@link ModbusResponse}
     * instance.
     *
     * @param funcode the function code of the response.
     * @return a {@link ModbusResponse} instance specific for the given function
     * type.
     */
    ModbusResponse create(int funcode);

}
