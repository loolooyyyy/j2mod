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
import cc.koosha.modbus.io.net.AbstractUDPTerminal;
import cc.koosha.modbus.msg.response.ExceptionResponse;
import cc.koosha.modbus.xinternal.J2ModUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * Class implementing the <tt>ModbusTransaction</tt>
 * interface for the UDP transport mechanism.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@Slf4j
public class ModbusUDPTransaction extends AbstractModbusTransaction {

    //instance attributes and associations
    private AbstractUDPTerminal terminal;
    private final Object MUTEX = new Object();

    /**
     * Constructs a new <tt>ModbusUDPTransaction</tt>
     * instance.
     */
    public ModbusUDPTransaction(AbstractUDPTerminal terminal) {
        super(terminal.getTransport());
        this.terminal = terminal;
    }

    @Override
    public void execute() throws IOException, ModbusSlaveException, ModbusException {

        //1. assert executeability
        assertExecutable();
        //2. open the connection if not connected
        // TODO ensure connection is open, and super transport is already set.
        if (!terminal.isActive()) {
            try {
                terminal.activate();
            }
            catch (Exception ex) {
                log.debug("Terminal activation failed.", ex);
                throw new IOException("Activation failed");
            }
        }

        //3. Retry transaction retries times, in case of
        //I/O Exception problems.
        int retryCount = 0;
        while (retryCount <= getRetries()) {
            try {
                //3. write request, and read response,
                //   while holding the lock on the IO object
                synchronized (MUTEX) {
                    //write request message
                    getTransport().writeMessage(getRequest());
                    //read response message
                    setResponse(getTransport().readResponse());
                    break;
                }
            }
            catch (IOException ex) {
                retryCount++;
                if (retryCount > getRetries()) {
                    log.error("Cannot send UDP message", ex);
                }
                else {
                    J2ModUtils.sleep(getRandomSleepTime(retryCount));
                }
            }
        }

        //4. deal with "application level" exceptions
        if (getResponse() instanceof ExceptionResponse) {
            throw new ModbusSlaveException(((ExceptionResponse)getResponse()).getExceptionCode());
        }

        //toggle the id
        incrementTransactionID();
    }

    /**
     * Asserts if this <tt>ModbusTCPTransaction</tt> is
     * executable.
     *
     * @throws ModbusException if this transaction cannot be
     *                         asserted as executable.
     */
    private void assertExecutable() throws ModbusException {
        if (getRequest() == null || terminal == null) {
            throw new ModbusException("Assertion failed, transaction not executable");
        }
    }

    /**
     * Toggles the transaction identifier, to ensure
     * that each transaction has a distinctive
     * identifier.<br>
     * When the maximum value of 65535 has been reached,
     * the identifiers will start from zero again.
     */
    private void incrementTransactionID() {
        if (isCheckingValidity()) {
            if (transactionID >= Modbus.MAX_TRANSACTION_ID) {
                transactionID = Modbus.DEFAULT_TRANSACTION_ID;
            }
            else {
                transactionID++;
            }
        }
        getRequest().setTransactionID(getTransactionID());
    }
}