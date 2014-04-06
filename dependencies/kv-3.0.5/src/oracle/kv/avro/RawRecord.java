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

package oracle.kv.avro;

import java.util.Arrays;

import org.apache.avro.Schema;

/**
 * A RawRecord represents an Avro object as a Schema along with the raw Avro
 * serialized data. It is used with a {@link RawAvroBinding}.
 *
 * @see RawAvroBinding
 * @see AvroCatalog#getRawBinding getRawBinding
 *
 * @since 2.0
 */
public class RawRecord {
    private final byte[] rawData;
    private final Schema schema;

    /**
     * Creates a RawRecord from a Schema and Avro serialized data.
     */
    public RawRecord(byte[] rawData, Schema schema) {
        this.rawData = rawData;
        this.schema = schema;
    }

    /**
     * Returns the Avro serialized data for this RawRecord.
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * Returns the Avro Schema for this RawRecord.
     */
    public Schema getSchema() {
        return schema;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RawRecord)) {
            return false;
        }
        final RawRecord o = (RawRecord) other;
        return Arrays.equals(rawData, o.rawData) && schema.equals(o.schema);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(rawData);
    }

    @Override
    public String toString() {
        return Arrays.toString(rawData) + "\nSchema: " + schema.toString();
    }
}
