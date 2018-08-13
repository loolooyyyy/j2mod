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

import cc.koosha.modbus.util.Subscriber;
import cc.koosha.modbus.xinternal.SubscriptionManager;
import cc.koosha.modbus.xinternal.J2ModThreadSafeSubscriptionManager;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.LinkedList;
import java.util.List;


/**
 * {@inheritDoc}
 */
@NotThreadSafe
final class SimpleFIFO implements FIFO {

    private static final int DEFAULT_MAX_SIZE = 31;

    private final SubscriptionManager<FIFO, ValueEvent> subscriptionManager =
            new J2ModThreadSafeSubscriptionManager<FIFO, ValueEvent>(this);

    @Deprecated
    private final int address;
    private final int maxSize;
    private final LinkedList<Register> registers = new LinkedList<Register>();


    SimpleFIFO(int address) {
        this.address = address;
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    SimpleFIFO(int address, int maxSize) {
        this.address = address;
        this.maxSize = maxSize;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public int getAddress() {
        return this.address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public int getMaxSize() {
        return this.maxSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRegisterCount() {
        return this.registers.size();
    }

    /**
     * TODO make copy of each register.
     */
    @Override
    public List<Register> getRegisters() {
        LinkedList<Register> copy = new LinkedList<Register>(registers);
        copy.addFirst(new SimpleRegister(copy.size()));
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushRegister(Register register) {
        if (registers.size() == maxSize) {
            registers.removeFirst();
        }
        registers.push(new SimpleRegister(register.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetRegisters() {
        registers.clear();
    }


    @Override
    public int subscribe(Subscriber<FIFO, ValueEvent> subscriber) {
        return subscriptionManager.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return this.subscriptionManager.unsubscribe(id);
    }


    @Override
    public String toString() {
        return "FIFO{maxSize=" + maxSize + ", address=" + address + ", registerCount=" + getRegisterCount() + "}";
    }

}
