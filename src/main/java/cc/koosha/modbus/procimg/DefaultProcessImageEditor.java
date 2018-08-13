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

import lombok.RequiredArgsConstructor;


/**
 * Class implementing a simple process image to be able to run unit tests or
 * handle simple cases.
 *
 * <p>
 * The image has a simple linear address space for, analog, digital and file
 * objects. Holes may be created by adding a object with a reference after the
 * last object reference of that type.
 * <p>
 *
 * @author Dieter Wimberger
 * @author Julie Added support for files of records.
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@RequiredArgsConstructor
final class DefaultProcessImageEditor implements ProcessImageEditor {

    private final DefaultProcessImage processImage;

    @Override
    public void setDigitalOut(int ref, DigitalOut value) {
        processImage.getDigitalOuts().set(ref, value);
    }

    @Override
    public void addDigitalOut(DigitalOut value) {
        processImage.getDigitalOuts().add(value);
    }

    @Override
    public void addDigitalOut(int ref, DigitalOut value) {
        processImage.getDigitalOuts().put(ref, value);
    }

    @Override
    public void removeDigitalOut(DigitalOut value) {
        processImage.getDigitalOuts().remove(value);
    }


    // --------------------

    @Override
    public void setDigitalIn(int ref, DigitalIn value) {
        processImage.getDigitalIns().set(ref, value);
    }

    @Override
    public void addDigitalIn(DigitalIn value) {
        processImage.getDigitalIns().add(value);
    }

    @Override
    public void addDigitalIn(int ref, DigitalIn value) {
        processImage.getDigitalIns().put(ref, value);
    }

    @Override
    public void removeDigitalIn(DigitalIn value) {
        processImage.getDigitalIns().remove(value);
    }


    // --------------------

    @Override
    public void setInputRegister(int ref, InputRegister value) {
        processImage.getInputRegisters().set(ref, value);
    }

    @Override
    public void addInputRegister(InputRegister value) {
        processImage.getInputRegisters().add(value);
    }

    @Override
    public void addInputRegister(int ref, InputRegister value) {
        processImage.getInputRegisters().put(ref, value);
    }

    @Override
    public void removeInputRegister(InputRegister value) {
        processImage.getInputRegisters().remove(value);
    }

    // --------------------

    @Override
    public void setRegister(int ref, Register value) {
        processImage.getRegisters().set(ref, value);
    }

    @Override
    public void addRegister(Register value) {
        processImage.getRegisters().add(value);
    }

    @Override
    public void addRegister(int ref, Register value) {
        processImage.getRegisters().put(ref, value);
    }

    @Override
    public void removeRegister(Register value) {
        processImage.getRegisters().remove(value);
    }

    // --------------------

    @Override
    public void setFile(int ref, File value) {
        processImage.getFiles().set(ref, value);
    }

    @Override
    public void addFile(File value) {
        processImage.getFiles().add(value);
    }

    @Override
    public void addFile(int ref, File value) {
        processImage.getFiles().put(ref, value);
    }

    @Override
    public void removeFile(File value) {
        processImage.getFiles().remove(value);
    }

    // --------------------

    @Override
    public void setFIFO(int ref, FIFO value) {
        processImage.getFifos().set(ref, value);
    }

    @Override
    public void addFIFO(FIFO value) {
        processImage.getFifos().add(value);
    }

    @Override
    public void addFIFO(int ref, FIFO value) {
        processImage.getFifos().put(ref, value);
    }

    @Override
    public void removeFIFO(FIFO value) {
        processImage.getFifos().remove(value);
    }

}
