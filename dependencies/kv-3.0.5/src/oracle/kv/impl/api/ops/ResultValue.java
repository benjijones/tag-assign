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

import oracle.kv.Value;
import oracle.kv.impl.util.FastExternalizable;
import oracle.kv.impl.util.UserDataControl;

/**
 * Holds a Value for a result, optimized to avoid array allocations/copies.
 * Value is serialized on the service from a byte array and deserialized on
 * the client into a Value object.
 */
class ResultValue implements FastExternalizable {

    /* The value field is non-null on the client and null on the service. */
    private final Value value;
    /* The bytes field is non-null on the service and null on the client. */
    private final byte[] bytes;
    /* A byte array to represent an empty record on the wire to the client */
    private static final byte[] EMPTY_BYTES = new byte[] {0};

    /**
     * Used by the service.
     */
    ResultValue(byte[] bytes) {
        this.value = null;
        if (bytes == null || bytes.length == 0) {
            this.bytes = EMPTY_BYTES;
        } else {
            this.bytes = bytes;
        }
    }

    /**
     * Deserialize into byte array.
     * Used by the client when deserializing a response.
     */
    public ResultValue(ObjectInput in, short serialVersion)
        throws IOException {

        bytes = null;
        value = new Value(in, serialVersion);
    }

    /**
     * FastExternalizable writer.
     * Used by the service when serializing a response.
     */
    @Override
    public void writeFastExternal(ObjectOutput out,
                                  short serialVersion)
        throws IOException {

        if (value != null) {
            value.writeFastExternal(out, serialVersion);
        } else {
            Value.writeFastExternal(out, serialVersion, bytes);
        }
    }

    /**
     * Used by the client.
     */
    Value getValue() {
        if (value == null) {
            /* Only occurs in tests when RMI is bypassed. */
            return Value.fromByteArray(bytes);
        }
        return value;
    }

    @Override
    public String toString() {
        return UserDataControl.displayValue(value, bytes);
    }
}
