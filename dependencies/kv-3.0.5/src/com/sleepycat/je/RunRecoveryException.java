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

package com.sleepycat.je;

/**
 * This base class of {@link EnvironmentFailureException} is deprecated but
 * exists for API backward compatibility.
 *
 * <p>Prior to JE 4.0, {@code RunRecoveryException} is thrown to indicate that
 * the JE environment is invalid and cannot continue on safely.  Applications
 * catching {@code RunRecoveryException} prior to JE 4.0 were required to close
 * and re-open the {@code Environment}.</p>
 *
 * <p>When using JE 4.0 or later, the application should catch {@link
 * EnvironmentFailureException}. The application should then call {@link
 * Environment#isValid} to determine whether the {@code Environment} must be
 * closed and re-opened, or can continue operating without being closed.  See
 * {@link EnvironmentFailureException}.</p>
 *
 * @deprecated replaced by {@link EnvironmentFailureException} and {@link
 * Environment#isValid}.
 */
@Deprecated
public abstract class RunRecoveryException extends DatabaseException {

    private static final long serialVersionUID = 1913208269L;

    /** 
     * For internal use only.
     * @hidden 
     */
    public RunRecoveryException(String message) {
        super(message);
    }

    /** 
     * For internal use only.
     * @hidden 
     */
    public RunRecoveryException(String message, Throwable e) {
        super(message, e);
    }
}
