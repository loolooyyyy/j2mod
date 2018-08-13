package cc.koosha.modbus.procimg;

import cc.koosha.modbus.IllegalAddressException;
import cc.koosha.modbus.util.Range;

import java.util.List;


/**
 * Interface defining a process image in an object oriented manner.
 * <p>
 * The process image is understood as a shared memory area used form
 * communication between slave and master or device side.
 * <p>
 * This interface is a read-only view of the underlying registers.
 * <p>
 * All methods may throw {@link IllegalAddressException} except xxxCount
 * methods.
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface ProcessImage {

    /**
     * Returns a range of <tt>DigitalOut</tt> instances.
     *
     * @param range the start and end offset of registers.
     * @return an array of <tt>DigitalOut</tt> instances.
     * @ if the range from start to end is non existent.
     */
    List<DigitalOut> getDigitalOutRange(Range<Integer> range);

    /**
     * Returns a range of <tt>DigitalOut</tt> instances.
     *
     * @param offset the start offset.
     * @param count  the amount of <tt>DigitalOut</tt> from the offset.
     * @return an array of <tt>DigitalOut</tt> instances.
     * @ if the range from offset to offset+count is non existent.
     */
    List<DigitalOut> getDigitalOutRange(int offset, int count);

    /**
     * Returns the <tt>DigitalOut</tt> instance at the given reference.
     *
     * @param ref the reference.
     * @return the <tt>DigitalOut</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    DigitalOut getDigitalOut(int ref);

    /**
     * Returns the number of <tt>DigitalOut</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * @return the number of digital outs as <tt>int</tt>.
     */
    int getDigitalOutCount();


    /**
     * Returns a range of <tt>DigitalIn</tt> instances.
     *
     * @param range the start and end offset of registers.
     * @return an array of <tt>DigitalIn</tt> instances.
     * @ if the range from start to end is non existent.
     */
    List<DigitalIn> getDigitalInRange(Range<Integer> range);

    /**
     * Returns a range of <tt>DigitalIn</tt> instances.
     *
     * @param offset the start offset.
     * @param count  the amount of <tt>DigitalIn</tt> from the offset.
     * @return an array of <tt>DigitalIn</tt> instances.
     * @ if the range from offset to offset+count is non existent.
     */
    List<DigitalIn> getDigitalInRange(int offset, int count);

    /**
     * Returns the <tt>DigitalIn</tt> instance at the given reference.
     *
     * @param ref the reference.
     * @return the <tt>DigitalIn</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    DigitalIn getDigitalIn(int ref);

    /**
     * Returns the number of <tt>DigitalIn</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * @return the number of digital ins as <tt>int</tt>.
     */
    int getDigitalInCount();


    /**
     * Returns a range of <tt>InputRegister</tt> instances.
     *
     * @param range the start and end offset of registers.
     * @return an array of <tt>InputRegister</tt> instances.
     * @ if the range from start to end is non existent.
     */
    List<InputRegister> getInputRegisterRange(Range<Integer> range);

    /**
     * Returns a range of <tt>InputRegister</tt> instances.
     *
     * @param offset the start offset.
     * @param count  the amount of <tt>InputRegister</tt> from the offset.
     * @return an array of <tt>InputRegister</tt> instances.
     * @ if the range from offset to offset+count is non existent.
     */
    List<InputRegister> getInputRegisterRange(int offset, int count);

    /**
     * Returns the <tt>InputRegister</tt> instance at the given reference.
     *
     * @param ref the reference.
     * @return the <tt>InputRegister</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    InputRegister getInputRegister(int ref);

    /**
     * Returns the number of <tt>InputRegister</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * <p>
     * This is not the same as the value of the highest addressable register.
     *
     * @return the number of input registers as <tt>int</tt>.
     */
    int getInputRegisterCount();


    /**
     * Returns a range of <tt>Register</tt> instances.
     *
     * @param range the start and end offset of registers.
     * @return an array of <tt>Register</tt> instances.
     * @ if the range from start to end is non existent.
     */
    List<Register> getRegisterRange(Range<Integer> range);

    /**
     * Returns a range of <tt>Register</tt> instances.
     *
     * @param offset the start offset.
     * @param count  the amount of <tt>Register</tt> from the offset.
     * @return an array of <tt>Register</tt> instances.
     * @ if the range from offset to offset+count is non existent.
     */
    List<Register> getRegisterRange(int offset, int count);

    /**
     * Returns the <tt>Register</tt> instance at the given reference.
     * <p>
     *
     * @param ref the reference.
     * @return the <tt>Register</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    Register getRegister(int ref);

    /**
     * Returns the number of <tt>Register</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * <p>
     * This is not the same as the value of the highest addressable register.
     *
     * @return the number of registers as <tt>int</tt>.
     */
    int getRegisterCount();


    /**
     * Returns the <tt>File</tt> instance at the given reference.
     * <p>
     *
     * @param ref the reference.
     * @return the <tt>File</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    File getFile(int ref);

    /**
     * Returns the <tt>File</tt> instance having the specified file number.
     *
     * @param ref The file number for the File object to be returned.
     * @return the <tt>File</tt> instance having the given number.
     * @ if a File with the given number does not exist.
     */
    File getFileByNumber(int ref);

    /**
     * Returns the number of <tt>File</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * <p>
     * This is not the same as the value of the highest addressable register.
     *
     * @return the number of registers as <tt>int</tt>.
     */
    int getFileCount();


    /**
     * Returns the <tt>FIFO</tt> instance in the list of all FIFO objects
     * in this ProcessImage.
     *
     * @param ref the reference.
     * @return the <tt>File</tt> instance at the given address.
     * @ if the reference is invalid.
     */
    FIFO getFIFO(int ref);

    /**
     * Returns the <tt>FIFO</tt> instance having the specified base address.
     *
     * @param ref The address for the FIFO object to be returned.
     * @return the <tt>FIFO</tt> instance having the given base address
     * @ if a File with the given number does not exist.
     */
    FIFO getFIFOByAddress(int ref);

    /**
     * Returns the number of <tt>File</tt> instances in this
     * <tt>ProcessImage</tt>.
     *
     * <p>
     * This is not the same as the value of the highest addressable register.
     *
     * @return the number of registers as <tt>int</tt>.
     */
    int getFIFOCount();


    // ---------------------------------------

    ProcessImageEditor editor();

    RegisterFactory registerFactory();

}
