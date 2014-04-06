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

package oracle.kv;

import java.util.concurrent.TimeUnit;

/**
 * Thrown by {@link KVStore#storeIterator(Direction, int, Key, KeyRange, Depth,
 * Consistency, long, TimeUnit, StoreIteratorConfig)} when an exception
 * occurs. The underlying exception may be retrieved using the {@link
 * #getCause()} method. storeIterator results sets are generally retrieved in
 * batches using a specific key for each batch. If an exception occurs during a
 * retrieval, the key used to gather that batch of records is available to the
 * application with the {@link #getKey} method. This might be useful, for
 * instance, to determine approximately how far an iteration had progressed
 * when the exception occurred.
 *
 * A StoreIteratorException being thrown from {@link
 * ParallelScanIterator#next()} method does not necessarily close or invalidate
 * the iterator. Repeated calls to next() may or may not cause an exception to
 * be thrown. It is incumbent on the caller to determine the type of exception
 * and act accordingly.
 */
public class StoreIteratorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Key key;

    /**
     * For internal use only.
     * @hidden
     */
    public StoreIteratorException(Throwable cause, Key key) {
        super(cause);
        this.key = key;
    }

    /**
     * Returns the key which was used to retrieve the current batch of records
     * in the iteration. Because batches are generally more than a single
     * record so the key may not be the key of the same record which caused the
     * fault.
     */
    public Key getKey() {
        return key;
    }
}
