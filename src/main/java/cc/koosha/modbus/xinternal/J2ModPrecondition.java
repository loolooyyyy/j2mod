package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Range;
import lombok.NonNull;

import java.util.Map;


/**
 * Precondition utility class, do range checks here, as assertions are not
 * really exceptional cases.
 * <p>
 * Makes code more readable
 * <p>
 * TODO 0 - Implement: implement.
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public final class J2ModPrecondition {

    private J2ModPrecondition() {
    }

    /**
     * @deprecated use {@link #ensureNotNull(Object, Object)}.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public static <T> T ensureNotNull(T reference) {
        return ensureNotNull(reference, "");
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     * <p>
     * From guava.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T ensureNotNull(T reference, Object errorMessage) {
        if (reference == null)
            throw new NullPointerException(String.valueOf(errorMessage));
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null. but throws an {@link IllegalStateException}
     * exception.
     * <p>
     * For instance: when a method should have been called or actions have been
     * taken to put object into a valid state, and if not, some field will be
     * null then.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws IllegalStateException if {@code reference} is null
     */
    public static <T> T ensureNotNullState(T reference, Object errorMessage) {
        if (reference == null)
            throw new IllegalStateException(String.valueOf(errorMessage));
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     * <p>
     * From guava.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static String ensureNotEmpty(String reference, Object errorMessage) {
        if (reference == null || reference.isEmpty())
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        return reference;
    }

    /**
     * Check value is positive (zero or more).
     *
     * @param value value to check
     * @return the value
     * @throws IllegalArgumentException if value is not greater than zero.
     */
    public static int ensurePositive(int value, String name) {
        if (value < 0)
            throw new IllegalArgumentException(name + " must be greater than zero: " + value);
        return value;
    }

    public static boolean isInRange(int i, int closedLow, int closedHigh) {
        return Range.closed(closedLow, closedHigh).contains(i);
    }

    public static int ensureIsInRange(int i,
                                      int closedLow,
                                      int closedHigh,
                                      Object errorMessage) {
        if (!Range.closed(closedLow, closedHigh).contains(i))
            throw new IllegalArgumentException(
                    errorMessage + "[" + closedHigh + "~" + closedHigh + "]: " + i);
        return i;
    }

    public static <T> void ensureCapacity(String name,
                                          @NonNull T[] ts,
                                          int len) {
        if (ts.length < len)
            throw new IllegalArgumentException(
                    name + " - array does not have enough capacity for: " + len
                            + " actual capacity=" + ts.length);
    }

    public static void ensureCapacity(String name,
                                      @NonNull byte[] ts,
                                      int len) {
        if (ts.length < len)
            throw new IllegalArgumentException(
                    name + " - array does not have enough capacity for: " + len +
                            " actual capacity=" + ts.length);
    }

    public static <T> boolean doesMapHaveRange(@NonNull Map<Integer, T> map,
                                               int ref,
                                               int count) {
        return Range.closed(ref, ref + count).containsAll(map.keySet());
    }

    // =================================================================== VALUE

    public static void ensureFitsInByte(int i, String name) {
        if (i < 0 || i > 255)
            throw new IllegalArgumentException(name + " - value does not fit in a byte: " + i);
    }

    public static void ensureFitsInShort(int i, String name) {
        if (i < 0 || i > 65535)
            throw new IllegalArgumentException(name + " - value does not fit in a short: " + i);
    }

    // ================================================================= ADDRESS

    /**
     * Check given address could be a valid value as an address.
     *
     * @param address the value to check.
     * @param name    indicated subject in error messages.
     * @return the value itself if it was valid.
     * @throws IllegalArgumentException if the value was not valid.
     */
    public static int ensureAddress(int address, String name) {
        if (address < 0)
            throw new IllegalAddressException(
                    name + " - invalid address, must be greater than zero: " + address);
        return address;
    }

    public static int ensureIsValidCount(int count, String name) {
        if (count < 0)
            throw new IllegalAddressException(
                    name + " - invalid count, must be greater than zero: " + count);
        return count;
    }

    /**
     * Check the element fetched from address is valid (that is, the address was
     * valid).
     * <p>
     * If the fetched element is null, then the address is invalid.
     *
     * @param fetched the element fetched for address.
     * @param address the address of the fetched element.
     * @param name    indicated subject in error messages.
     * @return the element itself if the address was valid.
     * @throws IllegalAddressException if the address was invalid (the fethced
     *                                 element is null).
     */
    @SuppressWarnings("ConstantConditions")
    public static <T> T ensureHadAddress(T fetched, int address, String name) {
        if (fetched == null) {
            throw new IllegalAddressException(
                    name + " - invalid address, no value for address=" + address);
        }
        return fetched;
    }

    /**
     * Ensures actualSize >= (ref + count)
     *
     * @param actualSize total number of available elements.
     * @param ref        base index.
     * @param count      number of elements requested.
     * @param name       indicated subject in error messages.
     */
    public static void ensureRefAndCountAreInLength(int actualSize,
                                                    int ref,
                                                    int count,
                                                    String name) {
        if (actualSize < 0)
            throw new IllegalArgumentException("size must be greater than zero: " + actualSize);
        if (ref < 0)
            throw new IllegalArgumentException("ref must be greater than zero: " + ref);
        if (count < 0)
            throw new IllegalArgumentException("count must be greater than zero: " + count);
        if (actualSize < ref + count)
            throw new IllegalAddressException(name + " - invalid address, "
                                                      + " total available=" + actualSize
                                                      + " requested range=" + ref + "~" + (ref + count - 1)
                                                      + " requested length=" + count);
    }

    /**
     * Check the requested address is in valid range.
     * <p>
     * The range is a closed range (lower and higher bounds are valid
     * themselves).
     *
     * @param address          the value to check.
     * @param closeLowerBound  minimum acceptable value for address.
     * @param closeHigherBound maximum acceptable value for address.
     * @param name             indicated subject in error messages.
     * @return the address itself if it was valid.
     */
    public static int ensureAddressIsInRange(int address,
                                             int closeLowerBound,
                                             int closeHigherBound,
                                             String name) {
        if (address < closeLowerBound || address > closeHigherBound)
            throw new IllegalAddressException(name + " - address out of range, "
                                                      + "acceptable range="
                                                      + closeHigherBound + "~" + closeHigherBound
                                                      + " requested address=" + address
            );
        return address;
    }

    /**
     * Check the requested address is in valid range, that is, the array index
     * exists.
     *
     * @param address requested index in array.
     * @param ts      source array .
     * @param name    indicated subject in error messages.
     * @return the address itself if it was valid.
     */
    public static <T> T ensureAddressIsInArray(int address,
                                               T[] ts,
                                               String name) {
        // TODO do not call self.
        ensureAddressIsInRange(address, 0, ts.length - 1, name);
        return ts[address];
    }

}
