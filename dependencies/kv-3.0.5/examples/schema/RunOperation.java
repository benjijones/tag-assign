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

package schema;

import oracle.kv.ConsistencyException;
import oracle.kv.DurabilityException;
import oracle.kv.FaultException;
import oracle.kv.RequestTimeoutException;

/**
 * Utility class to run a KVStore operation and handle {@code
 * FaultExceptions} according to this application's policies.  The
 * {@code doOperation} method is abstract and must be implemented by
 * callers.  {@code doOperation} is called by the {@code run()} method
 * of this class.
 *
 * <p>This class is a placeholder for implementing
 * application-specific exception handling policies.  This
 * implementation simply prints the exception details to {@code
 * System.err}.  In addition to performing other actions when an
 * exception occurs, applications may wish to extend this mechanism to
 * perform retries under certain conditions.</p>
 *
 * <p>Note that {@code OperationExecutionException} is not handled by
 * this class because it is a contingency exception that is expected
 * to be handled by the operation itself (the {@code doOperation}
 * method).</p>
 */
abstract class RunOperation {

    /**
     * Must be implemented to perform the operation.
     */
    abstract void doOperation();

    /**
     * Calls the doOperation method and handles exceptions as needed.
     */
    void run() throws FaultException {
        try {
            doOperation();
        } catch (ConsistencyException e) {
            handleConsistencyException(e);
        } catch (DurabilityException e) {
            handleDurabilityException(e);
        } catch (RequestTimeoutException e) {
            handleTimeoutException(e);
        } catch (FaultException e) {
            handleFaultException(e);
        } catch (RuntimeException e) {
            handleOtherException(e);
        }
    }

    /**
     * Placeholder for application-specific handling of DurabilityException.
     *
     * With the default durability configuration setting (see
     * KVStoreConfig.getDurability) this exception should rarely occur, but it
     * may need to be handled in certain circumstances. See the
     * DurabilityException javadoc for more information.
     */
    private void handleDurabilityException(DurabilityException e) {
        handleFaultException(e);
    }

    /**
     * Placeholder for application-specific handling of ConsistencyException.
     *
     * With the default consistency configuration setting (see
     * KVStoreConfig.getConsistency) this exception should rarely occur, but it
     * may need to be handled in certain circumstances. See the
     * ConsistencyException javadoc for more information.
     */
    private void handleConsistencyException(ConsistencyException e) {
        handleFaultException(e);
    }

    /**
     * Placeholder for application-specific handling of
     * RequestTimeoutException.
     *
     * With the default request timeout configuration setting (see
     * KVStoreConfig.getRequestTimeout) this exception should rarely occur, but
     * it may need to be handled in certain circumstances.  See the
     * RequestTimeoutException javadoc for more information.
     */
    private void handleTimeoutException(RequestTimeoutException e) {
        handleFaultException(e);
    }

    /**
     * Placeholder for application-specific handling of FaultException.  This
     * is the base class for unchecked (runtime) exceptions thrown by KVStore.
     *
     * When the exception WAS logged remotely, the client may wish to log the
     * exception for correlating it with KVStore service logs.  When the
     * exception was NOT logged remotely, the client may wish to log and
     * additionally perform necessary notifications.  See the FaultException
     * javadoc for more information.
     */
    private void handleFaultException(FaultException e) {
        /* Can be correlated with server logs using time and fault name. */
        System.err.println("Time: " + System.currentTimeMillis() +
                           " Fault: " + e.getFaultClassName());
        e.printStackTrace();
        if (!e.wasLoggedRemotely()) {
            /* Do any necessary notifications here. */
        }
    }

    /**
     * Placeholder for application-specific handling of unexpected runtime
     * exceptions.
     */
    private void handleOtherException(RuntimeException e) {
        e.printStackTrace();
    }
}
