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

package oracle.kv.impl.api.parallelscan;

import java.io.Serializable;

import java.util.concurrent.atomic.AtomicLong;

import oracle.kv.stats.StoreIteratorMetrics;

/**
 * Holds the stats and metrics associated with a KVS Store Iterator and Store
 * Keys Iterator operations. This is only applicable to the Parallel Scan
 * invocation.
 */
public class StoreIteratorMetricsImpl
    implements Serializable, StoreIteratorMetrics {

    private static final long serialVersionUID = 1L;

    /* Number of times a put to the Results Queue blocked. */
    private AtomicLong blockedResultsQueuePuts = new AtomicLong();

    /* Cumulative time (ms) spent waiting to put into the Results Queue. */
    private AtomicLong blockedResultsQueuePutTime = new AtomicLong();

    /* Minimum time (ms) spent waiting to put into the Results Queue. */
    private AtomicLong minBlockedResultsQueuePutTime = new AtomicLong();

    /* Maximum time (ms) spent waiting to put into the Results Queue. */
    private AtomicLong maxBlockedResultsQueuePutTime = new AtomicLong();

    /* Number of times a take from the Results Queue blocked. */
    private AtomicLong blockedResultsQueueGets = new AtomicLong();

    /* Cumulative time (ms) spent waiting to take from the Results Queue. */
    private AtomicLong blockedResultsQueueGetTime = new AtomicLong();

    /* Minimum time (ms) spent waiting to take from the Results Queue. */
    private AtomicLong minBlockedResultsQueueGetTime = new AtomicLong();

    /* Maximum time (ms) spent waiting take from the Results Queue. */
    private AtomicLong maxBlockedResultsQueueGetTime = new AtomicLong();

    void accBlockedResultsQueuePutTime(long time) {
        if (time == 0) {
            return;
        }

        blockedResultsQueuePuts.incrementAndGet();
        blockedResultsQueuePutTime.addAndGet(time);
        setMinBlockedResultsQueuePutTime(time);
        setMaxBlockedResultsQueuePutTime(time);
    }

    private void setMinBlockedResultsQueuePutTime(long min) {
        while (true) {
            final long currentMin = minBlockedResultsQueuePutTime.get();
            if (currentMin > min) {
                if (!minBlockedResultsQueuePutTime.compareAndSet
                    (currentMin, min)) {
                    continue;
                }
            }
            break;
        }
    }

    private void setMaxBlockedResultsQueuePutTime(long max) {
        while (true) {
            final long currentMax = maxBlockedResultsQueuePutTime.get();
            if (currentMax < max) {
                if (!maxBlockedResultsQueuePutTime.compareAndSet
                    (currentMax, max)) {
                    continue;
                }
            }
            break;
        }
    }

    void accBlockedResultsQueueGetTime(long time) {
        if (time == 0) {
            return;
        }

        blockedResultsQueueGets.incrementAndGet();
        blockedResultsQueueGetTime.addAndGet(time);
        setMinBlockedResultsQueueGetTime(time);
        setMaxBlockedResultsQueueGetTime(time);
    }

    void setMinBlockedResultsQueueGetTime(long min) {
        while (true) {
            final long currentMin = minBlockedResultsQueueGetTime.get();
            if (currentMin > min) {
                if (!minBlockedResultsQueueGetTime.compareAndSet
                    (currentMin, min)) {
                    continue;
                }
            }
            break;
        }
    }

    void setMaxBlockedResultsQueueGetTime(long max) {
        while (true) {
            final long currentMax = maxBlockedResultsQueueGetTime.get();
            if (currentMax < max) {
                if (!maxBlockedResultsQueueGetTime.compareAndSet
                    (currentMax, max)) {
                    continue;
                }
            }
            break;
        }
    }

    /**
     * Returns the number of put() operations on the Results Queue on the
     * producer side.
     * <p>
     * These are generally the result of the client side not
     * being able to process results as fast as the parallel scan threads are
     * receiving them from the Replication Nodes.
     */
    @Override
    public long getBlockedResultsQueuePuts() {
        return blockedResultsQueuePuts.get();
    }

    /**
     * Returns the average time (in milliseconds) spent waiting for put()
     * operations on the Results Queue on the producer side.
     * <p>
     * Blocked puts are generally the result of the client side not
     * being able to process results as fast as the parallel scan threads are
     * receiving them from the Replication Nodes.
     */
    @Override
    public long getAverageBlockedResultsQueuePutTime() {
        final long bRQP = blockedResultsQueuePuts.get();
        if (bRQP == 0) {
            return 0;
        }
        return blockedResultsQueuePutTime.get() / bRQP;
    }

    /**
     * Returns the minimum time (in milliseconds) spent waiting for put()
     * operations on the Results Queue on the producer side.
     * <p>
     * Blocked puts are generally the result of the client side not
     * being able to process results as fast as the parallel scan threads are
     * receiving them from the Replication Nodes.
     */
    @Override
    public long getMinBlockedResultsQueuePutTime() {
        return minBlockedResultsQueuePutTime.get();
    }

    /**
     * Returns the maximum time (in milliseconds) spent waiting for put()
     * operations on the Results Queue on the producer side.
     * <p>
     * Blocked puts are generally the result of the client side not
     * being able to process results as fast as the parallel scan threads are
     * receiving them from the Replication Nodes.
     */
    @Override
    public long getMaxBlockedResultsQueuePutTime() {
        return maxBlockedResultsQueuePutTime.get();
    }

    /**
     * Returns the number of take() operations on the Results Queue on the
     * consumer (application) side.
     * <p>
     * These are generally the result of the parallel scan producer threads not
     * being able to gather results as fast as the application is able to
     * process them.
     */
    @Override
    public long getBlockedResultsQueueGets() {
        return blockedResultsQueueGets.get();
    }

    /**
     * Returns the average time (in milliseconds) spent waiting for take()
     * operations on the Results Queue on the consumer (application) side.
     * <p>
     * These are generally the result of the parallel scan producer threads not
     * being able to gather results as fast as the application is able to
     * process them.
     */
    @Override
    public long getAverageBlockedResultsQueueGetTime() {
        final long bRQG = blockedResultsQueueGets.get();
        if (bRQG == 0) {
            return 0;
        }
        return blockedResultsQueueGetTime.get() / bRQG;
    }

    /**
     * Returns the minimum time (in milliseconds) spent waiting for take()
     * operations on the Results Queue on the consumer (application) side.
     * <p>
     * These are generally the result of the parallel scan producer threads not
     * being able to gather results as fast as the application is able to
     * process them.
     */
    @Override
    public long getMinBlockedResultsQueueGetTime() {
        return minBlockedResultsQueueGetTime.get();
    }

    /**
     * Returns the maximum time (in milliseconds) spent waiting for take()
     * operations on the Results Queue on the consumer (application) side.
     * <p>
     * These are generally the result of the parallel scan producer threads not
     * being able to gather results as fast as the application is able to
     * process them.
     */
    @Override
    public long getMaxBlockedResultsQueueGetTime() {
        return maxBlockedResultsQueueGetTime.get();
    }

    public void clear() {
        blockedResultsQueuePuts.set(0);
        blockedResultsQueuePutTime.set(0);
        blockedResultsQueueGets.set(0);
        blockedResultsQueueGetTime.set(0);
        minBlockedResultsQueuePutTime.set(0);
        maxBlockedResultsQueuePutTime.set(0);
        minBlockedResultsQueueGetTime.set(0);
        maxBlockedResultsQueueGetTime.set(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StoreIteratorMetrics:");
        sb.append("\n blockedResultsQueuePuts=").
            append(blockedResultsQueuePuts.get());
        sb.append("\n averageBlockedResultsQueuePutTime=").
            append(getAverageBlockedResultsQueuePutTime());
        sb.append("\n minBlockedResultsQueuePutTime=").
            append(minBlockedResultsQueuePutTime.get());
        sb.append("\n maxBlockedResultsQueuePutTime=").
            append(maxBlockedResultsQueuePutTime.get());
        sb.append("\n blockedResultsQueueGets=").
            append(blockedResultsQueueGets.get());
        sb.append("\n averageBlockedResultsQueueGetTime=").
            append(getAverageBlockedResultsQueueGetTime());
        sb.append("\n minBlockedResultsQueueGetTime=").
            append(minBlockedResultsQueueGetTime.get());
        sb.append("\n maxBlockedResultsQueueGetTime=").
            append(maxBlockedResultsQueueGetTime.get());
        sb.append("\n");
        return sb.toString();
    }
}
