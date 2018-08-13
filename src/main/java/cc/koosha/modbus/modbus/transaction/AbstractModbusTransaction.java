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
package cc.koosha.modbus.modbus.transaction;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.modbus.transport.ModbusTransport;
import cc.koosha.modbus.msg.ModbusRequest;
import cc.koosha.modbus.msg.ModbusResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Random;


/**
 * Interface defining a ModbusTransaction.
 * <p>
 * A transaction is defined by the sequence of sending a request message and
 * receiving a related response message.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public abstract class AbstractModbusTransaction implements ModbusTransaction {

    private static final Random random = new SecureRandom();


    private final ModbusTransport transport;

    public ModbusTransport getTransport() {
        return transport;
    }

    AbstractModbusTransaction(ModbusTransport transport) {
        this.transport = transport;
    }


    /**
     * Returns the <tt>ModbusRequest</tt> instance associated with this
     * <tt>ModbusTransaction</tt>
     */
    @Getter
    private ModbusRequest request;

    /**
     * Returns the <tt>ModbusResponse</tt> instance associated with this
     * <tt>ModbusTransaction</tt>.
     */
    @Setter(AccessLevel.PROTECTED)
    @Getter
    private ModbusResponse response;

    /**
     * The flag that controls whether the validity of a transaction will be
     * checked.
     */
    @Setter
    @Getter
    private boolean validityCheck = Modbus.DEFAULT_VALIDITYCHECK;

    /**
     * amount of retries for opening the connection for executing the
     * transaction.
     */
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private int retries = Modbus.DEFAULT_RETRIES;


    static int transactionID = Modbus.DEFAULT_TRANSACTION_ID;


    /**
     * Sets the <tt>ModbusRequest</tt> for this
     * <tt>ModbusTransaction</tt>.<p>
     * The related <tt>ModbusResponse</tt> is acquired from the passed in
     * <tt>ModbusRequest</tt> instance.<br>
     * <p>
     *
     * @param req a <tt>ModbusRequest</tt>.
     */
    public void setRequest(ModbusRequest req) {
        request = req;
        if (req != null)
            request.setTransactionID(getTransactionID());
    }

    /**
     * getTransactionID -- get the next transaction ID to use.
     *
     * @return next transaction ID to use
     */
    synchronized int getTransactionID() {
        /*
         * Ensure that the transaction ID is in the valid range between
         * 0 and MAX_TRANSACTION_ID (65534).  If not, the value will be forced
         * to 0.
         */
        if (transactionID < Modbus.DEFAULT_TRANSACTION_ID && isValidityCheck())
            transactionID = Modbus.DEFAULT_TRANSACTION_ID;
        if (transactionID >= Modbus.MAX_TRANSACTION_ID)
            transactionID = Modbus.DEFAULT_TRANSACTION_ID;
        return transactionID;
    }

    /**
     * A useful method for getting a random sleep time based on an increment of
     * the retry count and retry sleep time
     *
     * @param count Retry count
     * @return Random sleep time in milliseconds
     */
    long getRandomSleepTime(int count) {
        return (Modbus.RETRY_SLEEP_TIME / 2) +
                (long) (random.nextDouble() * Modbus.RETRY_SLEEP_TIME * count);
    }

}