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

package oracle.kv.impl.fault;

import java.io.PrintWriter;
import java.io.StringWriter;

import oracle.kv.KVVersion;

/**
 * An exception wrapper used to indicate a fault encountered in a server side
 * process while servicing an internal, non-data operation request.
 * Application-specific subclasses of this class are typically used by the
 * ProcessFaultHandler to throw an exception when processing such a request.
 *
 * <p>
 * Given the distributed nature of the KVS, the client may not have access to
 * the class associated with the "cause" object created by the server, or the
 * class definition may represent a different and potentially incompatible
 * version. This wrapper class ensures that the stack trace and textual
 * information is preserved and communicated to the client.
 */
public abstract class InternalFaultException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String faultClassName;
    private final String originalStackTrace;

    public InternalFaultException(Throwable cause) {
        super(cause.getMessage() + " (" +
              KVVersion.CURRENT_VERSION.getNumericVersionString() + ")");
        /* Preserve the stack trace and the exception class name. */
        final StringWriter sw = new StringWriter(500);
        cause.printStackTrace(new PrintWriter(sw));
        originalStackTrace = sw.toString();

        faultClassName = cause.getClass().getName();
    }

    /* The name of the original exception class, often used for testing. */
    public String getFaultClassName() {
        return faultClassName;
    }

    @Override
    public String toString() {
        return getMessage() + " " + originalStackTrace;
    }
}
