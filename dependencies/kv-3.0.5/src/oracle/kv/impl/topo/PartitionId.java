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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.NumberFormatException;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class PartitionId extends ResourceId {

    public static PartitionId NULL_ID = new PartitionId(-1);

    public PartitionId(int partitionId) {
        super();
        this.partitionId = partitionId;
    }

    private static final long serialVersionUID = 1L;

    private int partitionId;

    @SuppressWarnings("unused")
    private PartitionId() {

    }

    public boolean isNull() {
        return partitionId == NULL_ID.partitionId;
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor first
     * to read common elements.
     */
    public PartitionId(ObjectInput in, short serialVersion)
        throws IOException {

        super(in, serialVersion);
        partitionId = in.readInt();
    }

    /**
     * FastExternalizable writer.  Must call superclass method first to write
     * common elements.
     */
    @Override
    public void writeFastExternal(ObjectOutput out, short serialVersion)
        throws IOException {

        super.writeFastExternal(out, serialVersion);
        out.writeInt(partitionId);
    }

    @Override
    public ResourceType getType() {
        return ResourceType.PARTITION;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public String getPartitionName() {
        return "p" + partitionId;
    }

    public static boolean isPartitionName(String name) {
        if (name.startsWith("p")) {
            try {
                Integer.parseInt(name.substring(1));
                return true;
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getType() + "-" + partitionId;
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.admin.ResourceId#getComponent(oracle.kv.impl.topo.Topology)
     */
    @Override
    public Partition getComponent(Topology topology) {
       return topology.get(this);
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
        PartitionId other = (PartitionId) obj;
        if (partitionId != other.partitionId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + partitionId;
        return result;
    }
}
