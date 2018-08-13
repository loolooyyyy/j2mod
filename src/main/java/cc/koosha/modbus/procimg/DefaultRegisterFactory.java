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
 * @author Koosha Hosseiny
 */
@ThreadSafe
final class DefaultRegisterFactory implements RegisterFactory {

    @Override
    public DigitalIn createDigitalIn(boolean initialState) {
        return new SimpleDigitalIn(initialState);
    }

    @Override
    public DigitalOut createDigitalOut(boolean initialState) {
        return new SimpleDigitalOut(initialState);
    }

    @Override
    public InputRegister createInputRegister(byte hi, byte lo) {
        return new SimpleInputRegister(hi, lo);
    }

    @Override
    public InputRegister createInputRegister(int initialValue) {
        return new SimpleInputRegister(initialValue);
    }

    @Override
    public Register createRegister(byte hi, byte lo) {
        return new SimpleRegister(hi, lo);
    }

    @Override
    public Register createRegister(int initialValue) {
        return new SimpleRegister(initialValue);
    }

    @Override
    public Record record(int recordNumber, int registers) {
        return new SimpleRecord(recordNumber, registers);
    }

    @Override
    public FIFO fifo(int address, int maxSize) {
        return new SimpleFIFO(address, maxSize);
    }

    @Override
    public File file(int fileNumber, int records) {
        return new SimpleFile(fileNumber, records);
    }


    @Override
    public DigitalIn threadSafe(DigitalIn value) {
        return new SynchronizedDigitalIn(value);
    }

    @Override
    public DigitalOut threadSafe(DigitalOut value) {
        return new SynchronizedDigitalOut(value);
    }

    @Override
    public Register threadSafe(Register value) {
        return new SynchronizedRegister(value);
    }

    @Override
    public Record threadSafe(Record value) {
        return new SynchronizedRecord(value);
    }

    @Override
    public FIFO threadSafe(FIFO value) {
        return new SynchronizedFIFO(value);
    }

    @Override
    public File threadSafe(File value) {
        return new SynchronizedFile(value);
    }

}