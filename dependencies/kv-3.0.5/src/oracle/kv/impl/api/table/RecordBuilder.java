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
import java.util.List;

import oracle.kv.table.FieldDef;
import oracle.kv.table.RecordDef;

import org.apache.avro.Schema;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.node.ObjectNode;

/*
 * Record builder
 */
public class RecordBuilder extends TableBuilderBase {
    private final String name;
    private String description;

    RecordBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    RecordBuilder(String name) {
        this.name = name;
    }

    @Override
    public String getBuilderType() {
        return "Record";
    }

    @Override
    public RecordDef build() {
        return new RecordDefImpl(name, fields, description);
    }

    @Override
    public TableBuilderBase addField(String name1, FieldDef field) {
        if (name1 == null) {
            throw new IllegalArgumentException
                ("Record fields must have names");
        }
        return super.addField(name1, field);
    }

    @Override
    public TableBuilderBase setDescription(final String description) {
        this.description = description;
        return this;
    }

    @SuppressWarnings("unused")
    @Override
    TableBuilderBase generateAvroSchemaFields(Schema schema,
                                              String name1,
                                              JsonNode defaultValue,
                                              String desc) {

        List<Schema.Field> schemaFields = schema.getFields();
        for (Schema.Field field : schemaFields) {
            super.generateAvroSchemaFields(field.schema(),
                                           field.name(),
                                           field.defaultValue(),
                                           field.doc());
        }
        return this;
    }

    /*
     * Create a JSON representation of the record field
     **/
    public String toJsonString(boolean pretty) {
        ObjectWriter writer = JsonUtils.createWriter(pretty);
        ObjectNode o = JsonUtils.createObjectNode();
        RecordDefImpl tmp = new RecordDefImpl(name, fields, description);
        tmp.toJson(o);
        try {
            return writer.writeValueAsString(o);
        } catch (IOException ioe) {
            return ioe.toString();
        }
    }
}
