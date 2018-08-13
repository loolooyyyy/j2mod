package cc.koosha.modbus.modbus.master;

import cc.koosha.modbus.ModbusException;
import cc.koosha.modbus.procimg.InputRegister;
import cc.koosha.modbus.procimg.Register;
import cc.koosha.modbus.util.BitVector;

import java.util.List;


/**
 * Modbus/TCP Master facade.
 *
 * <p>
 * TODO 0 - Abstraction: type safety (generics).
 * <p>
 * TODO 1 - Configurable: move to a configuration object.
 * <p>
 * TODO 9 - Architecture: Would it be possible to make all request fields final
 * and initialized at instantiation time?
 * <p>
 * TODO 9 - Architecture: request fields are shared, a better method for locking
 * and thread safety.
 *
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public interface ModbusMaster {

    // ------------------- F2

    /**
     * Reads a given number of input discrete states from the slave.
     * <p>
     * Note that the number of bits in the bit vector will be forced to the
     * number originally requested.
     * <p>
     * TODO 0 - Threading: do not expose synchronization logic, use fields.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the input discrete to start reading from.
     * @param count  the number of input discrete states to be read.
     * @return a <tt>BitVector</tt> instance holding the received input discrete
     * states.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    BitVector readDiscreteInput(int unitId, int ref, int count) throws ModbusException;


    // ------------------- F1, F5, F15

    /**
     * Reads a given number of coil states from the slave.
     * <p>
     * Note that the number of bits in the bit vector will be forced to the
     * number originally requested.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the coil to start reading from.
     * @param count  the number of coil states to be read.
     * @return a <tt>BitVector</tt> instance holding the received coil states.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    BitVector readCoils(int unitId, int ref, int count) throws ModbusException;

    /**
     * Writes a coil state to the slave.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the coil to be written.
     * @param state  the coil state to be written.
     * @return the state of the coil as returned from the slave.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    void writeCoil(int unitId, int ref, boolean state) throws ModbusException;

    /**
     * Writes a given number of coil states to the slave.
     * <p>
     * Note that the number of coils to be written is given implicitly, through
     * {@link BitVector#size()}.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the coil to start writing to.
     * @param coils  a <tt>BitVector</tt> which holds the coil states to be
     *               written.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    void writeCoils(int unitId, int ref, BitVector coils) throws ModbusException;


    // ------------------- F4

    /**
     * Reads a given number of input registers from the slave.
     * <p>
     * Note that the number of input registers returned (i.e. array length) will
     * be according to the number received in the slave response.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the input register to start reading from.
     * @param count  the number of input registers to be read.
     * @return a <tt>InputRegister[]</tt> with the received input registers.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    List<InputRegister> readInputRegisters(int unitId, int ref, int count) throws ModbusException;


    // ------------------- F3, F6, F16

    /**
     * Reads a given number of registers from the slave.
     * <p>
     * Note that the number of registers returned (i.e. array length) will be
     * according to the number received in the slave response.
     *
     * @param unitId the slave unit id.
     * @param ref    the offset of the register to start reading from.
     * @param count  the number of registers to be read.
     * @return a <tt>Register[]</tt> holding the received registers.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    List<Register> readHoldingRegisters(int unitId, int ref, int count) throws ModbusException;

    /**
     * Writes a single register to the slave.
     *
     * @param unitId   the slave unit id.
     * @param ref      the offset of the register to be written.
     * @param register a <tt>Register</tt> holding the value of the register to
     *                 be written.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    void writeHoldingRegister(int unitId, int ref, Register register) throws ModbusException;

    /**
     * Writes a number of registers to the slave.
     *
     * @param unitId    the slave unit id.
     * @param ref       the offset of the register to start writing to.
     * @param registers a <tt>Register[]</tt> holding the values of the
     *                  registers to be written.
     * @throws ModbusException if an I/O error, a slave exception or a
     *                         transaction error occurs.
     */
    void writeHoldingRegisters(int unitId, int ref, List<Register> registers) throws ModbusException;

}
