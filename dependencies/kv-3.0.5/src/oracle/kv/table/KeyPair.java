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

package oracle.kv.table;

/**
 * A wrapper class for return values from
 * {@link TableAPI#tableKeysIterator(IndexKey, MultiRowOptions,
 *  oracle.kv.table.TableIteratorOptions)}
 * This classes allows the iterator to return all field value information that
 * can be obtained directly from the index without an additional fetch.
 *
 * Note: this class has a natural ordering that is inconsistent with
 * equals. Ordering is based on the indexKey only.
 *
 * @since 3.0
 */
public class KeyPair implements Comparable<KeyPair> {
    private final PrimaryKey primaryKey;
    private final IndexKey indexKey;

    /**
     * @hidden
     * For internal use only.
     */
    public KeyPair(PrimaryKey primaryKey, IndexKey indexKey) {
        this.primaryKey = primaryKey;
        this.indexKey = indexKey;
    }

    /**
     * Returns the PrimaryKey from the pair.
     *
     * @return the PrimaryKey
     */
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Returns the IndexKey from the pair.
     *
     * @return the IndexKey
     */
    public IndexKey getIndexKey() {
        return indexKey;
    }

    /**
     * Compares the indexKey of this object with the indexKey of the specified
     * object for order.
     */
    @Override
    public int compareTo(KeyPair other) {
        return indexKey.compareTo((other).getIndexKey());
    }
}

