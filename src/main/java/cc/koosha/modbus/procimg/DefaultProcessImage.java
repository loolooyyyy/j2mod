package cc.koosha.modbus.procimg;

import cc.koosha.modbus.xinternal.J2ModPrecondition;
import cc.koosha.modbus.util.Predicate;
import cc.koosha.modbus.util.Range;
import cc.koosha.modbus.xinternal.SynchronizedStorage;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;


@Getter(AccessLevel.PACKAGE)
public final class DefaultProcessImage implements ProcessImage {

    private static final boolean STRICT = true;

    private static <T> SynchronizedStorage<T> storage(String name, int max) {
        return new SynchronizedStorage<T>(name, STRICT, 0, max);
    }

    private final SynchronizedStorage<DigitalIn> digitalIns = storage("DigitalIn", 65535);
    private final SynchronizedStorage<DigitalOut> digitalOuts = storage("DigitalOut", 65535);
    private final SynchronizedStorage<InputRegister> inputRegisters = storage("InputRegister", 65535);
    private final SynchronizedStorage<Register> registers = storage("Register", 65535);
    private final SynchronizedStorage<File> files = storage("File", 99999);
    private final SynchronizedStorage<FIFO> fifos = storage("Fifo", 99999);

    private final ProcessImageEditor editor = new DefaultProcessImageEditor(this);
    private final RegisterFactory maker = new DefaultRegisterFactory();

    private final int unitID;

    /**
     * Constructs a new <tt>SimpleProcessImage</tt> instance having a
     * (potentially) non-zero unit ID.
     *
     * @param unit Unit ID of this image
     */
    DefaultProcessImage(int unit) {
        unitID = unit;
    }

    public int getUnitID() {
        return unitID;
    }

    // --------------------

    private static int from(Range<Integer> range) {
        return range.lowerBoundType() == Range.BoundType.OPEN
               ? range.lowerEndpoint() + 1
               : range.lowerEndpoint();
    }

    private static int count(Range<Integer> range) {
        final int to = range.upperBoundType() == Range.BoundType.OPEN
                       ? range.upperEndpoint() - 1
                       : range.upperEndpoint();
        return to - from(range);
    }


    @Override
    public final List<DigitalOut> getDigitalOutRange(Range<Integer> range) {
        return getDigitalOutRange(from(range), count(range));
    }

    @Override
    public final List<DigitalIn> getDigitalInRange(Range<Integer> range) {
        return getDigitalInRange(from(range), count(range));
    }

    @Override
    public final List<InputRegister> getInputRegisterRange(Range<Integer> range) {
        return getInputRegisterRange(from(range), count(range));
    }

    @Override
    public final List<Register> getRegisterRange(Range<Integer> range) {
        return getRegisterRange(from(range), count(range));
    }

    // --------------------

    @Override
    public final List<DigitalOut> getDigitalOutRange(int ref, int count) {
        return digitalOuts.getRange(ref, count);
    }

    @Override
    public final DigitalOut getDigitalOut(int ref) {
        return digitalOuts.get(ref);
    }

    @Override
    public final int getDigitalOutCount() {
        return digitalOuts.size();
    }


    // --------------------

    @Override
    public final List<DigitalIn> getDigitalInRange(int ref, int count) {
        return digitalIns.getRange(ref, count);
    }

    @Override
    public final DigitalIn getDigitalIn(int ref) {
        return digitalIns.get(ref);
    }

    @Override
    public final int getDigitalInCount() {
        return digitalIns.size();
    }


    // --------------------

    @Override
    public final List<InputRegister> getInputRegisterRange(int ref, int count) {
        return inputRegisters.getRange(ref, count);
    }

    @Override
    public final InputRegister getInputRegister(int ref) {
        return inputRegisters.get(ref);
    }

    @Override
    public final int getInputRegisterCount() {
        return inputRegisters.size();
    }

    // --------------------

    @Override
    public final List<Register> getRegisterRange(int ref, int count) {
        return registers.getRange(ref, count);
    }

    @Override
    public final Register getRegister(int ref) {
        return registers.get(ref);
    }

    @Override
    public final int getRegisterCount() {
        return registers.size();
    }

    // --------------------

    @Override
    public final File getFileByNumber(final int ref) {
        J2ModPrecondition.ensureAddressIsInRange(ref, 0, 9999, "File");
        File found = files.find(new Predicate<File>() {
            @Override
            public final boolean test(File file) {
                return file.getFileNumber() == ref;
            }
        });
        return J2ModPrecondition.ensureHadAddress(found, ref, "File");
    }

    @Override
    public final File getFile(int ref) {
        return files.get(ref);
    }

    @Override
    public final int getFileCount() {
        return files.size();
    }

    // --------------------

    @Override
    public final FIFO getFIFOByAddress(final int ref) {
        // TODO check ref range?
        FIFO found = fifos.find(new Predicate<FIFO>() {
            @Override
            public final boolean test(FIFO file) {
                return file.getAddress() == ref;
            }
        });
        return J2ModPrecondition.ensureHadAddress(found, ref, "FIFO");
    }

    @Override
    public final FIFO getFIFO(int ref) {
        return fifos.get(ref);
    }

    @Override
    public final int getFIFOCount() {
        return fifos.size();
    }


    // =========================================================================

    @Override
    public ProcessImageEditor editor() {
        return editor;
    }

    @Override
    public RegisterFactory registerFactory() {
        return this.maker;
    }

}
