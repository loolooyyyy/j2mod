package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.koosha.modbus.xinternal.J2ModCollections.newModifiableMap;


@RequiredArgsConstructor
@ThreadSafe
public final class SynchronizedStorage<V> {

    @NonNull
    private final String name;

    /**
     * Whether if overriding must be treated strictly, that is, it is an error
     * to 1. replace a non-existing index (it must be added) or 2. adding with
     * an index, while that index already exists (it must be replaced instead).
     */
    private final boolean strictOverride;

    private final int minIndex;
    private final int maxIndex;

    private final Map<Integer, V> storage = newModifiableMap();
    private final AtomicInteger addIndex = new AtomicInteger(0);

    @Synchronized
    public void add(V value) {
        J2ModPrecondition.ensureAddressIsInRange(addIndex.get(), minIndex, maxIndex, name);
        storage.put(addIndex.getAndIncrement(), value);
    }

    @Synchronized
    public V set(Integer key, V value) {
        J2ModPrecondition.ensureHadAddress(storage.get(key), key, name);
        if (strictOverride && !storage.containsKey(key))
            throw new IllegalAddressException("non-existing address: " + key);
        return storage.put(key, value);
    }

    @Synchronized
    public V get(Integer key) {
        if (strictOverride && !storage.containsKey(key))
            throw new IllegalAddressException("does not exist: " + key);
        return storage.get(key);
    }

    @Synchronized
    public List<V> getRange(int ref, int count) {
        final List<V> list = new ArrayList<V>();
        for (int i = ref; i < ref + count; i++)
            list.add(storage.get(i));
        return list;
    }

    @Synchronized
    public V find(Predicate<V> p) {
        for (V v : storage.values())
            if (p.test(v))
                return v;
        return null;
    }

    @Synchronized
    public void put(Integer key, V value) {
        J2ModPrecondition.ensureAddressIsInRange(key, minIndex, maxIndex, name);
        if (strictOverride && storage.containsKey(key))
            throw new IllegalAddressException("duplicate address: " + key);
        storage.put(key, value);
    }

    @Synchronized
    public void remove(V value) {
        if (strictOverride && !storage.containsValue(value))
            throw new IllegalAddressException("non-existing value: " + value);
        storage.values().remove(value);
    }

    @Synchronized
    public int size() {
        return storage.size();
    }

}
