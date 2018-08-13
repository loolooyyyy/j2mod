package cc.koosha.modbus.xinternal;


import java.nio.charset.Charset;


/**
 * Utility class, Collection factory methods and collection goodies.
 * <p>
 * By using this class, it is easier to switch the collection implementation
 * used by j2mod (ie from java's collection to guava's).
 * <p>
 * TODO 0 - Implement: implement.
 */
public final class J2ModUtils {

    private J2ModUtils() {

    }

    public static Charset ASCII = Charset.forName("US-ASCII");

    /**
     * Sleeps safely for the specified amount of time unless awoken by an
     * interruption
     *
     * @param time Time in milliseconds
     * @return false if thread was interrupted, true otherwise (aka, sleep was
     * successful).
     */
    public static boolean sleep(long time) {
        try {
            Thread.sleep(time);
            return true;
        }
        catch (InterruptedException ex) {
            // TODO ?
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Return true if the string is null or empty
     *
     * @param value String to check
     * @return True if the value is blank or empty
     */
    public static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Return true if the array is null or empty
     *
     * @param list Array to check
     * @return True if the array is blank or empty
     */
    public static boolean isBlank(Object[] list) {
        return list == null || list.length == 0;
    }


    public static J2ModFastOutputStream dataOutput(int size) {
        return new J2ModFastOutputStream(size);
    }

    public static J2ModFastOutputStream dataOutput(byte[] buf) {
        return new J2ModFastOutputStream(buf);
    }

    public static J2ModFastInputStream dataInput(int size) {
        return new J2ModFastInputStream(size);
    }

    public static J2ModFastInputStream dataInput(byte[] buf) {
        return new J2ModFastInputStream(buf);
    }

}
