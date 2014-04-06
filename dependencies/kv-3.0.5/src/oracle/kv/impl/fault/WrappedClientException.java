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

/**
 * A wrapper exception used to wrap known serializable java runtime exceptions
 * that the client is guaranteed to have on its classpath back to the client.
 * The exception is wrapped as its cause. The RequestDispatcher at the client
 * unwraps the <i>cause</i> and throws the wrapped exception. Note that this
 * type of wrapping is different from the wrapping done via a FaultException
 * which is passed "as is" back to the client.
 *
 * The prototypical example motivating this type of wrapping is an
 * IllegalArgumentException in instances where the check can only be performed
 * remotely on the RN and not locally at the client.
 */
public class WrappedClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WrappedClientException(RuntimeException wrappedException) {
        super(wrappedException);
    }
}
