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
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import cc.koosha.modbus.xinternal.J2ModThreadSafeSubscriptionManager;
import cc.koosha.modbus.xinternal.SubscriptionManager;


/**
 * {@inheritDoc}
 */
final class SimpleFile implements File {

    private final SubscriptionManager<File, ValueEvent> subscriptionManager =
            new J2ModThreadSafeSubscriptionManager<File, ValueEvent>(this);

    @Deprecated
    private final int fileNumber;
    private final Record records[];


    SimpleFile(int fileNumber, int records) {
        this.fileNumber = fileNumber;
        this.records = new Record[records];
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public int getFileNumber() {
        return fileNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRecordCount() {
        return records.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record getRecord(int i) {
        return J2ModPrecondition.ensureAddressIsInArray(i, records, "file record address");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File setRecord(int i, Record record) {
        J2ModPrecondition.ensureAddressIsInArray(i, records, "file record address");
        records[i] = record;
        return this;
    }

    @Override
    public int subscribe(Subscriber<File, ValueEvent> subscriber) {
        return subscriptionManager.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(int id) {
        return this.subscriptionManager.unsubscribe(id);
    }


    // TODO
    @Override
    public String toString() {
        return "FIFO{fileNumber=" + fileNumber + ", recordCount=" + records.length + "}";
    }

}
