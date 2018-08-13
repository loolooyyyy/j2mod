package cc.koosha.modbus.xinternal;


import lombok.experimental.UtilityClass;
import lombok.val;


@UtilityClass
public class J2ModDataUtil {

    public static int twoBytesToInt(final byte[] bytes, final int offset) {
        return ((bytes[offset] & 0xff) << 8 | (bytes[offset + 1] & 0xff));
    }

    public static short twoBytesToShort(final byte[] bytes, final int offset) {
        return (short) (bytes[offset] << 8 | (bytes[offset + 1] & 0xff));
    }

    public static byte loByte(short s) {
        return (byte) (0xff & s);
    }

    public static byte hiByte(short s) {
        return (byte) (0xff & (s >> 8));
    }

    public static byte loByte(int s) {
        return (byte) (0xff & s);
    }

    public static byte hiByte(int s) {
        return (byte) (0xff & (s >> 8));
    }

    /**
     * Converts the register (a 16 bit value) into an unsigned short. The value
     * returned is:
     *
     * <pre><code>(((a &amp; 0xff) &lt;&lt; 8) | (b &amp; 0xff))</code></pre>
     * <p>
     * This conversion has been taken from the documentation of the
     * <tt>DataInput</tt> interface.
     * <p>
     *
     * @param bytes a register as <tt>byte[2]</tt>.
     * @return the unsigned short value as <tt>int</tt>.
     * @see java.io.DataInput
     */
    public static int registerValueToUnsignedShort(final byte[] bytes, final int offset) {
        return ((bytes[offset] & 0xff) << 8 | (bytes[offset + 1] & 0xff));
    }

    /**
     * Converts the given unsigned short into a register (2 bytes). The byte
     * values in the register, in the  order shown, are:
     *
     * <pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 8))
     * (byte)(0xff &amp; v)
     * </code></pre>
     * <p>
     * This conversion has been taken from the documentation of the
     * <tt>DataOutput</tt> interface.
     * <p>
     *
     * @param v Value to convert
     * @return the register as <tt>byte[2]</tt>.
     * @see java.io.DataOutput
     */
    public static byte[] unsignedShortToRegister(int v) {
        byte[] register = new byte[2];
        register[0] = (byte) (0xff & (v >> 8));
        register[1] = (byte) (0xff & v);
        return register;
    }

    /**
     * Converts the register (16-bit value) at the given index into a
     * <tt>short</tt>. The value returned is:
     *
     * <pre><code>
     * (short)((a &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * <p>
     * <p>
     * This conversion has been taken from the documentation of the
     * <tt>DataInput</tt> interface.
     *
     * @param bytes  a <tt>byte[]</tt> containing a short value.
     * @param offset an offset into the given byte[].
     * @return the signed short as <tt>short</tt>.
     */
    public static short registerValueToShort(final byte[] bytes, final int offset) {
        return (short) ((bytes[offset] << 8) | (bytes[offset + 1] & 0xff));
    }

    /**
     * Converts the given <tt>short</tt> into a register (2 bytes). The byte
     * values in the register, in the  order shown, are:
     *
     * <pre><code>
     * (byte)(0xff &amp; (v &gt;&gt; 8))
     * (byte)(0xff &amp; v)
     * </code></pre>
     * <p>
     *
     * @param s Value to convert
     * @return a register containing the given short value.
     */
    public static byte[] shortToRegister(short s) {
        byte[] register = new byte[2];
        register[0] = (byte) (0xff & (s >> 8));
        register[1] = (byte) (0xff & s);
        return register;
    }

    /**
     * Converts a byte[4] binary int value to a primitive int.<br> The value
     * returned is:
     *
     * <pre><code>
     * (((a &amp; 0xff) &lt;&lt; 24) | ((b &amp; 0xff) &lt;&lt; 16) |
     * &#32;((c &amp; 0xff) &lt;&lt; 8) | (d &amp; 0xff))
     * </code></pre>
     * <p>
     *
     * @param bytes registers as <tt>byte[4]</tt>.
     * @return the integer contained in the given register bytes.
     */
    public static int registerValueToInt(final byte[] bytes, final int offset) {
        return (((bytes[offset] & 0xff) << 24) |
                ((bytes[offset + 1] & 0xff) << 16) |
                ((bytes[offset + 2] & 0xff) << 8) |
                (bytes[offset + 3] & 0xff)
        );
    }

    /**
     * Converts an int value to a byte[4] array.
     * <p>
     *
     * @param v the value to be converted.
     * @return a byte[4] containing the value.
     */
    public static byte[] intToRegisters(int v) {
        byte[] registers = new byte[4];
        registers[0] = (byte) (0xff & (v >> 24));
        registers[1] = (byte) (0xff & (v >> 16));
        registers[2] = (byte) (0xff & (v >> 8));
        registers[3] = (byte) (0xff & v);
        return registers;
    }

