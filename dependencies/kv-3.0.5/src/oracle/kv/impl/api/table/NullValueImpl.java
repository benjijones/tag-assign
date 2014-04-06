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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.NullNode;

import com.sleepycat.persist.model.Persistent;

/**
 * A class to encapsulate an explicitly null value for a field.  This class is
 * implemented as a singleton object and is never directly constructed or
 * accessed by applications.
 */
@Persistent(version=1)
class NullValueImpl extends FieldValueImpl {
    private static final long serialVersionUID = 1L;
    private static NullValueImpl instanceValue = new NullValueImpl();

    static NullValueImpl getInstance() {
        return instanceValue;
    }

    /* DPL */
    @SuppressWarnings("unused")
    private NullValueImpl() {
    }

    @Override
    public FieldDef.Type getType() {
        throw new UnsupportedOperationException
            ("Cannot get type from NullNode");
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public JsonNode toJsonNode() {
        return NullNode.getInstance();
    }

    @Override
    public NullValueImpl clone() {
        return getInstance();
    }
}



