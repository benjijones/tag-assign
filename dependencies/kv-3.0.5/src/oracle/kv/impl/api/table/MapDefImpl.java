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

import static oracle.kv.impl.api.table.JsonUtils.COLLECTION;
import oracle.kv.table.FieldDef;
import oracle.kv.table.MapDef;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.sleepycat.persist.model.Persistent;

/**
 * MapDefImpl implements the MapDef interface.
 */
@Persistent(version=1)
class MapDefImpl extends FieldDefImpl
    implements MapDef {

    private static final long serialVersionUID = 1L;
    private final FieldDefImpl element;

    MapDefImpl(FieldDefImpl element,
               String description) {
        super(FieldDef.Type.MAP, description);
        if (element == null) {
            throw new IllegalArgumentException
                ("Map has no field and cannot be built");
        }
        this.element = element;
    }

    MapDefImpl(FieldDefImpl element) {
        this(element, null);
    }

    private MapDefImpl(MapDefImpl impl) {
        super(impl);
        element = impl.element.clone();
    }

    /**
     * For DPL
     */
    @SuppressWarnings("unused")
    private MapDefImpl() {
        element = null;
    }

    @Override
    public FieldDef getElement() {
        return element;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public MapDef asMap() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MapDefImpl) {
            return element.equals(((MapDefImpl)other).getElement());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public MapValueImpl createMap() {
        return new MapValueImpl(this);
    }

    @Override
    void toJson(ObjectNode node) {
        super.toJson(node);
        ObjectNode collNode = node.putObject(COLLECTION);
        if (element != null) {
            element.toJson(collNode);
        }
    }

    /**
     * {
     *  "type": {
     *    "type" : "map",
     *    "values" : "simpleType"  or for a complex type
     *    "values" : {
     *        "type" : ...
     *        ...
     *     }
     *  }
     * }
     */
    @Override
    public JsonNode mapTypeToAvro(ObjectNode node) {
        if (node == null) {
            node = JsonUtils.createObjectNode();
        }

        node.put("type", "map");
        node.put("values", element.mapTypeToAvroJsonNode());
        return node;
    }

    @Override
    public MapDefImpl clone() {
        return new MapDefImpl(this);
    }

    @Override
    FieldValueImpl createValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return NullValueImpl.getInstance();
        }
        if (!node.isObject()) {
            throw new IllegalArgumentException
                ("Default value for type MAP is not a map");
        }
        if (node.size() != 0) {
            throw new IllegalArgumentException
                ("Default value for map must be null or an empty map");
        }
        return createMap();
    }
}
