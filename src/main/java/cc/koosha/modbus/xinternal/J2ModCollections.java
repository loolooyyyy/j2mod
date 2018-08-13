package cc.koosha.modbus.xinternal;

import java.util.*;


/**
 * Utility class, Collection factory methods and collection goodies.
 * <p>
 * By using this class, it is easier to switch the collection implementation used by j2mod (ie from java's collection to
 * guava's).
 */
public final class J2ModCollections {

    private J2ModCollections() {
    }

    // -------------------------------------------------------------------------

    public static <E> Set<E> newModifiableSet() {
        return new HashSet<E>();
    }

    public static <E> Set<E> unmodifiable(Set<E> set) {
        return Collections.unmodifiableSet(set);
    }

    public static <E> Set<E> copy(Collection<E> set) {
        return Collections.unmodifiableSet(new HashSet<E>(set));
    }


    public static <E> Set<E> modifiableSetOf(E... elements) {
        HashSet<E> set = new HashSet<E>();
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> Set<E> setOf(E... elements) {
        HashSet<E> set = new HashSet<E>();
        Collections.addAll(set, elements);
        return Collections.unmodifiableSet(set);
    }

    public static <T> Set<T> singletonSet(T value) {
        return Collections.singleton(value);
    }

    // -------------------------------------------------------------------------

    public static <K, V> Map<K, V> newModifiableMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        return Collections.unmodifiableMap(new HashMap<K, V>(map));
    }


    public static <K, V> Map<K, V> newModifiableWeakMap() {
        return new WeakHashMap<K, V>();
    }

    public static <K, V> Hashtable<K, V> newHashTable(int initialCapacity) {
        return new Hashtable<K, V>(initialCapacity);
    }

    // -------------------------------------------------------------------------

    public static <E> List<E> newModifiableList() {
        return new ArrayList<E>();
    }

    public static <E> List<E> unmodifiable(List<E> list) {
        return Collections.unmodifiableList(list);
    }

    public static <E> List<E> copy(List<E> list) {
        return Collections.unmodifiableList(new ArrayList<E>(list));
    }

    public static <E> List<E> modifiableCopy(List<E> list) {
        return new ArrayList<E>(list);
    }

}
