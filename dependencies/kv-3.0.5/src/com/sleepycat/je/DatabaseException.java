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
 * The root of all BDB JE-defined exceptions.
 *
 * <p>Exceptions thrown by BDB JE fall into three categories.</p>
 * <ol>
 * <li>When a method is used incorrectly as the result of an application
 * programming error, a standard Java runtime exception is thrown: {@link
 * IllegalArgumentException}, {@link IllegalStateException} or {@link
 * UnsupportedOperationException}.  These exceptions have the standard meaning
 * defined by their javadoc.  Note that JE throws {@link
 * IllegalArgumentException} rather than {@link NullPointerException} when a
 * required parameter is null.
 * </li>
 * <li>When an operation failure occurs, {@link OperationFailureException} or
 * one of its subclasses is thrown.  See {@link OperationFailureException} for
 * details.
 * </li>
 * <li>When an {@code Environment} failure occurs, {@link
 * EnvironmentFailureException} or one of its subclasses is thrown.  See {@link
 * EnvironmentFailureException} for details.
 * </li>
 * </ol>
 *
 * <p>{@link OperationFailureException} and {@link EnvironmentFailureException}
 * are the only two direct subclasses of {@code DatabaseException}.</p>
 *
 * <p>(Actually the above statement is not strictly correct.  {@link
 * EnvironmentFailureException} extends {@link RunRecoveryException} which
 * extends {@code DatabaseException}.  {@link RunRecoveryException} exists for
 * backward compatibility and has been deprecated. {@link
 * EnvironmentFailureException} should be used instead.)</p>
 *
 * <p>Note that in some cases, certain methods return status values without
 * issuing an exception. This occurs in situations that are not normally
 * considered an error, but when some informational status is returned.  For
 * example, {@link com.sleepycat.je.Database#get Database.get} returns {@link
 * com.sleepycat.je.OperationStatus#NOTFOUND OperationStatus.NOTFOUND} when a
 * requested key does not appear in the database.</p>
 */
public abstract class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1535562945L;

    private String extraInfo = null;

    /** 
     * For internal use only.
     * @hidden 
     */
    public DatabaseException(Throwable t) {
        super(getVersionHeader() + t.toString(), t);
    }

    /** 
     * For internal use only.
     * @hidden 
     */
    public DatabaseException(String message) {
        super(getVersionHeader() + message);
    }

    /** 
     * For internal use only.
     * @hidden 
     */
    public DatabaseException(String message, Throwable t) {
        super((getVersionHeader() + message), t);
    }

    /** 
     * For internal use only.
     * @hidden 
     * Utility for generating the version at the start of the exception 
     * message. Public for unit tests. 
     */
    public static String getVersionHeader() {
        return "(JE " + JEVersion.CURRENT_VERSION + ") ";
    }

    /**
     * For internal use only.
     * @hidden
     *
     * Support the addition of extra error information. Use this approach
     * rather than wrapping exceptions whenever possible for two reasons:
     * 1) so the user can catch the original exception class and handle it
     * appropriately, and 2) because the EnvironmentFailureException hierarchy
     * does some intricate things with setting the environment as invalid.
     *
     * @param newExtraInfo the message to add, not including separator space.
     */
    public void addErrorMessage(String newExtraInfo) {

        if (extraInfo == null) {
            extraInfo = " " + newExtraInfo;
        } else {
            extraInfo = extraInfo + ' ' + newExtraInfo;
        }
    }

    @Override
    public String getMessage() {

        /*
         * If extraInfo is null, don't allocate memory.  A Java Error may have
         * occurred.
         */
        if (extraInfo == null) {
            return super.getMessage();
        }

        return super.getMessage() + extraInfo;
    }
}
