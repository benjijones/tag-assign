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

package oracle.kv.impl.api.ops;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.List;

import oracle.kv.impl.api.StoreIteratorParams;
import oracle.kv.impl.api.table.TargetTables;
import oracle.kv.impl.topo.PartitionId;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

/**
 * Iterate over table rows where the records may or may not reside on
 * the same partition.  Row values are returned which means that the
 * records are fetched from matching keys.
 */
public class TableIterate extends TableIterateOperation {

    public TableIterate(StoreIteratorParams sip,
                        TargetTables targetTables,
                        boolean majorComplete,
                        byte[] resumeKey) {
        super(OpCode.TABLE_ITERATE, sip, targetTables,
              majorComplete, resumeKey);
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor first
     * to read common elements.
     */
    TableIterate(ObjectInput in, short serialVersion)
        throws IOException {

        super(OpCode.TABLE_ITERATE, in, serialVersion);
    }

    @Override
    public Result execute(Transaction txn,
                          PartitionId partitionId,
                          final OperationHandler operationHandler) {
        verifyTableAccess();

        final List<ResultKeyValueVersion> results =
            new ArrayList<ResultKeyValueVersion>();

        final boolean moreElements = iterateTable
            (operationHandler,
             txn,
             partitionId,
             getMajorComplete(),
             getDirection(),
             getBatchSize(),
             getResumeKey(),
             CursorConfig.READ_COMMITTED,
             new OperationHandler.ScanVisitor() {

                 @Override
                 public int visit(Cursor cursor,
                                  DatabaseEntry keyEntry,
                                  DatabaseEntry dataEntry) {
                     /*
                      * 1.  check to see if key is part of table
                      * 2.  if so:
                      *    - fetch data
                      *    - add to results
                      */
                     int match = keyInTargetTable(operationHandler,
                                                  keyEntry,
                                                  dataEntry,
                                                  cursor);
                     if (match > 0) {

                         /*
                          * Because the iteration used READ_UNCOMMITTED and did
                          * not fetch the data it is necessary to fetch the data
                          * and lock the record now.
                          */
                         assert dataEntry.getPartial();
                         final DatabaseEntry dentry = new DatabaseEntry();
                         if (cursor.getCurrent
                             (keyEntry, dentry,
                              LockMode.DEFAULT) == OperationStatus.SUCCESS) {

                             if (!isTableData(dentry.getData(), null)) {
                                 return 0;
                             }
                             /*
                              * Add ancestor table results.  These appear
                              * before targets, even for reverse iteration.
                              */
                             match += addAncestorValues(cursor,
                                                        results,
                                                        keyEntry);
                             addValueResult(operationHandler, results,
                                            cursor, keyEntry, dentry);
                         }
                     }
                     return match;
                 }
             });
        /*
         * Table iteration filters results on the server side so some records
         * may be skipped.  This voids the moreElements logic in
         * OperationHandler.scan() so if moreElements is true but there are no
         * actual results in the current set, reset moreElements to false.
         */
        boolean more = (moreElements && results.size() == 0) ? false :
            moreElements;
        return new Result.IterateResult(getOpCode(), results, more);
    }
}
