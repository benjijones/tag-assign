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

package oracle.kv.impl.rep.admin;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import oracle.kv.impl.fault.ClientAccessException;
import oracle.kv.impl.fault.ProcessExitCode;
import oracle.kv.impl.rep.RepNodeService;
import oracle.kv.impl.rep.RepNodeServiceFaultHandler;

/**
 * Specializes the RepNodeServiceFaultHandler so that all thrown exceptions are
 * wrapped inside a RepNodeAdminFaultException.
 */
public class RepNodeAdminFaultHandler extends RepNodeServiceFaultHandler {

    /**
     * Tracks the number of requests that are currently active.
     */
    private final AtomicInteger activeRequests = new AtomicInteger(0);

    public RepNodeAdminFaultHandler(RepNodeService repNodeService,
                                    Logger logger,
                                    ProcessExitCode defaultExitCode) {
        super(repNodeService, logger, defaultExitCode);
    }

    public int getActiveRequests() {
        return activeRequests.get();
    }

    /**
     * Wrap it inside a RepNodeAdminFaultException.
     */
    @Override
    protected RuntimeException getThrowException(RuntimeException fault) {
        if (fault instanceof ClientAccessException) {
            /*
             * This is a security exception generated by the client.
             * Unwrap it so that the client sees it in its orginal form.
             */
            return ((ClientAccessException) fault).getCause();
        }

        return new RepNodeAdminFaultException(fault);
    }


    @Override
    public <R, E extends Exception> R execute(Operation<R, E> operation)
        throws E {

        activeRequests.incrementAndGet();
        try {
            return super.execute(operation);
        } finally {
            activeRequests.decrementAndGet();
        }
    }

    @Override
    public <R> R execute(SimpleOperation<R> operation) {

        activeRequests.incrementAndGet();
        try {
            return super.execute(operation);
        } finally {
            activeRequests.decrementAndGet();
        }
    }


    @Override
    public <E extends Exception> void execute(Procedure<E> proc)
        throws E {

        activeRequests.incrementAndGet();
        try {
            super.execute(proc);
        } finally {
            activeRequests.decrementAndGet();
        }
    }


    @Override
    public void execute(SimpleProcedure proc) {

        activeRequests.incrementAndGet();
        try {
            super.execute(proc);
        } finally {
            activeRequests.decrementAndGet();
        }
    }
}