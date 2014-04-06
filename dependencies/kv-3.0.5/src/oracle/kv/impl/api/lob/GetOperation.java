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

package oracle.kv.impl.api.lob;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import oracle.kv.Consistency;
import oracle.kv.Key;
import oracle.kv.ValueVersion;
import oracle.kv.impl.api.KVStoreImpl;
import oracle.kv.lob.InputStreamVersion;
import oracle.kv.lob.KVLargeObject.LOBState;

/**
 * Implements the LOB get operation
 */
public class GetOperation extends ReadOperation {

    GetOperation(KVStoreImpl kvsImpl,
                 Key appLobKey,
                 Consistency consistency,
                 long chunkTimeout,
                 TimeUnit timeoutUnit) {

        super(kvsImpl, appLobKey, consistency, chunkTimeout, timeoutUnit);
    }

    InputStreamVersion execute() {

        final ValueVersion appValueVersion =
                kvsImpl.get(appLOBKey, consistency,
                            chunkTimeoutMs, TimeUnit.MILLISECONDS);

        if (appValueVersion == null) {
            return null;
        }

        internalLOBKey = valueToILK(appValueVersion.getValue());

        final ValueVersion metadataVV = initMetadata(LOBState.COMPLETE);

        final long lastSuperChunkId = lobProps.getLastSuperChunkId();
        if ((lastSuperChunkId > 1) &&
            (consistency instanceof Consistency.Version)) {
            String msg = "Version consistency cannot be used to read " +
            		"a LOB striped across more than one partition. " +
            		"This LOB is striped across " + lastSuperChunkId +
            		"partitions. Use a different consistency policy";
            throw new IllegalArgumentException(msg);
        }

        final InputStream inputStream = new
            ChunkEncapsulatingInputStream(this,
                                          metadataVV.getVersion());
        return new InputStreamVersion(inputStream,
                                      appValueVersion.getVersion());
    }
}
