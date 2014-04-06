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

package oracle.kv.impl.topo;

import oracle.kv.impl.topo.ResourceId.ResourceType;

import com.sleepycat.persist.model.Persistent;

/**
 * The RN topology component.
 */
@Persistent
public class RepNode extends Topology.Component<RepNodeId>
    implements Comparable<RepNode> {

    private static final long serialVersionUID = 1L;

    private StorageNodeId storageNodeId;

    public RepNode(StorageNodeId storageNodeId) {

        this.storageNodeId = storageNodeId;
    }

    private RepNode(RepNode repNode) {
        super(repNode);

        storageNodeId = repNode.storageNodeId;
    }

    @SuppressWarnings("unused")
    private RepNode() {

    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#getResourceType()
     */
    @Override
    public ResourceType getResourceType() {
        return ResourceType.REP_NODE;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result +
            ((storageNodeId == null) ? 0 : storageNodeId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RepNode other = (RepNode) obj;
        return propertiesEquals(other);
    }

    public boolean propertiesEquals(RepNode other) {

        if (storageNodeId == null) {
            if (other.storageNodeId != null) {
                return false;
            }
        } else if (!storageNodeId.equals(other.storageNodeId)) {
            return false;
        }
        return true;
    }


    /**
     * Returns the replication group id associated with the RN.
     */
    public RepGroupId getRepGroupId() {
        return new RepGroupId(getResourceId().getGroupId());
    }

    /**
     * Returns the StorageNodeId of the SN hosting this RN
     */
    @Override
    public StorageNodeId getStorageNodeId() {
        return storageNodeId;
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#clone()
     */
    @Override
    public RepNode clone() {
        return new RepNode(this);
    }

    @Override
    public boolean isMonitorEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "[" + getResourceId() + "]" + " sn=" + storageNodeId;
    }

    @Override
    public int compareTo(RepNode other) {
        return getResourceId().compareTo
            (other.getResourceId());
    }
}
