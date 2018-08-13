package cc.koosha.modbus.app;

import cc.koosha.modbus.modbus.transport.ModbusTransport;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.procimg.ProcessImage;
import cc.koosha.modbus.util.Function;
import cc.koosha.modbus.util.Producer;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public final class GuvnorImpl implements Runnable {

    private volatile boolean initialized = false;
    private volatile boolean running = true;

    private final Function<ModbusRequest, ModbusResponse> requestProcessor;

    private final Producer<ModbusTransport> transport;

    private final Producer<ProcessImage> processImage;

    @Synchronized
    private void init() {
        if (initialized)
            return;
        log.debug("initialize");
    }

    @Override
    public void run() {

    }

}
