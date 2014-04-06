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

package oracle.kv.impl.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * KVStore threads should have these characteristics:
 * - have a descriptive name that identifies it as a KVStore owned thread
 * - run as a daemon
 * - have an uncaught exception handler. By default, uncaught exceptions should
 *   log the exception into their local log.
 */
public class KVThreadFactory implements ThreadFactory {

    /* Factory name */
    private final String name;

    /* Used for logging uncaught exceptions. */
    private final Logger logger;
    private final AtomicInteger id;

    /**
     * @param name factory name, returned by getName()
     * @param exceptionLogger should be attached to a handler that is always
     * available to record uncaught exceptions. For example, it should at least
     * be able to preserve the uncaught exception in the kvstore component's
     * log file.
     */
    public KVThreadFactory(String name, Logger exceptionLogger) {
        this.name = name;
        this.logger = exceptionLogger;
        this.id = new AtomicInteger();
    }

    /**
     * Name of thread. All KVThreadFactory and subclasses prefix the
     * thread name with "KV". Other uses may want to override this method to
     * provide more specific names, such as KVMonitorCollector. Subclasses
     * need only specify the suffix, such as "MonitorCollector" in this
     * method.
     */
    public String getName() {
        return name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "KV" + getName() + "_" +
                              id.incrementAndGet());
        t.setDaemon(true);
        t.setUncaughtExceptionHandler(makeUncaughtExceptionHandler());
        return t;
    }

    /**
     * By default, these threads log uncaught exceptions. Can be overwritten
     * to provide different handling.
     */
    public Thread.UncaughtExceptionHandler makeUncaughtExceptionHandler() {
        return new LogUncaughtException();
    }

    private class LogUncaughtException
        implements Thread.UncaughtExceptionHandler {

        /**
         * TODO: this method hard codes the logger level. Is that okay, or is
         * more flexibility needed?
         */
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (logger != null) {
                logger.severe(t + " experienced this uncaught exception " + e);
            }
        }
    }
}
