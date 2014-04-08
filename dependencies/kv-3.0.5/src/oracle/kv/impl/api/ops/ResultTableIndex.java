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

import oracle.kv.impl.util.FastExternalizable;

/**
 * Holds results of table index iteration which includes primary key and index
 * key byte arrays.  This is all of the information that is available in a
 * single secondary scan without doing an additional database read of the
 * primary data.
 */
public class ResultTableIndex implements FastExternalizable {

    private final byte[] primaryKeyBytes;
    private final byte[] indexKeyBytes;

    public ResultTableIndex(byte[] primaryKeyBytes,
                            byte[] indexKeyBytes) {
        this.primaryKeyBytes = primaryKeyBytes;
        this.indexKeyBytes = indexKeyBytes;
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor
     * first to read common elements.
     */
    @SuppressWarnings("unused")
    public ResultTableIndex(ObjectInput in, short serialVersion)
        throws IOException {

        int keyLen = in.readShort();
        primaryKeyBytes = new byte[keyLen];
        in.readFully(primaryKeyBytes);
        keyLen = in.readShort();
        indexKeyBytes = new byte[keyLen];
        in.readFully(indexKeyBytes);
    }

    /**
     * FastExternalizable writer.  Must call superclass method first to
     * write common elements.
     */
    @SuppressWarnings("unused")
    @Override
    public void writeFastExternal(ObjectOutput out, short serialVersion)
        throws IOException {

        out.writeShort(primaryKeyBytes.length);
        out.write(primaryKeyBytes);
        out.writeShort(indexKeyBytes.length);
        out.write(indexKeyBytes);
    }

    public byte[] getPrimaryKeyBytes() {
        return primaryKeyBytes;
    }

    public byte[] getIndexKeyBytes() {
        return indexKeyBytes;
    }
}