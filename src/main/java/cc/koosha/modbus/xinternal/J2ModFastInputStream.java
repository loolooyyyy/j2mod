package cc.koosha.modbus.xinternal;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;


/**
 * Class implementing a byte array input stream with a DataInput interface.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
@SuppressWarnings("NullableProblems")
@Slf4j
public final class J2ModFastInputStream extends AccessibleDataInput {

    private final DataInputStream dataInputStream;

    /**
     * Number of bytes in the input buffer.
     */
    private int count;

    /**
     * Actual position pointer into the input buffer.
     */
    private int pos;

    /**
     * Marked position pointer into the input buffer.
     */
    private int mark;

    /**
     * Input buffer <tt>byte[]</tt>.
     */
    private byte[] buf;


    /**
     * Constructs a new <tt>BytesInputStream</tt> instance, with an empty buffer
     * of a given size.
     *
     * @param size the size of the input buffer.
     */
    J2ModFastInputStream(int size) {
        this(new byte[size]);
    }

    /**
     * Constructs a new <tt>BytesInputStream</tt> instance, that will read from
     * the given data.
     *
     * @param data a byte array containing data to be read.
     */
    J2ModFastInputStream(byte[] data) {
        buf = data;
        count = data.length;
        pos = 0;
        mark = 0;
        dataInputStream = new DataInputStream(this);
    }


    /**
     * Returns the reference to the input buffer.
     *
     * @return the reference to the <tt>byte[]</tt> input buffer.
     */
    @Override
    public byte[] getBuffer() {
        byte[] dest = new byte[buf.length];
        System.arraycopy(buf, 0, dest, 0, dest.length);
        return dest;
    }

    public void readFully(byte b[]) throws IOException {
        dataInputStream.readFully(b);
    }

    public void readFully(byte b[], int off, int len) throws IOException {
        dataInputStream.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readBoolean();
    }

    public byte readByte() throws IOException {
        return dataInputStream.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return dataInputStream.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return dataInputStream.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return dataInputStream.readUnsignedShort();
    }

    public char readChar() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readChar();
    }

    public int readInt() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readInt();
    }

    public long readLong() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readLong();
    }

    public float readFloat() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readFloat();
    }

    public double readDouble() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readDouble();
    }

    public String readLine() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String readUTF() throws IOException {
        throw new UnsupportedOperationException();
        // return dataInputStream.readUTF();
    }


    // --- ByteArrayInputStream compatible methods ---

    public int read() {
        throw new UnsupportedOperationException();
        // if (log.isTraceEnabled()) {
        //     log.trace("read()");
        //     log.trace("count={} pos={}", count, pos);
        // }
        // return (pos < count) ? (buf[pos++] & 0xff) : (-1);
    }

    public int read(byte[] toBuf) {
        throw new UnsupportedOperationException();
        // if (log.isTraceEnabled()) {
        //     log.trace("read(byte[])");
        // }
        // return read(toBuf, 0, toBuf.length);
    }

    public int read(byte[] toBuf, int offset, int length) {
        throw new UnsupportedOperationException();
        // if (log.isTraceEnabled()) {
        //     log.trace("read(byte[],int,int)");
        // }
        // int avail = count - pos;
        // if (avail <= 0) {
        //     return -1;
        // }
        // if (length > avail) {
        //     length = avail;
        // }
        // for (int i = 0; i < length; i++) {
        //     toBuf[offset++] = buf[pos++];
        // }
        // return length;
    }

    public long skip(long count) {
        int myCount = (int) count;
        if (myCount + pos > this.count) {
            myCount = this.count - pos;
        }
        pos += myCount;
        return myCount;
    }

    public int available() {
        return count - pos;
    }

    public void mark(int readLimit) {
        if (J2ModFastInputStream.log.isTraceEnabled()) {
            J2ModFastInputStream.log.trace("mark()");
        }
        mark = pos;
        if (J2ModFastInputStream.log.isTraceEnabled()) {
            J2ModFastInputStream.log.trace("mark={} pos={}", mark, pos);
        }
    }

    public void reset() {
        if (J2ModFastInputStream.log.isTraceEnabled()) {
            J2ModFastInputStream.log.trace("reset()");
        }
        pos = mark;
        if (J2ModFastInputStream.log.isTraceEnabled()) {
            J2ModFastInputStream.log.trace("mark={} pos={}", mark, pos);
        }
    }

    public boolean markSupported() {
        throw new UnsupportedOperationException();
        // return true;
    }


    /**
     * Resets this <tt>BytesInputStream</tt> using the given byte[] as new input
     * buffer and a given length.
     *
     * @param data   a byte array with data to be read.
     * @param length the length of the buffer to be considered.
     */
    @Deprecated
    public void reset(byte[] data, int length) {
        pos = 0;
        mark = 0;
        count = length;
        buf = data;
    }

    /**
     * Resets this <tt>BytesInputStream</tt>  assigning the input buffer a new
     * length.
     *
     * @param length the length of the buffer to be considered.
     */
    @Deprecated
    public void reset(int length) {
        pos = 0;
        count = length;
    }

    /**
     * Skips the given number of bytes or all bytes till the end of the assigned
     * input buffer length.
     *
     * @param n the number of bytes to be skipped as <tt>int</tt>.
     */
    @Deprecated
    public void skip(int n) {
        mark(pos);
        pos += n;
    }

}
