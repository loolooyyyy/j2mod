package cc.koosha.modbus.app;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.procimg.ProcessImage;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static cc.koosha.modbus.xinternal.J2ModCollections.copy;
import static cc.koosha.modbus.xinternal.J2ModDebugUtils.toHex;


/**
 * Takes a {@link ModbusRequest} and produces a corresponding {@link
 * ModbusResponse} according to {@link ProcessImage}s available.
 * <p>
 * Thread-safe IF {@link ProcessImage}s are thread safe.
 */
@Slf4j
public final class DefaultModbusRequestProcessor implements ModbusRequestProcessor {

    private final Config config;
    private final Map<Integer, ProcessImage> pi;

    @SuppressWarnings("WeakerAccess")
    public DefaultModbusRequestProcessor(@NonNull Map<Integer, ProcessImage> pi,
                                         @NonNull Config config) {
        this.pi = copy(pi);
        this.config = config;
    }

    public DefaultModbusRequestProcessor(@NonNull Map<Integer, ProcessImage> pi) {
        this(pi, DEFAULT_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusResponse apply(ModbusRequest request) {
        final ModbusResponse response;
        if (!pi.containsKey(request.getUnitID()) && !config.forgiveNonExistingUnitId) {
            // response.setAuxiliaryType(ModbusResponse.AuxiliaryMessageTypes.UNIT_ID_MISMATCH);
            // TODO: illegal address?
            response = request.createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
        }
        else if (!pi.containsKey(request.getUnitID()) && config.resortToDefaultUnitId) {
            response = request.createResponse(pi.get(config.defaultUnitId));
        }
        else {
            response = request.createResponse(pi.get(request.getUnitID()));
        }

        if (log.isDebugEnabled()) {
            log.debug("Request: {}", toHex(request));
            log.debug("Response: {}", toHex(response));
        }

        return response;
    }

    // -------------------

    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    @Builder
    @EqualsAndHashCode
    public static final class Config {
        private int defaultUnitId = -1;
        private boolean forgiveNonExistingUnitId = false;
        private boolean resortToDefaultUnitId = false;
    }

    private static final Config DEFAULT_CONFIG = Config.builder().build();

}
