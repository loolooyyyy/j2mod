package cc.koosha.modbus.xinternal;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Class implementing a byte array output stream with a DataInput interface.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@SuppressWarnings("NullableProblems")
@Slf4j
public final class J2ModFastOutputStream extends AccessibleDataOutput {

    /**
     * Defines the default increment of the output buffer size (100 bytes).
     */
    private static final int DEFAULT_BUMP_SIZE = 100;

    /**
     * Output buffer <tt>byte[]</tt>.
     */
    private byte[] buf;

    private DataOutputStream dataOutputStream;
    /**
     * Number of bytes in the output buffer.
     */

    private int count;

    /**
     * Increment of the output buffer size on overflow.
     */
    private int bumpLen;

    private void bump(int needed) {
        byte[] toBuf = new byte[buf.length + needed + bumpLen];
        System.arraycopy(buf, 0, toBuf, 0, count);
        buf = toBuf;
    }

    /**
     * Constructs a new <tt>BytesOutputStream</tt> instance with a new output
     * buffer of the given size.
     *
     * @param size the size of the output buffer as <tt>int</tt>.
     */
    J2ModFastOutputStream(int size) {
        this.buf = new byte[size];
        this.bumpLen = DEFAULT_BUMP_SIZE;
        dataOutputStream = new DataOutputStream(this);
    }

    /**
     * Constructs a new <tt>BytesOutputStream</tt> instance with a given output
     * buffer.
     *
     * @param buffer the output buffer as <tt>byte[]</tt>.
     */
    J2ModFastOutputStream(byte[] buffer) {
        this.buf = buffer;
        this.bumpLen = DEFAULT_BUMP_SIZE;
        count = 0;
        dataOutputStream = new DataOutputStream(this);
    }

    /**
     * Returns the reference to the output buffer.
     *
     * @return the reference to the <tt>byte[]</tt> output buffer.
     */
    @Override
    public byte[] getBufferCopy() {
        byte[] dest = new byte[buf.length];
        System.arraycopy(buf, 0, dest, 0, dest.length);
        return dest;
    }

    public void writeBoolean(boolean v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        dataOutputStream.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        dataOutputStream.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeDouble(v);
    }

    public void writeBytes(String s) {
        throw new UnsupportedOperationException();
        // int len = s.length();
        // for (int i = 0; i < len; i++) {
        //     this.write((byte) s.charAt(i));
        // }
    }

    public void writeChars(String s) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeChars(s);
    }

    public void writeUTF(String str) throws IOException {
        throw new UnsupportedOperationException();
        // dataOutputStream.writeUTF(str);
    }


    public void write(int b) {
        if (count + 1 > buf.length) {
            bump(1);
        }
        buf[count++] = (byte) b;
    }

    public void write(byte[] fromBuf) {
        int needed = count + fromBuf.length - buf.length;
        if (needed > 0) {
            bump(needed);
        }
        for (byte aFromBuf : fromBuf) {
            buf[count++] = aFromBuf;
        }
    }

    public void write(byte[] fromBuf, int offset, int length) {

        int needed = count + length - buf.length;
        if (needed > 0) {
            bump(needed);
        }
        int fromLen = offset + length;

        for (int i = offset; i < fromLen; i++) {
            buf[count++] = fromBuf[i];
        }
    }


    /**
     * Returns the number of bytes written to this
     * <tt>FastByteArrayOutputStream</tt>.
     *
     * @return the number of bytes written as <tt>int</tt>.
     */
    @Override
    public int size() {
        return count;
    }

    @Override
    public void reset() {
        count = 0;
    }

    /**
     * Returns the written bytes in a newly allocated byte[] of length
     * getSize().
     *
     * @return a newly allocated byte[] with the content of the output buffer.
     */
    @Deprecated
    public byte[] toByteArray() {
        byte[] toBuf = new byte[count];
        System.arraycopy(buf, 0, toBuf, 0, count);
        //for (int i = 0; i < count; i++) {
        //  toBuf[i] = buf[i];
        //}
        return toBuf;
    }

    @Deprecated
    public String toString() {
        try {
            return new String(buf, 0, count, "US-ASCII");
        }
        catch (Exception e) {
            J2ModFastOutputStream.log.error("problem converting bytes to string - {}", e
                    .getMessage());
        }
        return "";
    }

}
