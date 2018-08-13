package cc.koosha.modbus.msg.response;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.ModbusResponseFactory;


/**
 * {@inheritDoc}
 */
public final class DefaultModbusResponseFactory implements ModbusResponseFactory {

    private static final DefaultModbusResponseFactory INSTANCE = new DefaultModbusResponseFactory();

    public static DefaultModbusResponseFactory getInstance() {
        return INSTANCE;
    }

    private DefaultModbusResponseFactory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusResponse create(int funcode) {
        ModbusResponse response;
        switch (funcode) {
            case Modbus.READ_COILS:
                response = new ReadCoilsResponse();
                break;
            case Modbus.READ_INPUT_DISCRETES:
                response = new ReadInputDiscretesResponse();
                break;
            case Modbus.READ_MULTIPLE_REGISTERS:
                response = new ReadMultipleRegistersResponse();
                break;
            case Modbus.READ_INPUT_REGISTERS:
                response = new ReadInputRegistersResponse();
                break;
            case Modbus.WRITE_COIL:
                response = new WriteCoilResponse();
                break;
            case Modbus.WRITE_SINGLE_REGISTER:
                response = new WriteSingleRegisterResponse();
                break;
            case Modbus.WRITE_MULTIPLE_COILS:
                response = new WriteMultipleCoilsResponse();
                break;
            case Modbus.WRITE_MULTIPLE_REGISTERS:
                response = new WriteMultipleRegistersResponse();
                break;
            case Modbus.READ_EXCEPTION_STATUS:
                response = new ReadExceptionStatusResponse();
                break;
            case Modbus.READ_SERIAL_DIAGNOSTICS:
                response = new ReadSerialDiagnosticsResponse();
                break;
            case Modbus.READ_COMM_EVENT_COUNTER:
                response = new ReadCommEventCounterResponse();
                break;
            case Modbus.READ_COMM_EVENT_LOG:
                response = new ReadCommEventLogResponse();
                break;
            case Modbus.REPORT_SLAVE_ID:
                response = new ReportSlaveIDResponse();
                break;
            case Modbus.READ_FILE_RECORD:
                response = new ReadFileRecordResponse();
                break;
            case Modbus.WRITE_FILE_RECORD:
                response = new WriteFileRecordResponse();
                break;
            case Modbus.MASK_WRITE_REGISTER:
                response = new MaskWriteRegisterResponse();
                break;
            case Modbus.READ_WRITE_MULTIPLE:
                response = new ReadWriteMultipleResponse();
                break;
            case Modbus.READ_FIFO_QUEUE:
                response = new ReadFIFOQueueResponse();
                break;
            case Modbus.READ_MEI:
                response = new ReadMEIResponse();
                break;
            default:
                if ((funcode & 0x80) != 0) {
                    response = new ExceptionResponse(funcode);
                }
                else {
                    response = new ExceptionResponse();
                }
                break;
        }
        return response;
    }

}
