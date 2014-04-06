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

import oracle.kv.ReturnValueVersion;

import oracle.kv.table.RecordDef;
import oracle.kv.table.ReturnRow;

/*
 * Note in ReturnRow.  The values of ReturnRow.Choice are identical to
 * those in ReturnValueVersion.Choice.  ReturnRow does not extend
 * ReturnValueVersion because it does not need, or want the underlying
 * ValueVersion object.
 */
public class ReturnRowImpl extends RowImpl implements ReturnRow {
    private static final long serialVersionUID = 1L;
    private final Choice returnChoice;

    ReturnRowImpl(RecordDef field, TableImpl table,
                  Choice returnChoice) {
        super(field, table);
        this.returnChoice = returnChoice;
    }

    private ReturnRowImpl(ReturnRowImpl other) {
        super(other);
        returnChoice = other.returnChoice;
    }

    @Override
    public Choice getReturnChoice() {
        return returnChoice;
    }

    @Override
    public ReturnRowImpl clone() {
        return new ReturnRowImpl(this);
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            if (other instanceof ReturnRowImpl) {
                ReturnRowImpl otherImpl = (ReturnRowImpl) other;
                return returnChoice == otherImpl.returnChoice;
            }
        }
        return false;
    }

    /**
     * Map ReturnRow.Choice to ReturnValueVersion.Choice.  Allow null to
     * mean NONE.
     */
    static ReturnValueVersion.Choice mapChoice(ReturnRow.Choice choice) {
        if (choice == null) {
            return ReturnValueVersion.Choice.NONE;
        }
        switch (choice) {
        case VALUE:
            return ReturnValueVersion.Choice.VALUE;
        case VERSION:
            return ReturnValueVersion.Choice.VERSION;
        case ALL:
            return ReturnValueVersion.Choice.ALL;
        case NONE:
            return ReturnValueVersion.Choice.NONE;
        default:
            throw new IllegalStateException("Unknown choice: " + choice);
        }
    }

    ReturnValueVersion makeReturnValueVersion() {
        return new ReturnValueVersion(mapChoice(returnChoice));
    }

    /**
     * Initialize this object from a ReturnValueVersion returned
     * from a get, put, or delete operation.
     *
     * If the previous row is from a later table version than this object's
     * table is aware of, deserialize that version using the correct table and
     * copy the fields to "this."  In other paths a new RowImpl object would be
     * created from the appropriate table but this object was created by the
     * caller.  The other alternative is to make the TableImpl in RowImpl
     * settable, and reset it. TBD.
     */
    void init(TableAPIImpl impl, ReturnValueVersion rvv, RowImpl key) {
        if (returnChoice == Choice.VALUE || returnChoice == Choice.ALL) {
            if (rvv.getValue() != null) {
                copyKeyFields(key);
                try {
                    table.rowFromValueVersion(rvv, this);
                } catch (TableVersionException tve) {
                    RowImpl newRow =
                        impl.getRowFromValueVersion(rvv, this, false);

                    /*
                     * Copy the fields appropriate to this row and set the
                     * correct table version.
                     */
                    copyFrom(newRow, true);
                    setTableVersion(newRow.getTableVersion());
                }
            }
        }

        /*
         * Version is either null or not so just set it.
         */
        setVersion(rvv.getVersion());
    }
}



