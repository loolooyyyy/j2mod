package cc.koosha.modbus.msg;


public interface ModbusRequestFactory {

    /**
     * Factory method creating the required specialized {@link ModbusRequest}
     * instance.
     *
     * @param funcode the function code of the request.
     * @return a {@link ModbusRequest} instance specific for the given function
     * type.
     */
    ModbusRequest create(int funcode);

}
