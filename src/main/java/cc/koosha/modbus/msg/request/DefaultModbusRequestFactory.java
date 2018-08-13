package cc.koosha.modbus.msg.request;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusRequestFactory;


/**
 * {@inheritDoc}
 */
public final class DefaultModbusRequestFactory implements ModbusRequestFactory {

    private static final DefaultModbusRequestFactory INSTANCE = new DefaultModbusRequestFactory();

    public static DefaultModbusRequestFactory getInstance() {
        return INSTANCE;
    }

    private DefaultModbusRequestFactory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusRequest create(int funcode) {
        ModbusRequest request;

        switch (funcode) {
            case Modbus.READ_COILS:
                request = new ReadCoilsRequest();
                break;
            case Modbus.READ_INPUT_DISCRETES:
                request = new ReadInputDiscretesRequest();
                break;
            case Modbus.READ_MULTIPLE_REGISTERS:
                request = new ReadMultipleRegistersRequest();
                break;
            case Modbus.READ_INPUT_REGISTERS:
                request = new ReadInputRegistersRequest();
                break;
            case Modbus.WRITE_COIL:
                request = new WriteCoilRequest();
                break;
            case Modbus.WRITE_SINGLE_REGISTER:
                request = new WriteSingleRegisterRequest();
                break;
            case Modbus.WRITE_MULTIPLE_COILS:
                request = new WriteMultipleCoilsRequest();
                break;
            case Modbus.WRITE_MULTIPLE_REGISTERS:
                request = new WriteMultipleRegistersRequest();
                break;
            case Modbus.READ_EXCEPTION_STATUS:
                request = new ReadExceptionStatusRequest();
                break;
            case Modbus.READ_SERIAL_DIAGNOSTICS:
                request = new ReadSerialDiagnosticsRequest();
                break;
            case Modbus.READ_COMM_EVENT_COUNTER:
                request = new ReadCommEventCounterRequest();
                break;
            case Modbus.READ_COMM_EVENT_LOG:
                request = new ReadCommEventLogRequest();
                break;
            case Modbus.REPORT_SLAVE_ID:
                request = new ReportSlaveIDRequest();
                break;
            case Modbus.READ_FILE_RECORD:
                request = new ReadFileRecordRequest();
                break;
            case Modbus.WRITE_FILE_RECORD:
                request = new WriteFileRecordRequest();
                break;
            case Modbus.MASK_WRITE_REGISTER:
                request = new MaskWriteRegisterRequest();
                break;
            case Modbus.READ_WRITE_MULTIPLE:
                request = new ReadWriteMultipleRequest();
                break;
            case Modbus.READ_FIFO_QUEUE:
                request = new ReadFIFOQueueRequest();
                break;
            case Modbus.READ_MEI:
                request = new ReadMEIRequest();
                break;
            default:
                request = new IllegalFunctionRequest(funcode);
                break;
        }
        return request;
    }

}
