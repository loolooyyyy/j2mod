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
package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.util.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.val;

import javax.annotation.concurrent.ThreadSafe;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.koosha.modbus.xinternal.J2ModCollections.newModifiableWeakMap;


/**
 * Thread Safety model is simple: synchronize all methods on a single lock
 * object.
 *
 * @author Koosha Hosseiny.
 */
@RequiredArgsConstructor
@ThreadSafe
public final class J2ModThreadSafeSubscriptionManager<S, E> implements SubscriptionManager<S, E> {

    private final Object LOCK = new Object();

    private final Map<WeakReference<Subscriber<S, E>>, Integer> subscribers = newModifiableWeakMap();
    private final AtomicInteger id = new AtomicInteger(0);
    private final S source;

    /**
     * {@inheritDoc}
     */
    @Synchronized("LOCK")
    @Override
    public int subscribe(Subscriber<S, E> subscriber) {
        int id = this.id.getAndIncrement();
        subscribers.put(new WeakReference<Subscriber<S, E>>(subscriber), id);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized("LOCK")
    @Override
    public boolean unsubscribe(int id) {
        return subscribers.values().remove(id);
    }

    /**
     * {@inheritDoc}
     */
    @Synchronized("LOCK")
    @Override
    public int removeAll() {
        val size = subscribers.size();
        subscribers.clear();
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Synchronized
    public void publish(E event) {
        final Map<WeakReference<Subscriber<S, E>>, Integer> copy;
        synchronized (LOCK) {
            copy = J2ModCollections.copy(this.subscribers);
        }
        for (WeakReference<Subscriber<S, E>> ref : copy.keySet())
            if (ref != null) {
                Subscriber<S, E> s = ref.get();
                if (s != null) {
                    s.update(source, event);
                }
            }
    }

}
