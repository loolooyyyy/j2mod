package cc.koosha.modbus.util;

import cc.koosha.modbus.util.BitVector;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BitVectorTest {

    @Test
    public void testBitVector() {
        for (int s = 1; s <= 128; s++) {
            BitVector bv = BitVector.valueOf(s);
            Assert.assertNotNull("Could not instantiate bitvector of size " + s, bv);
            Assert.assertEquals("Bitvector does not have size " + s, s, bv.size());
        }
    }

    @Test
    public void testCreateBitVector() {
        byte[] testData = new byte[2];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte)i;
        }
        BitVector b1 = BitVector.valueOf(testData);
        Assert.assertNotNull("Could not instantiate bitvector of size 16", b1);
        Assert.assertEquals("Bitvector does not have size 16", 16, b1.size());
        BitVector b2 = BitVector.valueOf(testData, 8);
        Assert.assertNotNull("Could not instantiate bitvector of size 8", b2);
        Assert.assertEquals("Bitvector does not have size 8", 8, b2.size());
    }


    @Test
    public void testGetSetBytes() {
        byte[] testData = new byte[8];
        byte[] nullData = new byte[8];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte)i;
            nullData[i] = (byte)0;
        }
        BitVector b1 = BitVector.valueOf(nullData);
        b1.setBytes(testData);
        byte[] actualData = b1.getBytes();
        Assert.assertNotNull("Cannot retrieve bytes from bitvector", actualData);
        Assert.assertEquals("Returned data array does not have the same length as original", testData.length, actualData.length);
        for (int i = 0; i < testData.length; i++) {
            Assert.assertEquals("Byte " + i + " is not equal to testdata", testData[i], actualData[i]);
        }
    }

    @Test
    public void testSetGetBit() {
        byte[] nullData = new byte[8];
        for (int i = 0; i < nullData.length; i++) {
            nullData[i] = (byte)0;
        }
        BitVector bv = BitVector.valueOf(nullData);
        for (int i = 0; i < 64; i++) {
            Assert.assertFalse("Bit " + i + " should not be set", bv.getBit(i));
            bv.setBit(i, true);
            Assert.assertTrue("Bit " + i + " should be set", bv.getBit(i));
        }
    }

    @Test
    public void testSizes() {
        BitVector b1 = BitVector.valueOf(16);
        Assert.assertEquals("Size should be 16", 16, b1.size());
        Assert.assertEquals("Bytesize should be 2", 2, b1.byteSize());
        b1.trim(4);
        Assert.assertEquals("Size should be 4", 4, b1.size());
        Assert.assertEquals("Bytesize should still be 2", 2, b1.byteSize());

        BitVector b2 = BitVector.valueOf(4);
        Assert.assertEquals("Size should be 4", 4, b2.size());
        Assert.assertEquals("Bytesize should still be 1", 1, b2.byteSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForceIllegalSize() throws IllegalArgumentException {
        BitVector bv = BitVector.valueOf(8);
        bv.trim(8000);
    }

    @Test
    public void testToString() {
        byte[] testData = new byte[8];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte)i;
        }
        BitVector bv = BitVector.valueOf(testData);
        bv.trim(62);
        Assert.assertEquals("BitVector string is incorrect",
                "00000000 00000001 00000010 00000011 00000100 00000101 00000110 000111 ", bv.toString());
    }

}
