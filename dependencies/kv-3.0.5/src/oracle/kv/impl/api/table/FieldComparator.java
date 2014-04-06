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

import java.io.Serializable;
import java.util.Comparator;

/**
 * FieldComparator is a simple implementation of Comparator<String> that
 * is used for case-insensitive String comparisons.  This is used to
 * implement case-insensitive, but case-preserving names for fields, tables,
 * and indexes.
 *
 * IMPORTANT: technically this class should be declared @Persistent and
 * stored with JE instances of TreeMap that use it, but JE does not
 * currently store Comparator instances.  As a result the code that
 * uses previously-stored JE entities will not have the comparator set.
 * Fortunately that list is restricted to persistent plans and tasks
 * for creation and evolution of tables, and further, that code indirectly
 * uses FieldMap to encapsulate the relevant maps and that class *always*
 * deep-copies the source maps so the Comparator will always be set in
 * TableMetadata and related objects.
 */
class FieldComparator implements Comparator<String>, Serializable {
    static final FieldComparator instance = new FieldComparator();
    private static final long serialVersionUID = 1L;

    /**
     * Comparator<String>
     */
    @Override
    public int compare(String s1, String s2) {
        return s1.compareToIgnoreCase(s2);
    }
}

