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

import static oracle.kv.impl.api.table.JsonUtils.MIN;
import static oracle.kv.impl.api.table.JsonUtils.MAX;

import oracle.kv.impl.util.SortableString;
import oracle.kv.table.IntegerDef;

import com.sleepycat.persist.model.Persistent;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * IntegerDefImpl is an implementation of the IntegerDef interface.
 */
@Persistent(version=1)
class IntegerDefImpl extends FieldDefImpl
    implements IntegerDef {

    private static final long serialVersionUID = 1L;
    /*
     * These are not final to allow for schema evolution.
     */
    private final Integer min;
    private final Integer max;
    private int encodingLength;

    /**
     * Constructor requiring all fields.
     */
    public IntegerDefImpl(String description,
                          Integer min, Integer max) {
        super(Type.INTEGER, description);
        this.min = min;
        this.max = max;
        validate();
    }

    /**
     * This constructor defaults most fields.
     */
    public IntegerDefImpl() {
        super(Type.INTEGER);
        min = null;
        max = null;
        encodingLength = 0; /* 0 means full-length */
    }

    private IntegerDefImpl(IntegerDefImpl impl) {
        super(impl);
        min = impl.min;
        max = impl.max;
        encodingLength = impl.encodingLength;
    }

    @Override
    public Integer getMin() {
        return min;
    }

    @Override
    public Integer getMax() {
        return max;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public IntegerDef asInteger() {
        return this;
    }

    @Override
    public boolean isValidKeyField() {
        return true;
    }

    @Override
    public boolean isValidIndexField() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntegerDefImpl) {
            IntegerDefImpl otherDef = (IntegerDefImpl) other;
            return (compare(getMin(), otherDef.getMin()) &&
                    compare(getMax(), otherDef.getMax()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() +
            (min != null ? min.hashCode() : 0) +
            (max != null ? max.hashCode() : 0);
    }

    @Override
    int getEncodingLength() {
        return encodingLength;
    }

    @Override
    void toJson(ObjectNode node) {
        super.toJson(node);
        /*
         * Add min, max
         */
        if (min != null) {
            node.put(MIN, min);
        }
        if (max != null) {
            node.put(MAX, max);
        }
    }

    @Override
    public IntegerDefImpl clone() {
        return new IntegerDefImpl(this);
    }

    @Override
    public IntegerValueImpl createInteger(int value) {
        validateRange(value);
        return new IntegerValueImpl(value);
    }

    private void validate() {
        /* Make sure min <= max */
        if (min != null && max != null) {
            if (min > max) {
                throw new IllegalArgumentException
                    ("Invalid min or max value");
            }
        }
        encodingLength = SortableString.encodingLength(min, max);
    }

    /**
     * Validates the value against the range if one exists.
     */
    private void validateRange(int val) {

        /* min/max are inclusive */
        if ((min != null && val < min) ||
            (max != null && val > max)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Value, ");
            sb.append(val);
            sb.append(", is outside of the allowed range");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @Override
    FieldValueImpl createValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return NullValueImpl.getInstance();
        }
        if (!node.isInt()) {
            throw new IllegalArgumentException
                ("Default value for type INTEGER is not int");
        }
        return createInteger(node.getIntValue());
    }
}
