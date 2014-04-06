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

import java.io.IOException;

import oracle.kv.impl.util.SortableString;
import oracle.kv.table.FloatValue;
import oracle.kv.table.FieldDef;
import oracle.kv.table.FieldValue;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.node.ValueNode;
import org.codehaus.jackson.map.SerializerProvider;

import com.sleepycat.persist.model.Persistent;

@Persistent(version=1)
class FloatValueImpl extends FieldValueImpl implements FloatValue {
    private static final long serialVersionUID = 1L;
    private float value;

    FloatValueImpl(float value) {
        this.value = value;
    }

    /**
     * This constructor creates FloatValueImpl from the String format used for
     * sorted keys.
     */
    FloatValueImpl(String keyValue) {
        this.value = SortableString.floatFromSortable(keyValue);
    }

    /* DPL */
    @SuppressWarnings("unused")
    private FloatValueImpl() {
    }

    @Override
    public float get() {
        return value;
    }

    @Override
    public FieldDef.Type getType() {
        return FieldDef.Type.FLOAT;
    }

    @Override
    public FloatValueImpl clone() {
        return new FloatValueImpl(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FloatValueImpl) {
            /* == doesn't work for the various Float constants */
            return Float.compare(value,((FloatValueImpl)other).get()) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((Float) value).hashCode();
    }

    @Override
    public int compareTo(FieldValue other) {
        if (other instanceof FloatValueImpl) {
            return Float.compare(value, ((FloatValueImpl)other).value);
        }
        throw new ClassCastException
            ("Object is not an FloatValue");
    }

    @Override
    public String formatForKey(FieldDef field) {
        return SortableString.toSortable(value);
    }

    @Override
    public FieldValueImpl getNextValue() {
        if (value == Float.MAX_VALUE) {
            return null;
        }
        return new FloatValueImpl(Math.nextAfter(value,
                                                 Float.MAX_VALUE));
    }

    @Override
    public FieldValueImpl getMinimumValue() {
        return new FloatValueImpl(Float.MIN_VALUE);
    }

    @Override
    public FloatValue asFloat() {
        return this;
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    /**
     * Jackson does not have a FloatNode for the object node representation.
     * There is a FloatNode implementation below that works for serializing
     * the value to JSON.
     */
    @Override
    public JsonNode toJsonNode() {
        return new FloatNode(value);
    }

    /**
     * Jackson 1.9 does not have a FloatNode.  The implementation of
     * toJsonString() uses toJsonNode() which requires a JsonNode
     * implementation for this type.  This class is a minimal implementation
     * that works for serialization of a Float value as a string for JSON
     * output.
     *
     * It is only used by toJsonNode(), which is only used
     * for FieldValue.toJsonString().  If another mechanism is used for
     * that output this can go away.  Also, if an upgrade to Jackson
     * 2.x is done it can go away, as that version has a FloatNode.
     *
     * TODO: implement another way to do toJsonString()
     */
    private static final class FloatNode extends ValueNode {
        private final float value;

        FloatNode(float value) {
            this.value = value;
        }

        /**
         * This is the only method that matters.  The others exist because they
         * are abstract in the base.
         */
        @Override
        public final void serialize(JsonGenerator jg, SerializerProvider provider)
            throws IOException, JsonProcessingException
        {
            jg.writeNumber(value);
        }

        @Override
        public JsonToken asToken() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String getValueAsText() {
            return asText();
        }

        @Override
        public String asText() {
            return ((Float)value).toString();
        }
    }
}
