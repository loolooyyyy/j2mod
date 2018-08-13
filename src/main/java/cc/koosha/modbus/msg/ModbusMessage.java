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

import cc.koosha.modbus.Modbus;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * Interface defining a ModbusMessage.
 * <p>
 * TODO 9 - Architecture: probably it is possible to move many method / fields
 * to an auxiliary MetaClass or DefinitionClass.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface ModbusMessage {

    /**
     * Check the flag which indicates that this <tt>ModbusMessage</tt> is for a
     * headless (serial, or headless networked) connection.
     * <p>
     * So that 3 unsigned shorts are not read from transport:
     * <ul>
     * <li> {@link cc.koosha.modbus.msg.request.AbstractModbusRequest#setTransactionID(int)},
     * </li>
     * <li> {@link cc.koosha.modbus.msg.request.AbstractModbusRequest#setProtocolID(int)}
     * </li>
     * <li> {@link cc.koosha.modbus.msg.request.AbstractModbusRequest#dataLength}.
     * </li>
     * <li> {@link cc.koosha.modbus.msg.response.AbstractModbusResponse#setTransactionID(int)},
     * </li>
     * <li> {@link cc.koosha.modbus.msg.response.AbstractModbusResponse#setProtocolID(int)}
     * </li>
     * <li> {@link cc.koosha.modbus.msg.response.AbstractModbusResponse#dataLength}.
     * </li>
     * </ul>
     *
     * @return is for a headless (serial, or headless networked) connection
     */
    boolean isHeadless();

    /**
     * Sets the flag that marks this <tt>ModbusMessage</tt> as headless (for
     * serial transport).
     *
     * @see #isHeadless()
     */
    void setHeadless(boolean i);


    /**
     * Returns the transaction identifier of this <tt>ModbusMessage</tt> as
     * <tt>int</tt>.
     *
     * <p>
     * The identifier is a 2-byte (short) non negative integer value valid in
     * the range of 0-65535.
     *
     * @return the transaction identifier as <tt>int</tt>.
     */
    int getTransactionID();

    /**
     * Returns the protocol identifier of this <tt>ModbusMessage</tt> as
     * <tt>int</tt>.
     *
     * <p>
     * The identifier is a 2-byte (short) non negative integer value valid in
     * the range of 0-65535.
     *
     * @return the protocol identifier as <tt>int</tt>.
     */
    int getProtocolID();

    /**
     * Returns the length of the data appended after the protocol header.
     * <p>
     *
     * @return the data length as <tt>int</tt>.
     */
    int getDataLength();

    /**
     * Returns the unit identifier of this <tt>ModbusMessage</tt> as
     * <tt>int</tt>.
     *
     * <p>
     * The identifier is a 1-byte non negative integer value valid in the range
     * of 0-255.
     *
     * @return the unit identifier as <tt>int</tt>.
     */
    int getUnitID();

    /**
     * Returns the function code of this <tt>ModbusMessage</tt> as
     * <tt>int</tt>.<br> The function code is a 1-byte non negative integer
     * value valid in the range of 0-127.
     * <p>
     * TODO 0 - ?: use links in doc
     * <p>
     * Function codes are ordered in conformance classes their values are
     * specified in
     * <tt>cc.koosha.modbus.Modbus</tt>.
     *
     * @return the function code as <tt>int</tt>.
     * @see Modbus
     */
    int getFunctionCode();

    /**
     * Returns the <i>raw</i> message as an array of bytes.
     * <p>
     *
     * @return the <i>raw</i> message as <tt>byte[]</tt>.
     */
    byte[] getMessage();

    /**
     * Returns the number of bytes that will be written by {@link
     * #writeTo(DataOutput)}.
     *
     * @return the number of bytes that will be written as <tt>int</tt>.
     */
    int getOutputLength();

    /**
     * Writes this <tt>Transportable</tt> to the given <tt>DataOutput</tt>.
     *
     * @param dout the <tt>DataOutput</tt> to write to.
     * @throws java.io.IOException if an I/O error occurs.
     */
    void writeTo(DataOutput dout) throws IOException;

    /**
     * Reads this <tt>Transportable</tt> from the given
     * <tt>DataInput</tt>.
     *
     * @param din the <tt>DataInput</tt> to read from.
     * @throws java.io.IOException if an I/O error occurs or the data is
     *                             invalid.
     */
    void readFrom(DataInput din) throws IOException;

}
