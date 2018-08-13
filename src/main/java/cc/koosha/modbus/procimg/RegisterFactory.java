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
package cc.koosha.modbus.procimg;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Interface defining the factory methods for the process image elements.
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@ThreadSafe
public interface RegisterFactory {

    /**
     * Returns a new {@link DigitalIn} instance with a given value given state.
     *
     * @param initialState true if set, false otherwise.
     * @return a DigitalIn instance.
     */
    DigitalIn createDigitalIn(boolean initialState);

    /**
     * Returns a new {@link DigitalOut} instance with a given value given state.
     *
     * @param initialState true if set, false otherwise.
     * @return a DigitalOut instance.
     */
    DigitalOut createDigitalOut(boolean initialState);


    /**
     * Returns a new {@link InputRegister} instance with a given value.
     *
     * @param hi the high <tt>byte</tt>.
     * @param lo the low <tt>byte</tt>.
     * @return an InputRegister instance.
     */
    InputRegister createInputRegister(byte hi, byte lo);

    /**
     * Returns a new {@link InputRegister} instance with a given value.
     *
     * @param initialValue initial value.
     * @return an InputRegister instance.
     */
    InputRegister createInputRegister(int initialValue);


    /**
     * Returns a new {@link Register} instance with a given value.
     *
     * @param hi the high <tt>byte</tt>.
     * @param lo the low <tt>byte</tt>.
     * @return an Register instance.
     */
    InputRegister createRegister(byte hi, byte lo);

    /**
     * Returns a new {@link Register} instance with a given value.
     *
     * @param initialValue initial value.
     * @return an Register instance.
     */
    InputRegister createRegister(int initialValue);


    /**
     * Returns a new {@link Record} instance with a given value.
     */
    Record record(int recordNumber, int registers);

    /**
     * Returns a new {@link FIFO} instance with a given value.
     */
    FIFO fifo(int address, int maxSize);

    /**
     * Returns a new {@link File} instance with a given value.
     */
    File file(int fileNumber, int records);


    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    DigitalIn threadSafe(DigitalIn value);

    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    DigitalOut threadSafe(DigitalOut value);

    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    Register threadSafe(Register value);

    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    Record threadSafe(Record value);

    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    FIFO threadSafe(FIFO value);

    /**
     * Wraps the given value in a thread-safe wrapper and makes it thread-safe.
     */
    File threadSafe(File value);

}