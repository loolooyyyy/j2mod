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
package cc.koosha.modbus.msg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public interface ModbusResponse extends ModbusMessage {

    void setTransactionID(int tid);

    void setDataLength(int length);

    /**
     * Writes the subclass specific data to the given DataOutput.
     *
     * @param dout the DataOutput to be written to.
     * @throws IOException if an I/O related error occurs.
     */
    void writeData(DataOutput dout) throws IOException;

    /**
     * Reads the subclass specific data from the given DataInput instance.
     *
     * @param din the DataInput to read from.
     * @throws IOException if an I/O related error occurs.
     */
    void readData(DataInput din) throws IOException;

    // TODO 1 - ?: Rename to error type?
    // TODO 9 - Architecture: try to deprecate, it behaves as a boolean flag only.
    enum AuxiliaryMessageTypes {
        NONE,
        UNIT_ID_MISMATCH
    }

}