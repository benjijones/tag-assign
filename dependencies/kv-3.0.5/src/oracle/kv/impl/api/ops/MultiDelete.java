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

import oracle.kv.Depth;
import oracle.kv.KeyRange;
import oracle.kv.impl.api.ops.OperationHandler.KVAuthorizer;
import oracle.kv.impl.topo.PartitionId;

import com.sleepycat.je.Transaction;

/**
 * A multi-delete operation deletes records in the KV Store.
 */
public class MultiDelete extends MultiKeyOperation {

    /** LOB suffix is present at version 2 and greater. */
    private static final short LOB_SERIAL_VERSION = 2;

    /**
     * The LOB suffix bytes sent to the RN, so the RN can use them to ensure
     * that LOB objects in the range are not deleted.  Is null when an R1
     * client sends the request.
     */
    private final byte[] lobSuffixBytes;

    /**
     * Constructs a multi-delete operation.
     */
    public MultiDelete(byte[] parentKey,
                       KeyRange subRange,
                       Depth depth,
                       byte[] lobSuffixBytes) {
        super(OpCode.MULTI_DELETE, parentKey, subRange, depth);
        this.lobSuffixBytes = lobSuffixBytes;
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor first
     * to read common elements.
     */
    MultiDelete(ObjectInput in, short serialVersion)
        throws IOException {

        super(OpCode.MULTI_DELETE, in, serialVersion);

        if (serialVersion >= LOB_SERIAL_VERSION) {
            final int suffixLen = in.readShort();
            if (suffixLen == 0) {
                lobSuffixBytes = null;
            } else {
                lobSuffixBytes = new byte[suffixLen];
                in.readFully(lobSuffixBytes);
            }
        } else {
            lobSuffixBytes = null;
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

        if (serialVersion >= LOB_SERIAL_VERSION) {
            if ((lobSuffixBytes != null) && (lobSuffixBytes.length > 0)) {
                out.writeShort(lobSuffixBytes.length);
                out.write(lobSuffixBytes);
            } else {
                out.writeShort(0);
            }
        }
    }

    @Override
    public Result execute(Transaction txn,
                          PartitionId partitionId,
                          OperationHandler operationHandler) {

        final KVAuthorizer kvAuth = checkPermission();

        final int result = operationHandler.multiDelete
            (txn, partitionId, getParentKey(), getSubRange(), getDepth(),
             lobSuffixBytes, kvAuth);

        return new Result.MultiDeleteResult(getOpCode(), result);
    }
}
