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

package oracle.kv.impl.util.registry;

import java.rmi.RemoteException;

import oracle.kv.impl.util.SerialVersion;

/**
 * Base class for API classes that wrap remote interfaces to provide an API
 * called by clients of the remote service.
 *
 * @see VersionedRemote
 */
public abstract class RemoteAPI {

    private final short serialVersion;
    private final VersionedRemote remote;

    /**
     * Caches the effective version.  This constructor should be called only
     * by a private constructor in the API class, which is called only by the
     * API's wrap() method.
     */
    protected RemoteAPI(VersionedRemote remote)
        throws RemoteException {

        serialVersion =
            (short) Math.min(SerialVersion.CURRENT, remote.getSerialVersion());
        this.remote = remote;
    }

    /**
     * Returns the effective version, which is the minimum of the current
     * service and client version, and should be passed as the last argument of
     * each remote method.
     */
    public short getSerialVersion() {
        return serialVersion;
    }

    @Override
    public int hashCode() {
        return remote.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RemoteAPI)) {
            return false;
        }
        final RemoteAPI o = (RemoteAPI) other;
        return remote.equals(o.remote);
    }
}
