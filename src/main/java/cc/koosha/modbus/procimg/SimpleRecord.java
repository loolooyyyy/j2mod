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

import cc.koosha.modbus.xinternal.J2ModPrecondition;
import cc.koosha.modbus.util.Subscriber;
import cc.koosha.modbus.xinternal.SubscriptionManager;
import cc.koosha.modbus.xinternal.J2ModThreadSafeSubscriptionManager;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.concurrent.NotThreadSafe;


/**
 * @author Koosha Hosseiny.
 * @author Julie
 * <p>
 * File -- an abstraction of a Modbus Record, as supported by the
 * READ FILE RECORD and WRITE FILE RECORD commands.
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@NotThreadSafe
final class SimpleRecord implements Record {

    private final SubscriptionManager<Record, ValueEvent> subscriptionManager =
            new J2ModThreadSafeSubscriptionManager<Record, ValueEvent>(this);

    @Deprecated
    private final int recordNumber;
    private final Register registers[];


    SimpleRecord(int recordNumber, int registers) {
        this.recordNumber = recordNumber;
        this.registers = new Register[registers];

        for (int i = 0; i < registers; i++) {
            this.registers[i] = new SimpleRegister(0);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public int getRecordNumber() {
        return recordNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRegisterCount() {
        return registers.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Register getRegister(int register) {
        J2ModPrecondition.ensureAddressIsInRange(register, 0, registers.length - 1, "Register");
        return registers[register];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record setRegister(int ref, Register register) {
        J2ModPrecondition.ensureAddressIsInRange(ref, 0, registers.length - 1, "Register");
        registers[ref] = register;
        return this;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int subscribe(Subscriber<Record, ValueEvent> subscriber) {
        return subscriptionManager.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return subscriptionManager.unsubscribe(id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Record{recordNumber=" + recordNumber + ", recordCount=" + registers.length + "}";
    }

}
