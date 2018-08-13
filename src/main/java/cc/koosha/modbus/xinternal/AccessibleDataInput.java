package cc.koosha.modbus.xinternal;

import java.io.DataInput;
import java.io.InputStream;


public abstract class AccessibleDataInput extends InputStream implements DataInput {

    public abstract byte[] getBuffer();

}
