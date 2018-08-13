/**
 * TODO 1 - X: Why is this package called facade?
 * <p>
 * <p>
 * How it works: Serial/TCP/UDP
 * <p>
 * 0. ModbusXxxMaster
 * 1. createNewConnection()
 * 2. XxxConnection
 * 3. open()
 * 4. createTransport()
 * 5. createTransaction()
 * 6. Make Requests...
 * <p>
 * <p>
 * TODO 9 - Architecture: ModbusXxxMaster shouldn't create connections, it must be injected into.
 * totally reverse the order from transaction to master.
 */
package cc.koosha.modbus.modbus.master;

