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

package oracle.kv;

import oracle.kv.avro.AvroBinding;
import oracle.kv.avro.AvroCatalog;

/**
 * Generic interface for translating between {@link Value}s (stored byte
 * arrays) and typed objects representing that value.  In other words, this
 * interface is used for serialization and deserialization of {@link Value}s.
 * <p>
 * A built-in {@link AvroBinding}, which is a {@code ValueBinding} subtype, may
 * be obtained from the {@link AvroCatalog}.  Or, the {@code ValueBinding}
 * interface may be implemented directly by the application to create custom
 * bindings, when the Avro data format is not used.
 * <p>
 * <em>WARNING:</em> We strongly recommend using an {@link AvroBinding}.  NoSQL
 * DB will leverage Avro in the future to provide additional features and
 * capabilities.
 *
 * @param <T> is the type of the deserialized object that is passed to {@link
 * #toValue toValue} and returned by {@link #toObject toObject}.  The specific
 * type depends on the particular binding that is used.
 *
 * @see AvroBinding
 * @see AvroCatalog
 *
 * @since 2.0
 */
public interface ValueBinding<T> {

    /**
     * After doing a read operation using a {@link KVStore} method, the user
     * calls {@code toObject} with the {@link Value} obtained from the read
     * operation.
     *
     * @param value the {@link Value} obtained from a {@link KVStore} read
     * operation method.
     *
     * @return the deserialized object.
     *
     * @throws RuntimeException if a parameter value is disallowed by the
     * binding; see {@link AvroBinding} for specific exceptions thrown when
     * using the Avro format.
     */
    public T toObject(Value value)
        throws RuntimeException;

    /**
     * Before doing a write operation, the user calls {@code toValue} passing
     * an object she wishes to store.  The resulting {@link Value} is then
     * passed to the write operation method in {@link KVStore}.
     *
     * @param object the object the user wishes to store, or at least
     * serialize.
     *
     * @return the serialized object.
     *
     * @throws RuntimeException if a parameter value is disallowed by the
     * binding; see {@link AvroBinding} for specific exceptions thrown when
     * using the Avro format.
     */
    public Value toValue(T object)
        throws RuntimeException;
}
