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

import java.util.ArrayList;
import java.util.List;

import oracle.kv.Key;

/**
 * Utility methods associated with the manipulation of chunk keys
 */
public class ChunkKeyFactory {

    /**
     * Produce a key that's compatible with this metadata version.
     */
    private final int metadataVersion;

    /**
     * The radix used to encode the chunk key
     */
    static final int KEY_RADIX = 32;

    ChunkKeyFactory(int metadataVersion) {
        this.metadataVersion = metadataVersion;
    }

    /**
     * Creates a chunk key suitable for "getting" the chunk.
     */
    Key create(Key internalLobKey,
                      long superChunkId,
                      long chunkId) {

        if (! (superChunkId > 0 && chunkId > 0)) {
            throw new IllegalArgumentException("super chunk id:" +
                                                superChunkId +
                                                "chunk id:" + chunkId);
        }
        final List<String> majorPath =
            new ArrayList<String>(internalLobKey.getMajorPath());
        majorPath.add(getIdString(superChunkId));

        final List<String> minorPath =
                new ArrayList<String>(internalLobKey.getMinorPath());
        minorPath.add(getIdString(chunkId));

        return Key.createKey(majorPath, minorPath);
    }

    /**
     * Create a super chunk key that can be used as the basis for getting the
     * chunk keys associated with it.
     */
    Key createSuperChunkKey(Key internalLobKey,
                                   long superChunkId) {

        if (superChunkId <= 0) {
            throw new IllegalArgumentException("Invalid super chunk id:" +
                                                superChunkId);
        }

        final List<String> majorPath =
            new ArrayList<String>(internalLobKey.getMajorPath());
        majorPath.add(getIdString(superChunkId));
        return Key.createKey(majorPath);
    }

    /**
     * Parses a chunk key to obtain the chunk id.
     */
    int getChunkId(Key chunkKey) {
       return Integer.parseInt(chunkKey.getMinorPath().get(0), KEY_RADIX);
    }

    /**
     * Used to format the super chunk and chunk ids into string key components.
     */
    private String getIdString(long i) {
        if (metadataVersion == 1) {
            /* For compatibility with version 1 */
            final String s = Long.toString(i, KEY_RADIX);
            /*
             * Note bug in version 1, substring expression should have been:
             *  "0000000".substring(0, 7 - s.length())
             */
            return "0000000".substring(7 - s.length()) + s;
        }
        return Long.toString(i, KEY_RADIX);
    }

    int getMetadataVersion() {
        return metadataVersion;
    }
}
