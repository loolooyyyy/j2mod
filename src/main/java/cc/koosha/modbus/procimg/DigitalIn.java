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


/**
 * Interface defining a digital input.
 * <p>
 * In Modbus terms this represents an input discrete, it is read only from the
 * slave side. see the documentation of * {@link #set()} and {@link #unset()}.
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface DigitalIn extends DigitalRegister<DigitalIn> {

    /**
     * Sets the state of this digital register to high (true).
     *
     * <b>NOTE</b>:  According to modbus contract, and for complying with it,
     * this method should only be used from master/device side.
     *
     * @see #unset()
     */
    @Override
    void set();

    /**
     * Sets the state of this digital register to low (false).
     *
     * <b>NOTE</b>:  According to modbus contract, and for complying with it,
     * this method should only be used from master/device side.
     *
     * @see #set()
     */
    @Override
    void unset();

}

