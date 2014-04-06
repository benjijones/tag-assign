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

package oracle.kv.hadoop;

import java.io.IOException;

import oracle.kv.Key;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * A Hadoop InputFormat class for reading data from an Oracle NoSQL Database.
 * Map/reduce keys and values are returned as Text objects.
 *
 * NoSQL Database Key arguments are passed in the canonical format returned by
 * {@link Key#toString Key.toString} format.
 *
 * <p>
 * Refer to the javadoc for {@link KVInputFormatBase} for information on the
 * parameters that may be passed to this class.
 * <p>
 * A simple example demonstrating the Oracle NoSQL DB Hadoop
 * oracle.kv.hadoop.KVInputFormat class can be found in the
 * KVHOME/example/hadoop directory. It demonstrates how to read records from
 * Oracle NoSQL Database in a Map/Reduce job.  The javadoc for that program
 * describes the simple Map/Reduce processing as well as how to invoke the
 * program in Hadoop.
 */
public class KVInputFormat extends KVInputFormatBase<Text, Text> {

    /**
     * @hidden
     */
    @Override
    public RecordReader<Text, Text>
        createRecordReader(InputSplit split, TaskAttemptContext context)
        throws IOException, InterruptedException {

        KVRecordReader ret = new KVRecordReader();
        ret.initialize(split, context);
        return ret;
    }
}
