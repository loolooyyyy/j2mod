package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.Modbus;
import cc.koosha.modbus.msg.ModbusMessage;
import lombok.val;


// TODO ensure usages only in debug
// TODO replace with netty

/**
 * Helper methods for debugging.
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author John Charlton
 * @author Steve O'Hara (4NG)
 */
public final class J2ModDebugUtils {

    private J2ModDebugUtils() {
    }

    /**
     * Converts a <tt>ModbusMessage</tt> instance into a hex encoded string
     * representation.
     *
     * @param msg the message to be converted.
     * @return the converted hex encoded string representation of the message.
     */
    public static String toHex(ModbusMessage msg) {
        try {
            val os = J2ModUtils.dataOutput(Modbus.MAX_MESSAGE_LENGTH);
            msg.writeTo(os);
            // TODO not worth the trouble for debugging.
            if (os instanceof J2ModFastOutputStream) {
                final val kast = (J2ModFastOutputStream) os;
                return toHex(os.getBufferCopy(), 0, kast.size());
            }
            else {
                return toHex(os.getBufferCopy());
            }
        }
        catch (Exception ex) {
            return "toHex(ModbusMessage) failed: " + ex.getMessage();
        }
    }

    /**
     * Returns a <tt>String</tt> containing unsigned hexadecimal numbers as
     * digits. The <tt>String</tt> will contain two hex digit characters for
     * each byte from the passed in <tt>byte[]</tt>.<br> The bytes will be
     * separated by a space character.
     *
     * @param data the array of bytes to be converted into a hex-string.
     * @param off  the offset to start converting from.
     * @param end  the offset of the end of the byte array.
     * @return the generated hexadecimal representation as <code>String</code>.
     */
    public static String toHex(byte[] data, int off, int end) {
        //double size, two bytes (hex range) for one byte
        val buf = new StringBuilder(data.length * 2);
        if (end > data.length) {
            end = data.length;
        }
        for (int i = off; i < end; i++) {
            //don't forget the second hex digit
            if (((int) data[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) data[i] & 0xff, 16).toUpperCase());
            if (i < end - 1) {
                buf.append(" ");
            }
        }
        return buf.toString();
    }

    /**
     * Returns the given byte[] as hex encoded string.
     *
     * @param data a byte[] array.
     * @return a hex encoded String.
     */
    public static String toHex(byte[] data) {
        return toHex(data, 0, data.length);
    }

    public static String toHex(int data) {
        // TODO LITTLE/BIG Endian?
        return toHex(new byte[]{
                (byte) (data >> 8),
                (byte) (data & 0xFF)
        });
    }


    /*
     * Returns a <tt>byte[]</tt> containing the given byte as unsigned
     * hexadecimal number digits.
     *
     * @param i the int to be converted into a hex string.
     * @return the generated hexadecimal representation as <code>byte[]</code>.
     */
/*
    public static byte[] toHex(int i) {
        StringBuilder buf = new StringBuilder(2);
        //don't forget the second hex digit
        if ((i & 0xff) < 0x10) {
            buf.append("0");
        }
        buf.append(Long.toString(i & 0xff, 16).toUpperCase());
        try {
            return buf.toString().getBytes("US-ASCII");
        }
        catch (Exception e) {
            return null;
        }
    }
*/

}
