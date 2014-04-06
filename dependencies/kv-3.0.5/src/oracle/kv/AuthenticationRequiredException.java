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

/**
 * This exception is thrown when a secured operation is attempted and the
 * client is not currently authenticated. This can occur either if the client
 * did not supply login credentials either directly or by specifying a login
 * file, or it can occur if login credentials were specified, but the login
 * session has expired, requiring that the client reauthenticate itself.
 * The client application should reauthenticate before retrying the operation.
 *
 * @since 3.0
 */
public class AuthenticationRequiredException extends KVSecurityException {

    private static final long serialVersionUID = 1L;

    /*
     * When false, this indicates that the error applies to the credentials
     * provided by the authentication context that was current when an operation
     * was performed, and should be considered when the dispatch mechanism
     * decides whether to retry the operation. When true, this indicates that
     * the exception refers to authentication credentials passed explicitly in
     * operation arguments, and the exception is intended to signal a return
     * result directly to the caller. A value of true is normally specified
     * when the exception is thrown, and a new exception is thrown when caught
     * in the context of authentication credentials checking.
     */
    private final boolean isReturnSignal;

    /**
     * For internal use only.
     * @hidden
     */
    public AuthenticationRequiredException(String msg,
                                           boolean isReturnSignal) {
        super(msg);
        this.isReturnSignal = isReturnSignal;
    }

    /**
     * For internal use only.
     * @hidden
     */
    public AuthenticationRequiredException(Throwable cause,
                                           boolean isReturnSignal) {
        super(cause);
        this.isReturnSignal = isReturnSignal;
    }

    /**
     * For internal use only.
     * @hidden
     */
    public boolean getIsReturnSignal() {
        return isReturnSignal;
    }
}
