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

package hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import oracle.kv.Key;
import oracle.kv.hadoop.KVInputFormat;

/**
 * A simple example demonstrating how to use the Oracle NoSQL DB Hadoop
 * oracle.kv.hadoop.KVInputFormat class to read data from NoSQL Database in a
 * Map/Reduce job and count the number of records for each major key in the
 * store.
 *
 * The map() function is passed the Key and Value for each record in the KV
 * Store and outputs k/v pairs containing the major path components as the
 * output key and a value of 1.  The reduce step sums the values for each of
 * the records with the same key.  This M/R task is similar to the ubiquitous
 * Hadoop Map/Reduce WordCount example.
 *
 * The KV Keys passed to the Map function are in the canonical format described
 * in the javadoc for the oracle.kv.Key.toString() method.
 *
 * The KVInputFormat and related classes are located in the lib/kvclient.jar
 * file so this must be included in the Hadoop classpath at runtime.
 *
 * The arguments to the program are the kvstore name, the helperHost:port pair,
 * the HDFS output path and optionally, the login file path.
 *
 * For example, if you build this class (and its subclasses) and put it into
 * myjar.jar, you can invoke with a command similar to this:

 * <pre>
 * export HADOOP_CLASSPATH=...:KVHOME/lib/kvstore.jar
 * bin/hadoop jar myjar.jar hadoop.CountMinorKeys \
 *            -libjars KVHOME/lib/kvstore.jar \
 *            mystore myhost:myport /myHDFSoutputdir [/myHDFSLoginfile]
 * </pre>
 */
public class CountMinorKeys extends Configured implements Tool {

    public static class Map
        extends Mapper<Text, Text, Text, IntWritable> {

        private Text word = new Text();
        private final static IntWritable one = new IntWritable(1);

        @Override
        public void map(Text keyArg, Text valueArg, Context context)
            throws IOException, InterruptedException {

            /*
             * keyArg is in the NoSQL Databse canonical Key format described in
             * the Key.toString() method's javadoc.
             *
             * The Output is the NoSQL Database record's Major Key as the
             * Map/Reduce key and 1 as the Map/Reduce value.
             */
            Key key = Key.fromString(keyArg.toString());
            /* Convert back to canonical format, but only use the major path. */
            word.set(Key.createKey(key.getMajorPath()).toString());
            context.write(word, one);
        }
    }

    public static class Reduce
        extends IntSumReducer<Text> {
    }

    @Override
    public int run(String[] args)
        throws Exception {

        @SuppressWarnings("deprecation")
        Job job = new Job(getConf());
        job.setJarByClass(CountMinorKeys.class);
        job.setJobName("Count Minor Keys");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(KVInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        KVInputFormat.setKVStoreName(args[0]);
        KVInputFormat.setKVHelperHosts(new String[] { args[1] });
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        /*
         * Load KVLoginFile if specified, otherwise try to load via reading
         * system property of oracle.kv.login.
         */
        if (args.length >= 4) {
            KVInputFormat.setKVSecurity(args[3]);
        }

        boolean success = job.waitForCompletion(true);
        return success ? 0 : 1;
    }

    public static void main(String[] args)
        throws Exception {

        int ret = ToolRunner.run(new CountMinorKeys(), args);
        System.exit(ret);
    }
}