    /**
     * Converts a byte[8] binary long value into a long primitive.
     * <p>
     *
     * @param bytes a byte[8] containing a long value.
     * @return a long value.
     */
    public static long registerValueToLong(final byte[] bytes, final int offset) {
        return ((((long) (bytes[offset] & 0xff) << 56) |
                ((long) (bytes[offset + 1] & 0xff) << 48) |
                ((long) (bytes[offset + 2] & 0xff) << 40) |
                ((long) (bytes[offset + 3] & 0xff) << 32) |
                ((long) (bytes[offset + 4] & 0xff) << 24) |
                ((long) (bytes[offset + 5] & 0xff) << 16) |
                ((long) (bytes[offset + 6] & 0xff) << 8) |
                ((long) (bytes[offset + 7] & 0xff)))
        );
    }

    /**
     * Converts a long value to a byte[8].
     * <p>
     *
     * @param v the value to be converted.
     * @return a byte[8] containing the long value.
     */
    public static byte[] longToRegisters(long v) {
        byte[] registers = new byte[8];
        registers[0] = (byte) (0xff & (v >> 56));
        registers[1] = (byte) (0xff & (v >> 48));
        registers[2] = (byte) (0xff & (v >> 40));
        registers[3] = (byte) (0xff & (v >> 32));
        registers[4] = (byte) (0xff & (v >> 24));
        registers[5] = (byte) (0xff & (v >> 16));
        registers[6] = (byte) (0xff & (v >> 8));
        registers[7] = (byte) (0xff & v);
        return registers;
    }

    /**
     * Converts a byte[4] binary float value to a float primitive.
     * <p>
     *
     * @param bytes the byte[4] containing the float value.
     * @return a float value.
     */
    public static float registerValueToFloat(final byte[] bytes, final int offset) {
        return Float.intBitsToFloat((((bytes[offset] & 0xff) << 24) |
                ((bytes[offset + 1] & 0xff) << 16) |
                ((bytes[offset + 2] & 0xff) << 8) |
                (bytes[offset + 3] & 0xff)));
    }

    /**
     * Converts a float value to a byte[4] binary float value.
     * <p>
     *
     * @param f the float to be converted.
     * @return a byte[4] containing the float value.
     */
    public static byte[] floatToRegisters(float f) {
        return intToRegisters(Float.floatToIntBits(f));
    }

    /**
     * Converts a byte[8] binary double value into a double primitive.
     * <p>
     *
     * @param bytes a byte[8] to be converted.
     * @return a double value.
     */
    public static double registerValueToDouble(final byte[] bytes, final int offset) {
        return Double.longBitsToDouble(((((long) (bytes[offset] & 0xff) << 56) |
                ((long) (bytes[offset + 1] & 0xff) << 48) |
                ((long) (bytes[offset + 2] & 0xff) << 40) |
                ((long) (bytes[offset + 3] & 0xff) << 32) |
                ((long) (bytes[offset + 4] & 0xff) << 24) |
                ((long) (bytes[offset + 5] & 0xff) << 16) |
                ((long) (bytes[offset + 6] & 0xff) << 8) |
                ((long) (bytes[offset + 7] & 0xff)))));
    }

    /**
     * Converts a double value to a byte[8].
     * <p>
     *
     * @param d the double to be converted.
     * @return a byte[8].
     */
    public static byte[] doubleToRegisters(double d) {
        return longToRegisters(Double.doubleToLongBits(d));
    }

    /**
     * Converts an unsigned byte to an integer.
     *
     * @param b the byte to be converted.
     * @return an integer containing the unsigned byte value.
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * Returns the low byte of an integer word.
     *
     * @param wd word to get low byte from
     * @return low byte of word
     */
    public static byte lowByte(int wd) {
        return Integer.valueOf(0xff & wd).byteValue();
    }

    /**
     * Makes a word from 2 bytes
     *
     * @param hibyte  High byte
     * @param lowbyte Low byte
     * @return Word
     */
    public static int makeWord(int hibyte, int lowbyte) {
        int hi = 0xFF & hibyte;
        int low = 0xFF & lowbyte;
        return ((hi << 8) | low);
    }

    /**
     * Returns a <tt>byte[]</tt> containing the given byte as unsigned
     * hexadecimal number digits.
     *
     * @param i the int to be converted into a hex string.
     * @return the generated hexadecimal representation as <code>byte[]</code>.
     */
    public static byte[] toHex(int i) {
        val buf = new StringBuilder(2);
        // Don't forget the second hex digit.
        if ((i & 0xff) < 16)
            buf.append("0");
        buf.append(Long.toString(i & 0xff, 16).toUpperCase());
        return buf.toString().getBytes(J2ModUtils.ASCII);
    }

}
