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
 * MapValue extends {@link FieldValue} to define a container object that holds
 * a map of FieldValue objects all of the same type.  The getters and setters
 * use the same semantics as Java Map.
 *
 * @since 3.0
 */
public interface MapValue extends FieldValue {

    /**
     * Returns the MapDef that defines the content of this map.
     *
     * @return the MapDef
     */
    MapDef getDefinition();

    /**
     * Returns the size of the map.
     *
     * @return the size
     */
    int size();

    /**
     * Remove the named field if it exists.
     *
     * @param fieldName the name of the field to remove
     *
     * @return the FieldValue if it existed, otherwise null
     */
    FieldValue remove(String fieldName);

    /**
     * Returns the FieldValue with the specified name if it
     * appears in the map.
     *
     * @param fieldName the name of the desired field.
     *
     * @return the value for the field or null if the name does not exist in
     * the map.
     */
    FieldValue get(String fieldName);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, int value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, long value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, String value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, double value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, float value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, boolean value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, byte[] value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue putFixed(String fieldName, byte[] value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue putEnum(String fieldName, String value);

    /**
     * Set the named field.  Any existing entry is silently overwritten.
     *
     * @param fieldName name of the desired field
     *
     * @param value the value to set
     *
     * @return this
     *
     * @throws IllegalArgumentException if the definition of the map type does
     * not match the input type
     */
    MapValue put(String fieldName, FieldValue value);

    /**
     * Puts a Record into the map.  Existing values are silently overwritten.
     *
     * @param fieldName the field to use for the map key
     *
     * @return an uninitialized RecordValue that matches the type
     * definition for the map
     *
     * @throws IllegalArgumentException if the definition of the map type
     * is not a RecordDef
     */
    RecordValue putRecord(String fieldName);

    /**
     * Puts a Map into the map.  Existing values are silently overwritten.
     *
     * @param fieldName the field to use for the map key
     *
     * @return an uninitialized MapValue that matches the type
     * definition for the map
     *
     * @throws IllegalArgumentException if the definition of the map type
     * is not a MapDef
     */
    MapValue putMap(String fieldName);

    /**
     * Puts an Array into the map.  Existing values are silently overwritten.
     *
     * @param fieldName the field to use for the map key
     *
     * @return an uninitialized ArrayValue that matches the type
     * definition for the map
     *
     * @throws IllegalArgumentException if the definition of the map type
     * is not an ArrayDef
     */
    ArrayValue putArray(String fieldName);

    /**
     * Returns a deep copy of this object.
     *
     * @return a deep copy of this object
     */
    @Override
    public MapValue clone();
}



