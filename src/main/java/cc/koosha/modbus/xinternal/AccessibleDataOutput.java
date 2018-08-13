package cc.koosha.modbus.xinternal;

import java.io.DataOutput;
import java.io.OutputStream;


public abstract class AccessibleDataOutput extends OutputStream implements DataOutput {

    public abstract byte[] getBufferCopy();

    public abstract int size();

    public abstract void reset();

}
