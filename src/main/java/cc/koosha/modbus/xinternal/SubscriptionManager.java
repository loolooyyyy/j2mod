package cc.koosha.modbus.xinternal;


import cc.koosha.modbus.util.Subscribable;
import cc.koosha.modbus.util.Subscriber;


/**
 * A helper class, acting as a manager for {@link Subscribable}, so the {@link
 * Subscribable} does not need to deal with details of subscription. and the
 * details of subscription management is hidden from outside the object.
 * <p>
 * Order of calling subscribers, thread safety, and parallelism depends on the
 * implementation. This class does not send any particular message to the
 * subscribers other than message source.
 * <p>
 * Each subscriber is assigned an ID and it is used to unsubscribe the
 * subscriber. A hard reference is <b>NOT</b> kept to subscribers, only a {@link
 * java.lang.ref.WeakReference} is held to them.
 *
 * @param <S> type of message source.
 * @param <E> type of the event.
 * @author Koosha Hosseiny.
 * @see Subscribable
 */
public interface SubscriptionManager<S, E> {

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
     *
     * <p>
     * ID is assigned by {@link #subscribe(Subscriber)}.
     *
     * @param id id of subscriber being unsubscribed.
     * @return true if there was such subscriber, false otherwise.
     * @see #subscribe(Subscriber) (Subscriber)
     */
    boolean unsubscribe(int id);

    /**
     * Unsubscribe all subscribers.
     *
     * @return number of subscribers removed.
     */
    int removeAll();

    /**
     * publish an event to all subscribers.
     */
    void publish(E event);

}
