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
import cc.koosha.modbus.io.net.TCPMasterConnection;
import cc.koosha.modbus.msg.response.ExceptionResponse;
import cc.koosha.modbus.xinternal.J2ModDebugUtils;
import cc.koosha.modbus.xinternal.J2ModUtils;
import com.cc.koosha.modbus.ModbusSlaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class implementing the <tt>ModbusTransaction</tt> interface.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public class ModbusTCPTransaction extends AbstractModbusTransaction {

    private static final Logger logger = LoggerFactory.getLogger(ModbusTCPTransaction.class);

    // instance attributes and associations
    private TCPMasterConnection connection;
    protected boolean reconnecting = Modbus.DEFAULT_RECONNECTING;

    /**
     * Constructs a new <tt>ModbusTCPTransaction</tt> instance with a given
     * <tt>TCPMasterConnection</tt> to be used for transactions.
     * <p>
     *
     * @param con a <tt>TCPMasterConnection</tt> instance.
     */
    public ModbusTCPTransaction(TCPMasterConnection con) {
        super(con.getTransport());
        connection = con;
    }

    /**
     * Tests if the connection will be opened and closed for <b>each</b>
     * execution.
     * <p>
     *
     * @return true if reconnecting, false otherwise.
     */
    public boolean isReconnecting() {
        return reconnecting;
    }

    /**
     * Sets the flag that controls whether a connection is opened and closed
     * for
     * <b>each</b> execution or not.
     * <p>
     *
     * @param b true if reconnecting, false otherwise.
     */
    // TODO set?
    public void setReconnecting(boolean b) {
        reconnecting = b;
    }

    @Override
    public synchronized void execute() throws ModbusException {

        if (getRequest() == null || connection == null) {
            throw new ModbusException("Invalid request or connection");
        }

        // Try sending the message up to retries time. Note that the message
        // is read immediately after being written, with no flushing of buffers.
        int retryCounter = 0;
        int retryLimit = (getRetries() > 0
                          ? getRetries()
                          : Modbus.DEFAULT_RETRIES);
        boolean keepTrying = true;

        // While we haven't exhausted all the retry attempts
        while (keepTrying) {

            // Automatically connect if we aren't already connected
            // TODO ensure transport is sete
            if (!connection.isConnected()) {
                try {
                    logger.debug("Connecting to: {}:{}", connection.getAddress()
                                                                   .toString(), connection
                                         .getPort());
                    connection.connect();
                    // setTransport(connection.getModbusTransport());
                }
                catch (Exception ex) {
                    throw new IOException("Connection failed for " +
                                                        connection.getAddress() + ":" +
                                                        connection.getPort(), ex);
                }
            }

            // Make sure the timeout is set
            getTransport().setTimeout(connection.getTimeout());

            try {

                // Write the message to the endpoint
                logger.debug("Writing request: {} (try: {}) request transaction ID = {} to {}:{}",
                             J2ModDebugUtils.toHex(getRequest()),
                             retryCounter, getRequest().getTransactionID(), connection
                                     .getAddress()
                                     .toString(), connection.getPort());
                getTransport().writeMessage(getRequest());

                // Read the response
                setResponse(getTransport().readResponse());
                logger.debug("Read response: {} (try: {}) response transaction ID = {} from {}:{}",
                             J2ModDebugUtils.toHex(getResponse()),
                             retryCounter, getResponse().getTransactionID(), connection
                                     .getAddress()
                                     .toString(), connection.getPort());
                keepTrying = false;

                // The slave may have returned an exception -- check for that.
                if (getResponse() instanceof ExceptionResponse) {
                    throw new ModbusSlaveException(((ExceptionResponse) getResponse())
                                                           .getExceptionCode());
                }

                // We need to keep retrying if;
                //   a) the response is empty OR
                //   b) we have been told to check the validity and the request/response transaction IDs don't match AND
                //   c) we haven't exceeded the maximum retry count
                if (responseIsInValid()) {
                    retryCounter++;
                    if (retryCounter >= retryLimit) {
                        throw new IOException("Executing transaction failed (tried " + retryLimit + " times)");
                    }
                    keepTrying = true;
                    long sleepTime = getRandomSleepTime(retryCounter);
                    if (getResponse() == null) {
                        logger.debug("Failed to get any response (try: {}) - retrying after {} milliseconds", retryCounter, sleepTime);
                    }
                    else {
                        logger.debug("Failed to get a valid response, transaction IDs do not match (try: {}) - retrying after {} milliseconds", retryCounter, sleepTime);
                    }
                    J2ModUtils.sleep(sleepTime);
                }
            }
            catch (IOException ex) {

                // Up the retry counter and check if we are exhausted
                retryCounter++;
                if (retryCounter >= retryLimit) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Executing transaction {} failed (tried " + retryLimit + " times): {}",
                                     J2ModDebugUtils.toHex(getRequest()),
                                     ex.getMessage());
                    }
                    throw new IOException("Executing transaction failed", ex);
                }
                else {
                    long sleepTime = getRandomSleepTime(retryCounter);
                    logger.debug("Failed transaction Request: {} (try: {}) - retrying after {} milliseconds",
                                 J2ModDebugUtils.toHex(getRequest()),
                                 retryCounter, sleepTime);
                    J2ModUtils.sleep(sleepTime);
                }

                // If this has happened, then we should close and re-open the connection before re-trying
                logger.debug("Failed request {} (try: {}) request transaction ID = {} - {} closing and re-opening connection {}:{}",
                             J2ModDebugUtils.toHex(getRequest()),
                             retryCounter, getRequest().getTransactionID(),
                             ex.getMessage(),
                             connection.getAddress().toString(),
                             connection.getPort());
                connection.close();
            }

            // Increment the transaction ID if we are still trying
            if (keepTrying) {
                incrementTransactionID();
            }
        }

        // Close the connection if it isn't supposed to stick around.
        if (isReconnecting()) {
            connection.close();
        }
        incrementTransactionID();
    }

    /**
     * Returns true if the response is not valid This can be if the response is
     * null or the transaction ID of the request doesn't match the reponse
     *
     * @return True if invalid
     */
    private boolean responseIsInValid() {
        if (getResponse() == null) {
            return true;
        }
        else if (!getResponse().isHeadless() && validityCheck) {
            return getRequest().getTransactionID() != getResponse().getTransactionID();
        }
        else {
            return false;
        }
    }

    /**
     * incrementTransactionID -- Increment the transaction ID for the next
     * transaction. Note that the caller must get the new transaction ID with
     * getTransactionID(). This is only done validity checking is enabled so
     * that dumb slaves don't cause problems. The original request will have its
     * transaction ID incremented as well so that sending the same transaction
     * again won't cause problems.
     */
    private synchronized void incrementTransactionID() {
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
