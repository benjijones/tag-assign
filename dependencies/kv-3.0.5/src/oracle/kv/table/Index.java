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

import java.io.InputStream;
import java.util.List;

/**
 * Index represents an index on a table in Oracle NoSQL Database.  It is an
 * immutable object created from system metadata.  Index is used to examine
 * index metadata and used as a factory for {@link IndexKey} objects used
 * for IndexKey operations in {@link TableAPI}.
 * <p>
 * Indexes are created and managed using the administrative command line
 * interface.
 *
 * @since 3.0
 */
public interface Index {

    /**
     * Returns the Table on which the index is defined.
     *
     * @return the table
     */
    Table getTable();

    /**
     * Returns the name of the index.
     *
     * @return the index name
     */
    String getName();

    /**
     * Returns the field names that define the index.  These are in order of
     * declaration which is significant.
     *
     * @return the field names
     */
    List<String> getFields();

    /**
     * Gets the index's description if present, otherwise null.  This is a
     * description of the index that is optionally supplied during
     * definition of the index.
     *
     * @return the description or null
     */
    String getDescription();

    /**
     * Creates an {@code IndexKey} for this index.  The returned key can only
     * hold fields that are part of this. Other fields are rejected if an
     * attempt is made to set them on the returned object.
     *
     * @return an empty index key based on the index
     */
    IndexKey createIndexKey();

    /**
     * Creates an {@code IndexKey} for the index populated relevant fields from
     * the {@code RecordValue} parameter.  Fields that are not part of the
     * index key are silently ignored.
     *
     * @param value a {@code RecordValue} instance
     *
     * @return an {@code IndexKey} containing relevant fields from the value
     */
    IndexKey createIndexKey(RecordValue value);

    /**
     * Creates an {@code IndexKey} based on JSON input.  If the {@code exact}
     * parameter is true the input string must contain an exact match to the
     * index key.  It must not have additional data.  If false, only matching
     * fields will be added and the input may have additional, unrelated data.
     *
     * @param jsonInput a JSON string
     *
     * @param exact set to true for an exact match.  See above
     *
     * @throws IllegalArgumentException if exact is true and a field is
     * missing or extra.  It will also be thrown if a field type or value is
     * not correct
     *
     * @throws IllegalArgumentException if the input is malformed
     */
    IndexKey createIndexKeyFromJson(String jsonInput,
                                    boolean exact);

    /**
     * Creates an {@code IndexKey} based on JSON input.  If the {@code exact}
     * parameter is true the input string must contain an exact match to the
     * index key.  It must not have additional data.  If false, only matching
     * fields will be added and the input may have additional, unrelated data.
     *
     * @param jsonInput a JSON string
     *
     * @param exact set to true for an exact match.  See above
     *
     * @throws IllegalArgumentException if exact is true and a field is
     * missing or extra.  It will also be thrown if a field type or value is
     * not correct
     *
     * @throws IllegalArgumentException if the input is malformed
     */
    IndexKey createIndexKeyFromJson(InputStream jsonInput,
                                    boolean exact);

    /**
     * Creates a {@code FieldRange} object used to specify a value range for
     * use in a index iteration operation in {@link TableAPI}.
     *
     * @param fieldName the name of the field from the index
     * to use for the range
     *
     * @throws IllegalArgumentException if the field is not defined in the
     * index
     *
     * @return an empty {@code FieldRange} based on the index
     */
    FieldRange createFieldRange(String fieldName);
}

