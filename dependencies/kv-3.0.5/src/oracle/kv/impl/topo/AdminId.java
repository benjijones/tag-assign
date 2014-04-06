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

import com.sleepycat.persist.model.Persistent;

/**
 * A unique identifier used by each of the instances of the Admin Service.
 */
@Persistent
public class AdminId extends ResourceId implements Comparable<AdminId> {

    private static final long serialVersionUID = 1;
    private static final String ADMIN_PREFIX = "admin";

    /**
     * The unique ID of this admin instance.
     */
    private int nodeId;

    /**
     * Creates an ID of an admin instance.
     *
     * @param nodeId the internal node id
     */
    public AdminId(int nodeId) {
        super();
        this.nodeId = nodeId;
    }

    /*
     * No-arg ctor for use by DPL.
     */
    public AdminId() {
    }

    /**
     * FastExternalizable constructor.  Must call superclass constructor first
     * to read common elements.
     */
    public AdminId(ObjectInput in, short serialVersion)
        throws IOException {

        super(in, serialVersion);
        nodeId = in.readInt();
    }

    /**
     * FastExternalizable writer.  Must call superclass method first to write
     * common elements.
     */
    @Override
    public void writeFastExternal(ObjectOutput out, short serialVersion)
        throws IOException {

        super.writeFastExternal(out, serialVersion);
        out.writeInt(nodeId);
    }

    /**
     * Gets the type of this resource
     *
     * @return the resource type
     */
    @Override
    public ResourceType getType() {
        return ResourceId.ResourceType.ADMIN;
    }

    /**
     * Gets the internal identifier of this admin instance
     *
     * @return the instance ID
     */
    public int getAdminInstanceId() {
        return nodeId;
    }

    @Override
    public String getFullName() {
        return ADMIN_PREFIX + getAdminInstanceId();
    }

    /**
     * Parse a string that is either an integer or is in adminX format and
     * generate an AdminId
     */
    public static AdminId parse(String s) {
        return new AdminId(parseForInt(ADMIN_PREFIX, s));
    }

    /**
     * Return the admin prefix string.
     */
    public static String getPrefix() {
        return ADMIN_PREFIX;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public Topology.Component<?> getComponent(Topology topology) {
    	/*
    	 * AdminId does not correspond to a real Topology component, so
    	 * we can't satisfy this request.
    	 */
        throw new UnsupportedOperationException
            ("Method not implemented: getComponent");
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
        AdminId other = (AdminId) obj;
        if (nodeId != other.nodeId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nodeId;
        return result;
    }

    @Override
    public int compareTo(AdminId other) {
        int x = this.getAdminInstanceId();
        int y = other.getAdminInstanceId();

        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
