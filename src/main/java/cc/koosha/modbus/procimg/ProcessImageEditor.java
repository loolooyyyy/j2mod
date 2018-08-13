package cc.koosha.modbus.procimg;


/**
 * Interface defining implementation specific details of the
 * <tt>ProcessImage</tt>, adding mechanisms for creating and modifying the
 * actual "process image".
 * <p>
 *
 * @author Koosha Hosseiny
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface ProcessImageEditor {

    /**
     * Sets a new <tt>DigitalOut</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param out the new <tt>DigitalOut</tt> instance to be set.
     */
    void setDigitalOut(int ref, DigitalOut out);

    /**
     * Adds a new <tt>DigitalOut</tt> instance.
     * <p>
     * TODO 0 - Explicitness: disallow implicit register overriding.
     * <p>
     * Just creates trouble, specially with observables.
     *
     * @param out the <tt>DigitalOut</tt> instance to be added.
     */
    void addDigitalOut(DigitalOut out);

    /**
     * Adds a new <tt>DigitalOut</tt> instance at the given reference.
     *
     * @param ref - the reference for the instance.
     * @param out - the <tt>DigitalOut</tt> instance to be added.
     */
    void addDigitalOut(int ref, DigitalOut out);

    /**
     * Removes a given <tt>DigitalOut</tt> instance.
     *
     * @param out the <tt>DigitalOut</tt> instance to be removed.
     */
    void removeDigitalOut(DigitalOut out);


    /**
     * Sets a new <tt>DigitalIn</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param di  the new <tt>DigitalIn</tt> instance to be set.
     */
    void setDigitalIn(int ref, DigitalIn di);

    /**
     * Adds a new <tt>DigitalIn</tt> instance.
     *
     * @param di the <tt>DigitalIn</tt> instance to be added.
     */
    void addDigitalIn(DigitalIn di);

    /**
     * Adds a new <tt>DigitalIn</tt> instance at the given reference, possibly
     * creating a hole between the last existing reference and the new object.
     *
     * @param ref - the reference for the new instance.
     * @param di  the <tt>DigitalIn</tt> instance to be added.
     */
    void addDigitalIn(int ref, DigitalIn di);

    /**
     * Removes a given <tt>DigitalIn</tt> instance.
     *
     * @param di the <tt>DigitalIn</tt> instance to be removed.
     */
    void removeDigitalIn(DigitalIn di);


    /**
     * Sets a new <tt>InputRegister</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param reg the new <tt>InputRegister</tt> instance to be set.
     */
    void setInputRegister(int ref, InputRegister reg);

    /**
     * Adds a new <tt>InputRegister</tt> instance.
     *
     * @param reg the <tt>InputRegister</tt> instance to be added.
     */
    void addInputRegister(InputRegister reg);

    /**
     * Adds a new <tt>InputRegister</tt> instance, possibly
     * creating a hole between the last existing reference and the new object.
     *
     * @param ref - The reference for the new instance.
     * @param reg the <tt>InputRegister</tt> instance to be added.
     */
    void addInputRegister(int ref, InputRegister reg);

    /**
     * Removes a given <tt>InputRegister</tt> instance.
     *
     * @param reg the <tt>InputRegister</tt> instance to be removed.
     */
    void removeInputRegister(InputRegister reg);


    /**
     * Sets a new <tt>Register</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param reg the new <tt>Register</tt> instance to be set.
     */
    void setRegister(int ref, Register reg);

    /**
     * Adds a new <tt>Register</tt> instance.
     *
     * @param reg the <tt>Register</tt> instance to be added.
     */
    void addRegister(Register reg);

    /**
     * Adds a new <tt>Register</tt> instance, possibly
     * creating a hole between the last existing reference and the new object.
     *
     * @param ref - the reference for the new instance.
     * @param reg the <tt>Register</tt> instance to be added.
     */
    void addRegister(int ref, Register reg);

    /**
     * Removes a given <tt>Register</tt> instance.
     *
     * @param reg the <tt>Register</tt> instance to be removed.
     */
    void removeRegister(Register reg);


    /**
     * Sets a new <tt>File</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param reg the new <tt>File</tt> instance to be set.
     */
    void setFile(int ref, File reg);

    /**
     * Adds a new <tt>File</tt> instance.
     *
     * @param reg the <tt>File</tt> instance to be added.
     */
    void addFile(File reg);

    /**
     * Adds a new <tt>File</tt> instance, possibly
     * creating a hole between the last existing reference and the new object.
     *
     * @param ref - the reference for the new isntance.
     * @param reg the <tt>File</tt> instance to be added.
     */
    void addFile(int ref, File reg);

    /**
     * Removes a given <tt>File</tt> instance.
     *
     * @param reg the <tt>File</tt> instance to be removed.
     */
    void removeFile(File reg);


    /**
     * Sets a new <tt>FIFO</tt> instance at the given reference.
     *
     * @param ref the reference as <tt>int</tt>.
     * @param reg the new <tt>FIFO</tt> instance to be set.
     */
    void setFIFO(int ref, FIFO reg);

    /**
     * Adds a new <tt>FIFO</tt> instance.
     *
     * @param reg the <tt>FIFO</tt> instance to be added.
     */
    void addFIFO(FIFO reg);

    /**
     * Adds a new <tt>FIFO</tt> instance, possibly
     * creating a hole between the last existing reference and the new object.
     *
     * @param ref - the reference for the new instance.
     * @param reg the <tt>FIFO</tt> instance to be added.
     */
    void addFIFO(int ref, FIFO reg);

    /**
     * Removes a given <tt>FIFO</tt> instance.
     *
     * @param reg the <tt>FIFO</tt> instance to be removed.
     */
    void removeFIFO(FIFO reg);

}
