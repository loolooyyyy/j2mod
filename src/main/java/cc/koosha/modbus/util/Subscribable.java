package cc.koosha.modbus.util;


/**
 * Represents a type which can be subscribed to, which publishes events of type E.
 *
 * @param <S> self (source).
 * @param <E> event type.
 * @see Subscriber
 */
public interface Subscribable<S extends Subscribable<S, E>, E> {

    /**
     * Adds a subscriber to the message source, and assign it an ID.
     *
     * @param subscriber subscriber being added to the source.
     * @return the id assigned to subscriber.
     * @see #unsubscribe(int)
     */
    int subscribe(Subscriber<S, E> subscriber);

    /**
     * Removes a subscriber from the message source by its ID.
     * <p>
     * ID is assigned by {@link #subscribe(Subscriber)}.
     *
     * @param id id of subscriber being unsubscribed.
     * @return true if there was such subscriber, false otherwise.
     * @see #subscribe(Subscriber) (Subscriber)
     */
    boolean unsubscribe(int id);

}
