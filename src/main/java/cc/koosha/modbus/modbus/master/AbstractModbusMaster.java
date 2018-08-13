/*
 * Copyright 2002-2016 jamod & j2mod development teams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.koosha.modbus.modbus.master;

import cc.koosha.modbus.ModbusException;
import cc.koosha.modbus.modbus.transaction.ModbusTransaction;
import cc.koosha.modbus.msg.ModbusResponse;
import cc.koosha.modbus.msg.request.*;
import cc.koosha.modbus.msg.response.*;
import cc.koosha.modbus.procimg.InputRegister;
import cc.koosha.modbus.procimg.Register;
import cc.koosha.modbus.util.BitVector;
import cc.koosha.modbus.xinternal.J2ModPrecondition;
import lombok.*;

import java.util.List;


/**
 * {@inheritDoc}
 *
 * @author Steve O'Hara (4NG)
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractModbusMaster implements ModbusMaster {

    @Getter
    private final ModbusMasterConfig config;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private ModbusTransaction transaction;

    private ModbusResponse getAndCheckResponse() throws ModbusException {
        ModbusResponse res = getTransaction().getResponse();
        if (res == null)
            throw new ModbusException("No response");
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitVector readCoils(int unitId, int ref, int count) throws ModbusException {
        // TODO validate args.
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new ReadCoilsRequest(unitId, ref, count);
        getTransaction().setRequest(req);
        getTransaction().execute();
        BitVector bv = ((ReadCoilsResponse) getAndCheckResponse()).getCoils();
        bv.trim(count);
        return bv;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeCoil(int unitId, int ref, boolean state) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new WriteCoilRequest(unitId, ref, state);
        getTransaction().setRequest(req);
        getTransaction().execute();
        return ((WriteCoilResponse) getAndCheckResponse()).getCoil();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCoils(int unitId, int ref, BitVector coils) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new WriteMultipleCoilsRequest(unitId, ref, coils);
        getTransaction().setRequest(req);
        getTransaction().execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitVector readDiscreteInput(int unitId, int ref, int count) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new ReadInputDiscretesRequest(unitId, ref, count);
        getTransaction().setRequest(req);
        getTransaction().execute();
        BitVector bv = ((ReadInputDiscretesResponse) getAndCheckResponse()).getDiscretes();
        bv.trim(count);
        return bv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<InputRegister> readInputRegisters(int unitId, int ref, int count) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val readInputRegistersRequest = new ReadInputRegistersRequest(unitId, ref, count);
        getTransaction().setRequest(readInputRegistersRequest);
        getTransaction().execute();
        return ((ReadInputRegistersResponse) getAndCheckResponse()).getRegisters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Register> readHoldingRegisters(int unitId, int ref, int count) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new ReadMultipleRegistersRequest(unitId, ref, count);
        getTransaction().setRequest(req);
        getTransaction().execute();
        return ((ReadMultipleRegistersResponse) getAndCheckResponse()).getRegisters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHoldingRegister(int unitId, int ref, Register register) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new WriteSingleRegisterRequest(unitId, ref, register);
        getTransaction().setRequest(req);
        getTransaction().execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHoldingRegisters(int unitId, int ref, List<Register> registers) throws ModbusException {
        J2ModPrecondition.ensureNotNullState(transaction, "transaction not set");
        val req = new WriteMultipleRegistersRequest(unitId, ref, registers);
        getTransaction().setRequest(req);
        getTransaction().execute();
    }

}