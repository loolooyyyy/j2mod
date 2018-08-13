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
package cc.koosha.modbus;

import lombok.experimental.UtilityClass;


/**
 * Defining all constants related to the Modbus protocol.
 * <p>
 *
 * @author Koosha Hosseiny.
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
@UtilityClass
public class Modbus {

    /**
     * Defines the class 1 function code for <tt>read coils</tt>.
     */
    public static final int READ_COILS = 1;

    /**
     * Defines a class 1 function code for <tt>read input discretes</tt>.
     */
    public static final int READ_INPUT_DISCRETES = 2;

    /**
     * Defines a class 1 function code for <tt>read holding registers</tt>
     */
    public static final int READ_HOLDING_REGISTERS = 3;

    /**
     * Defines the class 0 function code for <tt>read multiple registers</tt>.
     * The proper name is "Read Holding Registers".
     */
    public static final int READ_MULTIPLE_REGISTERS = 3;

    /**
     * Defines a class 1 function code for <tt>read input registers</tt>.
     */
    public static final int READ_INPUT_REGISTERS = 4;

    /**
     * Defines a class 1 function code for <tt>write coil</tt>.
     */
    public static final int WRITE_COIL = 5;

    /**
     * Defines a class 1 function code for <tt>write single register</tt>.
     */
    public static final int WRITE_SINGLE_REGISTER = 6;

    /**
     * <tt>read exception status</tt>
     * <p>
     * Serial devices only.
     */
    public static final int READ_EXCEPTION_STATUS = 7;

    /**
     * <tt>get serial diagnostics</tt>
     * <p>
     * Serial devices only.
     */
    public static final int READ_SERIAL_DIAGNOSTICS = 8;

    /**
     * <tt>get comm event counter</tt>
     * <p>
     * Serial devices only.
     */
    public static final int READ_COMM_EVENT_COUNTER = 11;

    /**
     * <tt>get comm event log</tt>
     * <p>
     * Serial devices only.
     */
    public static final int READ_COMM_EVENT_LOG = 12;

    /**
     * Defines a standard function code for <tt>write multiple coils</tt>.
     */
    public static final int WRITE_MULTIPLE_COILS = 15;

    /**
     * Defines the class 0 function code for <tt>write multiple registers</tt>.
     */
    public static final int WRITE_MULTIPLE_REGISTERS = 16;

    /**
     * Defines a standard function code for <tt>read slave ID</tt>.
     */
    public static final int REPORT_SLAVE_ID = 17;

    /**
     * <tt>read file record</tt>
     */
    public static final int READ_FILE_RECORD = 20;

    /**
     * <tt>write file record</tt>
     */
    public static final int WRITE_FILE_RECORD = 21;

    /**
     * <tt>mask write register</tt>
     * <p>
     * Update a single register using its current value and an AND and OR mask.
     */
    public static final int MASK_WRITE_REGISTER = 22;

    /**
     * <tt>read / write multiple registers</tt>
     * <p>
     * Write some number of registers, then read some number of potentially
     * other registers back.
     */
    public static final int READ_WRITE_MULTIPLE = 23;

    /**
     * <tt>read FIFO queue</tt>
     * <p>
     * Read from a FIFO queue.
     */
    public static final int READ_FIFO_QUEUE = 24;

    /**
     * Defines the function code for reading encapsulated data, such as vendor
     * information.
     */
    public static final int READ_MEI = 43;
    public static final int READ_MEI_VENDOR_INFO = 14;

    /**
     * Defines the byte representation of the coil state <b>on</b>.
     */
    public static final int COIL_ON = (byte) 255;

    /**
     * Defines the byte representation of the coil state <b>pos</b>.
     */
    public static final int COIL_OFF = 0;

    /**
     * Defines the word representation of the coil state <b>on</b>.
     */
    public static byte[] coilOnBytes() {
        return new byte[]{COIL_ON, COIL_OFF};
    }

    /**
     * Defines the word representation of the coil state <b>pos</b>.
     */
    public static byte[] coilOffBytes() {
        return new byte[]{COIL_OFF, COIL_OFF};
    }

    /**
     * Defines the maximum number of bits in multiple read/write of input
     * discretes or coils (<b>2000</b>).
     */
    public static final int MAX_BITS = 2000;

    /**
     * Defines the Modbus slave exception offset that is added to the function
     * code, to flag an exception.
     */
    public static final int EXCEPTION_OFFSET = 128;            //the last valid function code is 127

    /**
     * Defines the Modbus slave exception type <tt>illegal function</tt>. This
     * exception code is returned if the slave:
     * <ul>
     * <li>does not implement the function code <b>or</b></li>
     * <li>is not in a state that allows it to process the function</li>
     * </ul>
     */
    public static final int ILLEGAL_FUNCTION_EXCEPTION = 1;

    /**
     * Defines the Modbus slave exception type <tt>illegal data address</tt>.
     * This exception code is returned if the reference:
     * <ul>
     * <li>does not exist on the slave <b>or</b></li>
     * <li>the combination of reference and length exceeds the bounds
     * of the existing registers.
     * </li>
     * </ul>
     */
    public static final int ILLEGAL_ADDRESS_EXCEPTION = 2;

    /**
     * Defines the Modbus slave exception type <tt>illegal data value</tt>. This
     * exception code indicates a fault in the structure of the data values of a
     * complex request, such as an incorrect implied length.<br>
     * <b>This code does not indicate a problem with application specific
     * validity of the value.</b>
     */
    public static final int ILLEGAL_VALUE_EXCEPTION = 3;

    /**
     * Defines the Modbus slave exception type <tt>slave device failure</tt>.
     * This exception code indicates a fault in the slave device itself.
     */
    public static final int SLAVE_DEVICE_FAILURE = 4;

    /**
     * Defines the Modbus slave exception type <tt>slave busy</tt>.  This
     * exception indicates the the slave is unable to perform the operation
     * because it is performing an operation which cannot be interrupted.
     */
    public static final int SLAVE_BUSY_EXCEPTION = 6;

    /**
     * Defines the Modbus slave exception type <tt>negative acknowledgment</tt>.
     * This exception code indicates the slave cannot perform the requested
     * action.
     */
    public static final int NEGATIVE_ACKNOWLEDGEMENT = 7;

    /**
     * Defines the Modbus slave exception type <tt>Gateway target failed to
     * respond</tt>.  This exception code indicates that a Modbus gateway failed
     * to receive a response from the specified target.
     */
    public static final int GATEWAY_TARGET_NO_RESPONSE = 11;

    /**
     * Defines the default port number of Modbus (=<tt>502</tt>).
     */
    public static final int DEFAULT_PORT = 502;

    /**
     * Defines the maximum message length in bytes (=<tt>256</tt>).
     */
    public static final int MAX_MESSAGE_LENGTH = 256;

    /**
     * Defines the default transaction identifier (=<tt>0</tt>).
     */
    public static final int DEFAULT_TRANSACTION_ID = 0;

    /**
     * Defines the default protocol identifier (=<tt>0</tt>).
     */
    public static final int DEFAULT_PROTOCOL_ID = 0;

    /**
     * Defines the default unit identifier (=<tt>0</tt>).
     */
    public static final int DEFAULT_UNIT_ID = 0;

    /**
     * Defines the default setting for validity checking in transactions
     * (=<tt>true</tt>).
     */
    public static final boolean DEFAULT_VALIDITYCHECK = true;

    /**
     * Defines the default setting for I/O operation timeouts in milliseconds
     * (=<tt>3000</tt>).
     */
    public static final int DEFAULT_TIMEOUT = 3000;

    /**
     * Defines the sleep period between transaction retries in milliseconds
     * (=<tt>200</tt>).
     */
    public static final int RETRY_SLEEP_TIME = 500;

    /**
     * Defines the default reconnecting setting for transactions
     * (=<tt>false</tt>).
     */
    public static final boolean DEFAULT_RECONNECTING = false;

    /**
     * Defines the default amount of retires for opening a connection
     * (=<tt>3</tt>).
     */
    public static final int DEFAULT_RETRIES = 5;

    /**
     * Defines the default number of msec to delay before transmission<br>
     * Inter-message delays are managed by the SerialTransaction object
     * automatically based on the baud rate. Setting this value to anything
     * other than zero will bypass that process and force a specific
     * inter-message delay (=<tt>0</tt>).
     */
    public static final int DEFAULT_TRANSMIT_DELAY = 0;

    /**
     * Defines the default number of msec to delay before transmission if not
     * overridden by DEFAULT_TRANSMIT_DELAY (=<tt>2</tt>).
     */
    public static final int MINIMUM_TRANSMIT_DELAY = 2;

    /**
     * The number of characters delay that must be maintained between adjacent
     * requests on the same serial port (within the same transaction)
     */
    public static final double INTER_MESSAGE_GAP = 4;

    /**
     * Defines the maximum value of the transaction identifier.
     *
     * <p><b>Note:</b> The standard requires that the server copy whatever
     * value the client provides. However, the transaction ID is being limited
     * to signed 16-bit integers to prevent problems with servers that might
     * incorrectly assume the value is a signed value.
     */
    public static final int MAX_TRANSACTION_ID = Short.MAX_VALUE;

    /**
     * Defines the serial encoding "ASCII".
     */
    public static final String SERIAL_ENCODING_ASCII = "ascii";

    /**
     * Defines the serial encoding "RTU".
     */
    public static final String SERIAL_ENCODING_RTU = "rtu";

    /**
     * Defines the default serial encoding (ASCII).
     */
    public static final String DEFAULT_SERIAL_ENCODING = SERIAL_ENCODING_ASCII;


    /**
     * Defines a virtual number for the FRAME START token (COLON).
     */
    public static final int ASCII_FRAME_START_FLAG = Integer.MAX_VALUE - 3;

    /**
     * Defines a virtual number for the FRAME_END token (CR LF).
     */
    public static final int ASCII_FRAME_END_FLAG = Integer.MAX_VALUE - 2;

    public static final byte ASCII_FRAME_START = 58;
    public static final byte ASCII_FRAME_END_0 = 13;
    public static final byte ASCII_FRAME_END_1 = 10;

}