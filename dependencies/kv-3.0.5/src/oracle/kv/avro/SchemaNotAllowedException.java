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

import oracle.kv.FaultException;

/**
 * Thrown when a schema is passed to a binding method that is not allowed for
 * the binding.
 * <p>
 * The schemas allowed for a particular binding are those specified when the
 * binding is created using one of the {@link AvroCatalog} getXxxBinding
 * methods.
 * <p>
 * This exception may indicate a programming error if the application uses a
 * value with the wrong binding.  In that case, when this exception is thrown
 * the client should treat it as if it were an {@link IllegalArgumentException}
 * and report an error at a higher level.
 * <p>
 * However, an application may also use this exception to determine whether a
 * binding supports the value's schema or not.  In that case, depending on the
 * nature of the application, when this exception is thrown the client may wish
 * to
 * <ul>
 * <li>use a different binding that supports the schema,or </li>
 * <li>ignore the value having the unknown schema.</li>
 * </ul>
 * <p>
 * See {@link GenericAvroBinding} and {@link JsonAvroBinding} for an example of
 * handling {@code SchemaNotAllowedException}.
 *
 * @since 2.0
 */
public class SchemaNotAllowedException extends FaultException {

    private static final long serialVersionUID = 1L;

    final private String schemaName;

    /**
     * For internal use only.
     * @hidden
     */
    public SchemaNotAllowedException(String msg, String schemaName) {
        super(msg, null /*cause*/, false /*isRemote*/);
        this.schemaName = schemaName;
    }

    /**
     * Returns the full name of the schema that is not allowed.
     */
    public String getSchemaName() {
        return schemaName;
    }
}
