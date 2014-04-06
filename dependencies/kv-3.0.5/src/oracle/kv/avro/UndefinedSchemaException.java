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
 * Thrown when the application attempts to use a schema that has not been
 * defined using the NoSQL Database administration interface.
 * <p>
 * As described in detail under Avro Schemas in the {@link AvroCatalog} class
 * documentation, all schemas must be defined using the NoSQL Database
 * administration interface before they can be used to store values.
 * <p>
 * Depending on the nature of the application, when this exception is thrown
 * the client may wish to
 * <ul>
 * <li>retry the operation at a later time, if the schema is expected to be
 * available,</li>
 * <li>give up and report an error at a higher level so that a human being can
 * be made aware of the need to define the schema.</li>
 * </ul>
 * <p>
 * WARNING: Blocking and internal schema queries may occur frequently if
 * multiple threads repeatedly try to use a schema that is undefined in the
 * store.  To avoid this, it is important to delay before retrying an operation
 * using the undefined schema.
 *
 * @since 2.0
 */
public class UndefinedSchemaException extends FaultException {

    private static final long serialVersionUID = 1L;

    final private String schemaName;

    /**
     * For internal use only.
     * @hidden
     */
    public UndefinedSchemaException(String msg, String schemaName) {
        super(msg, null /*cause*/, false /*isRemote*/);
        this.schemaName = schemaName;
    }

    /**
     * Returns the full name of the undefined schema.
     */
    public String getSchemaName() {
        return schemaName;
    }
}
