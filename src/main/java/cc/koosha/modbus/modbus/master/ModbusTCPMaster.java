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

import cc.koosha.modbus.io.net.TCPMasterConnection;
import cc.koosha.modbus.modbus.transaction.ModbusTCPTransaction;

import javax.annotation.concurrent.NotThreadSafe;


/**
 * Modbus/TCP Master facade.
 * <p>
 * TODO 1 - ?: make this class final.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@NotThreadSafe
public final class ModbusTCPMaster extends AbstractModbusMaster {

    private final TCPMasterConnection connection;

    public ModbusTCPMaster(ModbusMasterConfig config, TCPMasterConnection connection) {
        super(config);
        this.connection = connection;
    }

}