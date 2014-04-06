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
import oracle.kv.impl.topo.Topology.Component;

import com.sleepycat.persist.model.Persistent;

/**
 * An Entry in the PartitionMap
 */
@Persistent
public class Partition extends Topology.Component<PartitionId> {

    private static final long serialVersionUID = 1L;
    private RepGroupId repGroupId;

    public Partition(RepGroup repGroup) {
        this(repGroup.getResourceId());
    }

    public Partition(RepGroupId repGroupId) {
        this.repGroupId = repGroupId;
    }

    @SuppressWarnings("unused")
    private Partition() {
    }

    private Partition(Partition partition) {
        super(partition);
        repGroupId = partition.repGroupId;
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#getResourceType()
     */
    @Override
    public ResourceType getResourceType() {
        return ResourceType.PARTITION;
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#clone()
     */
    @Override
    public Component<?> clone() {
        return new Partition(this);
    }

    /**
     * Returns the RepGroupId associated with the partition
     */
    public RepGroupId getRepGroupId() {
        return repGroupId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +
            ((repGroupId == null) ? 0 : repGroupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        if (!super.equals(obj)) {
            return false;
        }

        Partition other = (Partition) obj;
        return propertiesEqual(other);
    }

    /**
     * @return true if the logical portion of the Partitions are equal.
     */
    public boolean propertiesEqual(Partition other) {

        if (repGroupId == null) {
            if (other.repGroupId != null) {
                return false;
            }
        } else if (!repGroupId.equals(other.repGroupId)) {
            return false;
        } 

        return true;
    }

    @Override
    public String toString() {
        return "[" + getResourceId() + "] " +  " shard=" +  repGroupId;
    }
}
