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
package cc.koosha.modbus.util;


/**
 * A subscriber which can receive event of type E, and can subscribe to events
 * published by {@link Subscribable}.
 *
 * @param <S> type of source of the event.
 * @param <E> type of the event
 * @author Koosha Hosseiny
 * @see Subscribable
 */
public interface Subscriber<S, E> {

    /**
     * Called by {@link Subscribable} when an event occurs.
     *
     * @param source source of the event.
     * @param event  event being published.
     */
    void update(S source, E event);

}