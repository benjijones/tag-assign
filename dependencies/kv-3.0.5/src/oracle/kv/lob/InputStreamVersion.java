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

package oracle.kv.lob;

import java.io.InputStream;

import oracle.kv.Version;

/**
 * Holds a Stream and Version that are associated with a LOB.
 *
 * <p>
 * An InputStreamVersion instance is returned by {@link KVLargeObject#getLOB}
 * as the current value (represented by the stream) and version associated with
 * a given LOB. The version and inputStream properties will always be non-null.
 * </p>
 * IOExceptions thrown by this stream may wrap KVStore exceptions as described
 * in the documentation for the {@link KVLargeObject#getLOB} method.
 *
 * @since 2.0
 */
public class InputStreamVersion {

    private final InputStream inputStream;
    private final Version version;

    /**
     * Used internally to create an object with an inputStream and version.
     */
    public InputStreamVersion(InputStream inputStream, Version version) {
        this.inputStream = inputStream;
        this.version = version;
    }

    /**
     * Returns the InputStream part of the InputStream and Version pair.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Returns the Version of the InputStream and Version pair.
     */
    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "<InputStreamVersion " + inputStream + ' ' + version + '>';
    }
}