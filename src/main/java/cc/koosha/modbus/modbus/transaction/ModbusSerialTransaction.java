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
import cc.koosha.modbus.ModbusException;
import java.io.IOException;
import com.cc.koosha.modbus.ModbusSlaveException;
import cc.koosha.modbus.xinternal.J2ModUtils;
import cc.koosha.modbus.modbus.transport.ModbusSerialTransport;
import cc.koosha.modbus.msg.response.ExceptionResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Class implementing the <tt>ModbusTransaction</tt> interface.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public final class ModbusSerialTransaction extends AbstractModbusTransaction {

    private final Object MUTEX = new Object();

    @Setter
    @Getter
    // TODO configurable
    private int transDelayMS = Modbus.DEFAULT_TRANSMIT_DELAY;

    private long lastTransactionTimestamp = 0;

    public ModbusSerialTransaction(ModbusSerialTransport transport) {
        super(transport);
    }

    /**
     * Asserts if this <tt>ModbusTCPTransaction</tt> is executable.
     *
     * @throws ModbusException if the transaction cannot be asserted.
     */
    private void assertExecutable() throws ModbusException {
        if (getRequest() == null || getTransport() == null) {
            throw new ModbusException("Assertion failed, transaction not executable");
        }
    }

    @Override
    public void execute() throws ModbusException {
        //1. assert executeability
        assertExecutable();

        //3. write request, and read response,
        //   while holding the lock on the IO object
        int tries = 0;
        boolean finished = false;
        do {
            try {
                // Wait between adjacent requests
                ((ModbusSerialTransport) getTransport()).waitBetweenFrames(
                        transDelayMS, lastTransactionTimestamp);

                synchronized (MUTEX) {
                    //write request message
                    getTransport().writeMessage(getRequest());
                    //read response message
                    setResponse(getTransport().readResponse());
                    finished = true;
                }
            }
            catch (IOException e) {
                if (++tries >= getRetries()) {
                    throw e;
                }
                J2ModUtils.sleep(getRandomSleepTime(tries));
                log.debug("Execute try {} error: {}", tries, e.getMessage());
            }
        } while (!finished);

        //4. deal with exceptions
        if (getResponse() instanceof ExceptionResponse) {
            throw new ModbusSlaveException(((ExceptionResponse) getResponse()).getExceptionCode());
        }

        if (isCheckingValidity()) {
            checkValidity();
        }
        //toggle the id
        toggleTransactionID();

        // Set the last transaction timestamp
        lastTransactionTimestamp = System.currentTimeMillis();
    }

    /**
     * Checks the validity of the transaction, by checking if the values of the
     * response correspond to the values of the request.
     *
     * @throws ModbusException if the transaction is not valid.
     */
    private void checkValidity() throws ModbusException {

    }

    /**
     * Toggles the transaction identifier, to ensure that each transaction has a
     * distinctive identifier.<br> When the maximum value of 65535 has been
     * reached, the identifiers will start from zero again.
     */
    private void toggleTransactionID() {
        if (isCheckingValidity()) {
            if (transactionID == (Short.MAX_VALUE * 2)) {
                transactionID = 0;
            }
            else {
                transactionID++;
            }
        }
        getRequest().setTransactionID(getTransactionID());
    }

}
