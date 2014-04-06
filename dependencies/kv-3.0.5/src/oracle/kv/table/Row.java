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

import oracle.kv.KVStore;
import oracle.kv.Version;

/**
 * Row is a specialization of RecordValue to represent a single record,
 * or row, in a table.  It is a central object used in most table operations
 * defined in {@link TableAPI}.
 *<p>
 * Row objects are constructed explicitly using
 * {@link Table#createRow createRow} or implicitly when returned from table
 * access operations.
 *
 * @since 3.0
 */
public interface Row extends RecordValue {

    /**
     * Return the Table associated with this row.
     *
     * @return the Table
     */
    Table getTable();

    /**
     * Returns the Version for the row.  The description of {@link Version}
     * in its class description refers to its use in the {@link KVStore}
     * interface.  In {@link TableAPI} it it used as a return value for the
     * various put methods as well as {@link TableAPI#putIfVersion} and
     * {@link TableAPI#deleteIfVersion} to perform conditional updates to
     * allow an application to ensure that an update is occurring on the
     * desired version of a row.
     *
     * @return the Version object if it has been initialized, null
     * otherwise.  The Version will only be set if the row was retrieved from
     * the store.
     */
    Version getVersion();

    /**
     * Creates a PrimaryKey from this Row.  The non-key fields are
     * removed.
     *
     * @return the PrimaryKey
     */
    PrimaryKey createPrimaryKey();

    /**
     * Returns the version of the table used to create this row if it has been
     * deserialized from a get operation.  If the row has been created and
     * never been serialized the version returned is 0.  New Table versions
     * are created when a table is schema evolved.  Tables start out with
     * version 1 and it increments with each change.  This method can be used
     * by applications to help handle version changes.
     */
    int getTableVersion();

    /**
     * Equality comparison for Row instances is based on equality of the
     * individual field values and ignores the included {@link Version}, if
     * present.
     *
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object other);

    /**
     * Returns a deep copy of this object.
     *
     * @return a deep copy of this object
     */
    @Override
    public Row clone();
}
