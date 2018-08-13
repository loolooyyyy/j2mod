package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.IllegalAddressException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class J2ModPreconditionTest {

    @DataProvider
    public Object[][] dataProvider_for_checkThrowsErrorOnValuesItIsSupposedTo() {
        return new Object[][]{
                new Object[]{5, 2, 9},
                new Object[]{5, 0, 9},
                new Object[]{5, 9, 0},
                new Object[]{0, 2, 9},
                new Object[]{0, 0, 9},
                new Object[]{0, 9, 0},
                new Object[]{0, 1, 0},
                new Object[]{0, 0, 1},
                };
    }

    @Test(expectedExceptions = IllegalAddressException.class,
            dataProvider = "dataProvider_for_checkThrowsErrorOnValuesItIsSupposedTo")
    public void checkRefAndCountLength_checkThrowsErrorOnValuesItIsSupposedTo(int size, int ref, int count) {
        J2ModPrecondition.ensureRefAndCountAreInLength(size, ref, count);
    }


    @DataProvider
    public Object[][] dataProvider_for_checkValidatesOkForValuesThatAreValid() {
        return new Object[][]{
                new Object[]{0, 0, 0},
                new Object[]{1, 0, 1},
                new Object[]{1, 1, 1},
                new Object[]{1, 2, 1},
                new Object[]{5, 0, 5},
                new Object[]{5, 0, 4},
                new Object[]{5, 10, 5},
                new Object[]{5, 10, 4},
                new Object[]{5, 5, 5},
                new Object[]{5, 5, 4},

                new Object[]{1, 0, 0},
                new Object[]{1, 1, 0},
                new Object[]{1, 2, 0},
                new Object[]{5, 0, 0},
                new Object[]{5, 0, 0},
                new Object[]{5, 10, 0},
                new Object[]{5, 10, 0},
                new Object[]{5, 5, 0},
                new Object[]{5, 5, 0},
                new Object[]{5, 9999, 0},
                };
    }

    @Test(expectedExceptions = IllegalAddressException.class,
            dataProvider = "dataProvider_for_checkValidatesOkForValuesThatAreValid")
    public void checkRefAndCountLength_checkValidatesOkForValuesThatAreValid(int size, int ref, int count) {
        J2ModPrecondition.ensureRefAndCountAreInLength(size, ref, count);
    }

}