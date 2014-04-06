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

import java.util.Arrays;

import oracle.kv.table.FieldDef;
import oracle.kv.table.FieldValue;
import oracle.kv.table.FixedBinaryDef;
import oracle.kv.table.FixedBinaryValue;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.BinaryNode;

import com.sleepycat.persist.model.Persistent;

@Persistent(version=1)
class FixedBinaryValueImpl extends FieldValueImpl
    implements FixedBinaryValue {
    private static final long serialVersionUID = 1L;
    private byte[] value;
    private final FixedBinaryDefImpl def;

    FixedBinaryValueImpl(byte[] value, FixedBinaryDefImpl def) {
        this.value = value;
        this.def = def;
    }

    /* DPL */
    @SuppressWarnings("unused")
    private FixedBinaryValueImpl() {
        def = null;
    }

    @Override
    public byte[] get() {
        return value;
    }

    @Override
    public FixedBinaryDef getDefinition() {
        return def;
    }

    @Override
    public FieldDef.Type getType() {
        return FieldDef.Type.FIXED_BINARY;
    }

    @Override
    public FixedBinaryValueImpl clone() {
        return new FixedBinaryValueImpl(value, def);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FixedBinaryValueImpl) {
            FixedBinaryValueImpl otherImpl = (FixedBinaryValueImpl)other;
            return (def.equals(otherImpl.def) &&
                    Arrays.equals(value, otherImpl.get()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    /**
     * TODO: maybe use JE comparator algorithm.
     * For now, all binary is equal
     */
    @Override
    public int compareTo(FieldValue other) {
        if (other instanceof FixedBinaryValueImpl) {
            return 0;
        }
        throw new ClassCastException
            ("Object is not an FixedBinaryValue");
    }

    @Override
    public JsonNode toJsonNode() {
        return new BinaryNode(value);
    }

    @Override
    public FixedBinaryValue asFixedBinary() {
        return this;
    }

    @Override
    public boolean isFixedBinary() {
        return true;
    }

    /**
     * Overrides the FieldValueImpl method to make sure that the correct schema
     * is used when constructing the GenericEnumSymbol instance.
     */
    @Override
    Object toAvroValue(Schema schema) {
        Schema toUse = getUnionSchema(schema, Schema.Type.FIXED);
        return new GenericData.Fixed(toUse, get());
    }
}



