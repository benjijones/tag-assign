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

import java.util.EnumSet;

/**
 * Used with multiple-key and iterator operations to specify whether to select
 * (return or operate on) the key-value pair for the parent key, and the
 * key-value pairs for only immediate children or all descendants.
 */
public enum Depth {

    /**
     * Select only immediate children, do not select the parent.
     */
    CHILDREN_ONLY,

    /**
     * Select immediate children and the parent.
     */
    PARENT_AND_CHILDREN,

    /**
     * Select all descendants, do not select the parent.
     */
    DESCENDANTS_ONLY,

    /**
     * Select all descendants and the parent.
     */
    PARENT_AND_DESCENDANTS;

    private final static Depth[] DEPTHS_BY_ORDINAL;
    static {
        final EnumSet<Depth> set = EnumSet.allOf(Depth.class);
        DEPTHS_BY_ORDINAL = new Depth[set.size()];
        for (Depth op : set) {
            DEPTHS_BY_ORDINAL[op.ordinal()] = op;
        }
    }

    /**
     * For internal use only.
     * @hidden
     */
    public static Depth getDepth(int ordinal) {
        if (ordinal < 0 || ordinal >= DEPTHS_BY_ORDINAL.length) {
            throw new RuntimeException("unknown Depth: " + ordinal);
        }
        return DEPTHS_BY_ORDINAL[ordinal];
    }
}
