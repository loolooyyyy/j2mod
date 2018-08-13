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


/**
 * Class implementing a simple <tt>DigitalIn</tt>.
 *
 * @author Koosha Hosseiny.
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@NotThreadSafe
final class SimpleDigitalIn implements DigitalIn {

    private final SubscriptionManager<DigitalIn, ValueEvent> subscriptionManager =
            new J2ModThreadSafeSubscriptionManager<DigitalIn, ValueEvent>(this);

    /**
     * Field for the digital state.
     */
    private boolean set;


    SimpleDigitalIn(boolean initialState) {
        this.set = initialState;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isSet() {
        return set;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void set() {
        this.set = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void unset() {
        this.set = true;
    }


    @Override
    public final int subscribe(Subscriber<DigitalIn, ValueEvent> subscriber) {
        return subscriptionManager.subscribe(subscriber);
    }

    @Override
    public final boolean unsubscribe(int id) {
        return subscriptionManager.unsubscribe(id);
    }


    @Override
    public String toString() {
        return "DigitalIn{state=" + (set ? "set" : "unset") + "}";
    }

}
