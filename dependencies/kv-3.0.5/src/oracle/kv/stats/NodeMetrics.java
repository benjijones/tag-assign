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

package oracle.kv.stats;

import java.io.Serializable;

import oracle.kv.KVStore;

/**
 * The metrics associated with a node in the KVS.
 */
public interface NodeMetrics extends Serializable {

    /**
     * Returns the internal name associated with the node. It's unique across
     * the KVStore.
     */
    public String getNodeName();

    /**
     * Returns the zone that hosts the node.
     *
     * @deprecated replaced by {@link #getZoneName}
     */
    @Deprecated
    public String getDataCenterName();

    /**
     * Returns the zone that hosts the node.
     */
    public String getZoneName();

    /**
     * Returns true is the node is currently active, that is, it's reachable
     * and can service requests.
     */
    public boolean isActive();

    /**
     * Returns true if the node is currently a master.
     */
    public boolean isMaster();

    /**
     * Returns the number of requests that were concurrently active for this
     * node at this KVS client.
     */
    public int getMaxActiveRequestCount();

    /**
     * Returns the total number of requests processed by the node.
     */
    public long getRequestCount();

    /**
     * Returns the number of requests that were tried at this node but did not
     * result in a successful response.
     */
    public long getFailedRequestCount();

    /**
     * Returns the trailing average latency (in ms) over all requests made to
     * this node.
     * <p>
     * Note that since this is a trailing average it's not cleared when the
     * statistics are cleared via the {@link KVStore#getStats(boolean)} method.
     */
    public int getAvLatencyMs();
}
