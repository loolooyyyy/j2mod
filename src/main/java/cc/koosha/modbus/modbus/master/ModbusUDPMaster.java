/*
 * Copyright 2002-2016 jamod & j2mod development teams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.koosha.modbus.modbus.master;

import cc.koosha.modbus.io.net.UDPMasterConnection;
import lombok.extern.slf4j.Slf4j;


/**
 * Modbus/UDP Master facade.
 * <p>
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public final class ModbusUDPMaster extends AbstractModbusMaster {

    private final UDPMasterConnection connection;

    protected ModbusUDPMaster(ModbusMasterConfig config,
                              UDPMasterConnection connection) {
        super(config);
        this.connection = connection;
    }

}