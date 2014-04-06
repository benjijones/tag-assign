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
import java.util.Iterator;

import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.KeyValueVersion;
import oracle.kv.impl.api.KVStoreImpl;
import oracle.kv.impl.security.util.KVStoreLogin;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * @hidden
 */
public abstract class KVRecordReaderBase<K, V> extends RecordReader<K, V> {

    protected KVStore kvstore;
    protected Iterator<KeyValueVersion> iter;
    protected KeyValueVersion current;
    protected long cnt = 0;

    /**
     * Called once at initialization.
     * @param split the split that defines the range of records to read
     * @param context the information about the task
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
        throws IOException, InterruptedException {

        if (kvstore != null) {
            close();
        }

        KVInputSplit kvInputSplit = (KVInputSplit) split;
        String kvStoreName = kvInputSplit.getKVStoreName();
        String[] kvHelperHosts = kvInputSplit.getKVHelperHosts();
        String kvStoreSecurityFile = kvInputSplit.getKVStoreSecurityFile();

        final KVStoreConfig storeConfig =
            new KVStoreConfig(kvStoreName, kvHelperHosts);
        storeConfig.setSecurityProperties(
            KVStoreLogin.createSecurityProperties(kvStoreSecurityFile));
        kvstore = KVStoreFactory.getStore(storeConfig);
        KVStoreImpl kvstoreImpl = (KVStoreImpl) kvstore;
        int singlePartId = kvInputSplit.getKVPart();
        iter = kvstoreImpl.partitionIterator(kvInputSplit.getDirection(),
                                             kvInputSplit.getBatchSize(),
                                             singlePartId,
                                             kvInputSplit.getParentKey(),
                                             kvInputSplit.getSubRange(),
                                             kvInputSplit.getDepth(),
                                             kvInputSplit.getConsistency(),
                                             kvInputSplit.getTimeout(),
                                             kvInputSplit.getTimeoutUnit());
    }

    /**
     * Read the next key, value pair.
     * @return true if a key/value pair was read
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue()
        throws IOException, InterruptedException {

        try {
            boolean ret = iter.hasNext();
            if (ret) {
                current = iter.next();
            } else {
                current = null;
            }

            return ret;
        } catch (Exception E) {
            // Do we have to do anything better than just return false?
            System.out.println("KVRecordReaderBase " + this + " caught: " + E);
            E.printStackTrace();
            return false;
        }
    }

    /**
     * The current progress of the record reader through its data.
     *
     * @return a number between 0.0 and 1.0 that is the fraction of the data
     * read
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public float getProgress()
        throws IOException, InterruptedException {

        return 0;
    }

    /**
     * Close the record reader.
     */
    @Override
    public void close()
        throws IOException {

        kvstore.close();
    }
}
