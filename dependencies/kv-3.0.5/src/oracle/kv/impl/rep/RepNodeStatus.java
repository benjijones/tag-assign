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

package oracle.kv.impl.rep;

import java.io.Serializable;

import oracle.kv.impl.util.ConfigurableService.ServiceStatus;

import com.sleepycat.je.rep.ReplicatedEnvironment.State;
import com.sleepycat.je.rep.utilint.HostPortPair;
import oracle.kv.impl.rep.migration.PartitionMigrationStatus;

/**
 * RepNodeStatus represents the current status of a running RepNodeService.  It
 * includes ServiceStatus as well as additional state specific to a RepNode.
 */
public class RepNodeStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    private final ServiceStatus status;
    private final State state;
    private final long vlsn;

    /* Since R2 */
    private final String haHostPort;

    /* Since R2 */
    private final PartitionMigrationStatus[] migrationStatus;

    /*
     * The haPort field is present for backward compatibility. If deserialized
     * at an R1 node we still want it to function. The added field, haHostPort,
     * was added for elasticity and is not needed for general operation.
     *
     */
    private final int haPort;

    public RepNodeStatus(ServiceStatus status, State state, long vlsn, 
                         String haHostPort,
                         PartitionMigrationStatus[] migrationStatus) {
        this.status = status;
        this.state = state;
        this.vlsn = vlsn;
        this.haHostPort = haHostPort;
        this.migrationStatus = migrationStatus;
        haPort = HostPortPair.getPort(haHostPort);
    }

    public ServiceStatus getServiceStatus() {
        return status;
    }

    public State getReplicationState() {
        return state;
    }

    public long getVlsn() {
        return vlsn;
    }
    
    public int getHAPort() {
        return haPort;
    }

    /**
     * Returns the HA host and port string. The returned value may be null
     * if this instance represents a pre-R2 RepNodeService.
     *
     * @return the HA host and port string or null
     */
    public String getHAHostPort() {
        return haHostPort;
    }

    public PartitionMigrationStatus[] getPartitionMigrationStatus() {
        /* For compatibility with R1, return an empty array */
        return (migrationStatus == null) ? new PartitionMigrationStatus[0] :
                                           migrationStatus;
    }
    
    @Override
    public String toString() {
        return status + "," + state;
    }
}
