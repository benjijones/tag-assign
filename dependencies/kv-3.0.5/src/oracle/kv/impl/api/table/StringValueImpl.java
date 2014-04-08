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

package oracle.kv.impl.api.table;

import oracle.kv.table.FieldDef;
import oracle.kv.table.FieldValue;
import oracle.kv.table.StringValue;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.TextNode;

import com.sleepycat.persist.model.Persistent;

@Persistent(version=1)
class StringValueImpl extends FieldValueImpl implements StringValue {
    private static final long serialVersionUID = 1L;
    private final String value;
    private static final char MIN_VALUE_CHAR = ((char) 1);

    StringValueImpl(String value) {
        this.value = value;
    }

    /* DPL */
    @SuppressWarnings("unused")
    private StringValueImpl() {
        value = null;
    }

    public static StringValueImpl create(String value) {
        return new StringValueImpl(value);
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public FieldDef.Type getType() {
        return FieldDef.Type.STRING;
    }

    @Override
    public StringValueImpl clone() {
        return new StringValueImpl(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StringValueImpl) {
            return value.equals(((StringValueImpl)other).get());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(FieldValue other) {
        if (other instanceof StringValueImpl) {
            return value.compareTo(((StringValueImpl)other).value);
        }
        throw new ClassCastException
            ("Object is not an StringValue");
    }

    @Override
    public String formatForKey(FieldDef field) {
        return value;
    }

    /**
     * The "next" value, lexicographically, is this string with a
     * minimum character (1) added.
     */
    @Override
    public FieldValueImpl getNextValue() {
        return new StringValueImpl(value + MIN_VALUE_CHAR);
    }

    @Override
    public FieldValueImpl getMinimumValue() {
        throw new IllegalStateException
            ("StringValue.getMinimumValue should never be called");
    }

    @Override
    public JsonNode toJsonNode() {
        return new TextNode(value);
    }

    @Override
    public StringValue asString() {
        return this;
    }

    @Override
    public boolean isString() {
        return true;
    }
}