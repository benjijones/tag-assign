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

package oracle.kv.impl.api;

import java.util.concurrent.TimeUnit;

import oracle.kv.Consistency;
import oracle.kv.Depth;
import oracle.kv.Direction;
import oracle.kv.KeyRange;

/**
 * StoreIteratorParams serves two purposes:
 *
 * (1) it is a struct to hold the arguments to KVStore.storeIterator() so that
 * they can be passed around as a single arg rather than the usual 9 args.
 *
 * (2) it isolates out the two operations that are different for storeIterator
 * and storeKeysIterator. generateGetterOp() generates either a StoreKeysIterate
 * or StoreIterate operation to be passed to the server side. Since these two
 * operations return different types to the client (byte[] and KeyValueVersion)
 * convertResults() makes the right conversions depending on the type of
 * operation.
 *
 * ConvertResultsReturnValue is a simple struct to hold multiple return values
 * from the convertResults method. If Java provided multi-value returns, then
 * we wouldn't need this.
 */
public class StoreIteratorParams {
    protected final byte[] parentKeyBytes;
    protected final Direction direction;
    protected final int batchSize;
    protected final KeyRange subRange;
    protected final Depth depth;
    protected final Consistency consistency;
    protected final long timeout;
    protected final TimeUnit timeoutUnit;

    public StoreIteratorParams(final Direction direction,
                               final int batchSize,
                               final byte[] parentKeyBytes,
                               final KeyRange subRange,
                               final Depth depth,
                               final Consistency consistency,
                               final long timeout,
                               final TimeUnit timeoutUnit) {

        /*
         * Set default values for batchSize and Depth if not set.
         */
        this.batchSize = (batchSize > 0 ? batchSize :
                          KVStoreImpl.DEFAULT_ITERATOR_BATCH_SIZE);
        this.depth = (depth != null ? depth : Depth.PARENT_AND_DESCENDANTS);

        /*
         * Caller has validated direction.  If it is UNORDERED, turn into
         * FORWARD because that is what will be sent to each partition.
         */
        this.direction = (direction == Direction.UNORDERED ? Direction.FORWARD :
                          direction);
        this.parentKeyBytes = parentKeyBytes;
        this.subRange = subRange;
        this.consistency = consistency;
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public byte[] getParentKeyBytes() {
        return parentKeyBytes;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public KeyRange getSubRange() {
        return subRange;
    }

    public Depth getDepth() {
        return depth;
    }

    public Consistency getConsistency() {
        return consistency;
    }

    public long getTimeout() {
        return timeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }
}
