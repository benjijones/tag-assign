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

import oracle.kv.impl.util.SortableString;
import oracle.kv.table.FieldDef;
import oracle.kv.table.FieldValue;
import oracle.kv.table.LongValue;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.LongNode;

import com.sleepycat.persist.model.Persistent;

@Persistent(version=1)
class LongValueImpl extends FieldValueImpl implements LongValue {
    private static final long serialVersionUID = 1L;
    private final long value;

    LongValueImpl(long value) {
        this.value = value;
    }

    /**
     * This constructor creates LongValueImpl from the String format used for
     * sorted keys.
     */
    LongValueImpl(String keyValue) {
        this.value = SortableString.longFromSortable(keyValue);
    }

    /* DPL */
    @SuppressWarnings("unused")
    private LongValueImpl() {
        value = 0;
    }

    @Override
    public long get() {
        return value;
    }

    @Override
    public FieldDef.Type getType() {
        return FieldDef.Type.LONG;
    }

    @Override
    public LongValueImpl clone() {
        return new LongValueImpl(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof LongValueImpl) {
            return value == ((LongValueImpl)other).get();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Long) value).hashCode();
    }

    @Override
    public int compareTo(FieldValue other) {
        if (other instanceof LongValueImpl) {
            /* java 7
            return Long.compare(value, ((LongValueImpl)other).value);
            */
            return ((Long)value).compareTo(((LongValueImpl)other).value);
        }
        throw new ClassCastException
            ("Object is not an LongValue");
    }

    @Override
    public String formatForKey(FieldDef field) {
        int len = (field != null ?
                   ((LongDefImpl) field).getEncodingLength() : 0);
        return SortableString.toSortable(value, len);
    }

    @Override
    public FieldValueImpl getNextValue() {
        if (value == Long.MAX_VALUE) {
            return null;
        }
        return new LongValueImpl(value + 1L);
    }

    @Override
    public FieldValueImpl getMinimumValue() {
        return new LongValueImpl(Long.MIN_VALUE);
    }

    @Override
    public JsonNode toJsonNode() {
        return new LongNode(value);
    }

    @Override
    public LongValue asLong() {
        return this;
    }

    @Override
    public boolean isLong() {
        return true;
    }
}



