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

package oracle.kv.impl.api.lob;

import java.io.Serializable;

/**
 * Contains all the chunk-specific configuration parameters associated with the
 * behavior of Large Objects. They are hidden here in the imp package to
 * limit their use.
 */
public class ChunkConfig implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * The default number of contiguous LOB chunks that can be allocated in a
     * given partition.
     *
     * @since 2.0
     */
    // TODO: this parameter is better expressed as a number of bytes and
    // exposed in KVStoreConfig so that it's consistent with the
    // Consistency.Version  prohibition, which needs this awareness.
    static final int DEFAULT_CHUNKS_PER_PARTITION = 1000;

    /**
     * The default size of a chunk.
     *
     * @since 2.0
     */
    static final int DEFAULT_CHUNK_SIZE = 128 * 1024;

    private int chunksPerPartition;

    private int chunkSize;

    public ChunkConfig() {
        chunksPerPartition = DEFAULT_CHUNKS_PER_PARTITION;
        chunkSize = DEFAULT_CHUNK_SIZE;
    }

    @Override
    public ChunkConfig clone() {
        try {
            return (ChunkConfig) super.clone();
        } catch (CloneNotSupportedException neverHappens) {
            return null;
        }
    }

    public int getChunksPerPartition() {
        return chunksPerPartition;
    }

    public void setChunksPerPartition(int chunksPerPartition) {
        this.chunksPerPartition = chunksPerPartition;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return chunkSize;
    }
}