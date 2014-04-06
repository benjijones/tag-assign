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

/**
 * Denotes a TableOperation in a sequence of operations passed to the {@link
 * TableAPI#execute TableAPI.execute} method.
 *
 * <p>TableOperation instances are created only by
 * {@link TableOperationFactory} methods
 * and the TableOperation interface should not be implemented by the
 * application.</p>
 *
 * @see TableOperationFactory
 * @see TableAPI#execute TableAPI.execute
 *
 * @since 3.0
 */
public interface TableOperation {

    /**
     * The type of operation, as determined by the method used to create it.
     */
    public enum Type {

        /**
         * An operation created by
         * {@link TableOperationFactory#createPut}.
         */
        PUT,

        /**
         * An operation created by
         * {@link TableOperationFactory#createPutIfAbsent}.
         */
        PUT_IF_ABSENT,

        /**
         * An operation created by
         * {@link TableOperationFactory#createPutIfPresent}.
         */
        PUT_IF_PRESENT,

        /**
         * An operation created by
         * {@link TableOperationFactory#createPutIfVersion}.
         */
        PUT_IF_VERSION,

        /**
         * An operation created by {@link TableOperationFactory#createDelete}.
         */
        DELETE,

        /**
         * An operation created by {@link
         * TableOperationFactory#createDeleteIfVersion}.
         */
        DELETE_IF_VERSION,
    }

    /**
     * Returns the Row associated with the operation if it is a put operation,
     * otherwise return null.
     *
     * @return the row or null
     */
    Row getRow();

    /**
     * Returns the PrimaryKey associated with the operation if it is a
     * delete operation, otherwise return null.
     *
     * @return the primary key or null
     */
    PrimaryKey getPrimaryKey();

    /**
     * Returns the operation Type.
     *
     * @return the type
     */
    Type getType();

    /**
     * Returns whether this operation should cause the {@link TableAPI#execute
     * TableAPI.execute} transaction to abort when the operation fails.
     *
     * @return true if operation failure should cause the entire execution to
     * abort
     */
    boolean getAbortIfUnsuccessful();
}
