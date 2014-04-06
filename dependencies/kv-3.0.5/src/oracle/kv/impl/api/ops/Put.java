/*-
 *
 *  This file is part of Oracle NoSQL Database
 *  Copyright (C) 2011, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 * If you have received this file as part of Oracle NoSQL Database the
 * following applies to the work as a whole:
 *
 *   Oracle NoSQL Database server software is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Affero
 *   General Public License as published by the Free Software Foundation,
 *   version 3.
 *
 *   Oracle NoSQL Database is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Affero General Public License for more details.
 *
 * If you have received this file as part of Oracle NoSQL Database Client or
 * distributed separately the following applies:
 *
 *   Oracle NoSQL Database client software is free software: you can
 *   redistribute it and/or modify it under the terms of the Apache License
 *   as published by the Apache Software Foundation, version 2.0.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and/or the Apache License in the LICENSE file along with Oracle NoSQL
 * Database client or server distribution.  If not, see
 * <http://www.gnu.org/licenses/>
 * or
 * <http://www.apache.org/licenses/LICENSE-2.0>.
 *
 * An active Oracle commercial licensing agreement for this product supersedes
 * these licenses and in such case the license notices, but not the copyright
 * notice, may be removed by you in connection with your distribution that is
 * in accordance with the commercial licensing terms.
 *
 * For more information please contact:
 *
 * berkeleydb-info_us@oracle.com
 *
 */

package oracle.kv.impl.api.ops;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import oracle.kv.ReturnValueVersion;
import oracle.kv.Value;
import oracle.kv.Version;
import oracle.kv.impl.api.lob.KVLargeObjectImpl;
import oracle.kv.impl.topo.PartitionId;
import oracle.kv.impl.util.SerialVersion;

import com.sleepycat.je.Transaction;

/**
 * A Put operation puts a value in the KV Store
 */
public class Put extends SingleKeyOperation {

    /**
     * The value to write
     */
    private final RequestValue requestValue;

    /**
     * Whether to return previous value/version.
     */
    private final ReturnValueVersion.Choice prevValChoice;

    /**
     * Table operations include the table id.  0 means no table.
     */
    private final long tableId;

    /**
     * Constructs a put operation.
     */
    public Put(byte[] keyBytes,
               Value value,
               ReturnValueVersion.Choice prevValChoice) {
        this(OpCode.PUT, keyBytes, value, prevValChoice, 0);
    }

    /**
     * Constructs a put operation with a table id.
     */
    public Put(byte[] keyBytes,
               Value value,
               ReturnValueVersion.Choice prevValChoice,
               long tableId) {
        this(OpCode.PUT, keyBytes, value, prevValChoice, tableId);
    }

    /**
     * For subclasses, allows passing OpCode.
     */
    Put(OpCode opCode,
        byte[] keyBytes,
        Value value,
        ReturnValueVersion.Choice prevValChoice,
        long tableId) {

        super(opCode, keyBytes);
        this.requestValue = new RequestValue(value);
        this.prevValChoice = prevValChoice;
        this.tableId = tableId;
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor first
     * to read common elements.
     */
    Put(ObjectInput in, short serialVersion)
        throws IOException {

        this(OpCode.PUT, in, serialVersion);
    }

    /**
     * For subclasses, allows passing OpCode.
     */
    Put(OpCode opCode, ObjectInput in, short serialVersion)
        throws IOException {

        super(opCode, in, serialVersion);
        requestValue = new RequestValue(in, serialVersion);
        assert requestValue.getBytes() != null;
        prevValChoice = ReturnValueVersion.getChoice(in.readUnsignedByte());
        if (serialVersion >= SerialVersion.V4) {

            /*
             * Read table id.  If there is no table the value is 0.
             */
            tableId = in.readLong();
        } else {
            tableId = 0;
        }
    }

    /**
     * FastExternalizable writer.  Must call superclass method first to write
     * common elements.
     */
    @Override
    public void writeFastExternal(ObjectOutput out, short serialVersion)
        throws IOException {

        super.writeFastExternal(out, serialVersion);
        requestValue.writeFastExternal(out, serialVersion);
        out.writeByte(prevValChoice.ordinal());
        if (serialVersion >= SerialVersion.V4) {

            /*
             * Write the table id.  If this is not a table operation the
             * id will be 0.
             */
            out.writeLong(tableId);
        } else if (tableId != 0) {
            throwTablesRequired(serialVersion);
        }
    }

    /**
     * Gets the value to be put
     */
    public byte[] getValueBytes() {
        return requestValue.getBytes();
    }

    public ReturnValueVersion.Choice getReturnValueVersionChoice() {
        return prevValChoice;
    }

    /**
     * Returns the tableId, which is 0 if this is not a table operation.
     */
    long getTableId() {
        return tableId;
    }

    @Override
    public Result execute(Transaction txn,
                          PartitionId partitionId,
                          OperationHandler operationHandler) {

        checkPermission();
        TableOperationHandler.checkTable(operationHandler, getTableId());

        final ReturnResultValueVersion prevVal =
            new ReturnResultValueVersion(prevValChoice);

        final Version newVersion = operationHandler.put
            (txn, partitionId, getKeyBytes(), getValueBytes(), prevVal);

        return new Result.PutResult(getOpCode(), prevVal.getValueVersion(),
                                    newVersion);
    }

    @Override
    public byte[] checkLOBSuffix(byte[] lobSuffixBytes) {
        return KVLargeObjectImpl.hasLOBSuffix(getKeyBytes(), lobSuffixBytes) ?
            getKeyBytes() :
            null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (tableId != 0) {
            sb.append(" Table Id ");
            sb.append(tableId);
        }
        sb.append(" Value: ");
        sb.append(requestValue);
        return sb.toString();
    }
}
