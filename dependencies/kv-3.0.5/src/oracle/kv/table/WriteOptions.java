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

package oracle.kv.table;

import java.util.concurrent.TimeUnit;

import oracle.kv.Durability;
import oracle.kv.KVStoreConfig;

/**
 * WriteOptions is passed to store operations that can update the store to
 * specify non-default behavior relating to operation durability and timeouts.
 * <p>
 * The default behavior is configured when a store is opened using
 * {@link KVStoreConfig}.
 *
 * @since 3.0
 */
public class WriteOptions {

    private final Durability durability;
    private final long timeout;
    private final TimeUnit timeoutUnit;

    /**
     * Creates a {@code WriteOptions} with the specified parameters.
     * <p>
     * If {@code durability} is {@code null}, the
     * {@link KVStoreConfig#getDurability default durability} is used.
     * <p>
     * The {@code timeout} parameter is an upper bound on the time interval for
     * processing the operation.  A best effort is made not to exceed the
     * specified limit. If zero, the {@link KVStoreConfig#getRequestTimeout
     * default request timeout} is used.
     * <p>
     * If {@code timeout} is not 0, the {@code timeoutUnit} parameter must not
     * be {@code null}.
     *
     * @param durability the write durability to use
     * @param timeout the timeout value to use
     * @param timeoutUnit the {@link TimeUnit} used by the
     * <code>timeout</code> parameter
     *
     * @throws IllegalArgumentException if timeout is negative
     * @throws IllegalArgumentException if timeout is > 0 and timeoutUnit
     * is null
     */
    public WriteOptions(Durability durability,
                        long timeout,
                        TimeUnit timeoutUnit) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be >= 0");
        }
        if ((timeout != 0) && (timeoutUnit == null)) {
            throw new IllegalArgumentException("A non-zero timeout requires " +
                                               "a non-null timeout unit");
        }
        this.durability = durability;
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    /**
     * Gets the durability associated with the operation. If
     * null, the {@link KVStoreConfig#getDurability default durability} is
     * used.
     *
     * @return the durability or null
     */
    public Durability getDurability() {
        return durability;
    }

    /**
     * Gets the timeout, which is an upper bound on the time interval for
     * processing the operation.  A best effort is made not to exceed the
     * specified limit. If zero, the {@link KVStoreConfig#getRequestTimeout
     * default request timeout} is used.
     *
     * @return the timeout or zero
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Gets the unit of the timeout parameter, and may
     * be null only if {@link #getTimeout} returns zero.
     *
     * @return the {@code TimeUnit} or null
     */
    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }
}
