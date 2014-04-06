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

import static oracle.kv.impl.util.ObjectUtil.checkNull;

import oracle.kv.impl.topo.ResourceId.ResourceType;

import com.sleepycat.persist.model.Persistent;

/**
 * The Datacenter topology component.
 * version 0: original
 * version 1: added repFactor field
 */
@Persistent(version=1)
public class Datacenter extends Topology.Component<DatacenterId> {

    private static final long serialVersionUID = 1L;

    /** Data centers with version=1 are of type PRIMARY by default. */
    private static final DatacenterType DEFAULT_DATACENTER_TYPE =
        DatacenterType.PRIMARY;

    private String name;
    private int repFactor;

    /** Creates a new Datacenter. */
    public static Datacenter newInstance(final String name,
                                         final int repFactor,
                                         final DatacenterType datacenterType) {

        checkNull("datacenterType", datacenterType);
        switch (datacenterType) {
        case PRIMARY:

            /*
             * Create an instance of the original Datacenter type, to maintain
             * compatibility as needed during an upgrade.
             */
            return new Datacenter(name, repFactor);

        case SECONDARY:
            return new DatacenterV2(name, repFactor, datacenterType);
        default:
            throw new AssertionError();
        }
    }

    private Datacenter(String name, int repFactor) {
        this.name = name;
        this.repFactor = repFactor;
        if (repFactor < 1) {
            throw new IllegalArgumentException(
                "Replication factor must be greater than or equal to 1");
        }
    }

    private Datacenter(Datacenter datacenter) {
        super(datacenter);
        name = datacenter.name;
        repFactor = datacenter.repFactor;
    }

    @SuppressWarnings("unused")
    private Datacenter() {
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#getResourceType()
     */
    @Override
    public ResourceType getResourceType() {
        return ResourceType.DATACENTER;
    }

    /* Returns the name associated with the Datacenter. */
    public String getName() {
        return name;
    }

    public int getRepFactor() {
        return repFactor;
    }

    /* repfactor is excluded from the hash code because it's mutable. */
    public void setRepFactor(int factor) {
        repFactor = factor;
    }

    /**
     * Returns the type of the data center.
     */
    public DatacenterType getDatacenterType() {
        return DEFAULT_DATACENTER_TYPE;
    }

    /* (non-Javadoc)
     * @see oracle.kv.impl.topo.Topology.Component#clone()
     */
    @Override
    public Datacenter clone() {
        return new Datacenter(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        final Datacenter other = (Datacenter) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return (repFactor == other.repFactor);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=" + getResourceId() + " name=" + name +
                  " repFactor=" + repFactor + " type=" + getDatacenterType());
        return sb.toString();
    }

    /**
     * Define a subclass of Datacenter for instances with a non-default value
     * for the DatacenterType.
     */
    @Persistent
    private static class DatacenterV2 extends Datacenter {
        private static final long serialVersionUID = 1L;
        private DatacenterType datacenterType;

        DatacenterV2(final String name,
                     final int repFactor,
                     final DatacenterType datacenterType) {
            super(name, repFactor);
            checkNull("datacenterType", datacenterType);
            this.datacenterType = datacenterType;
        }

        private DatacenterV2(final DatacenterV2 datacenter) {
            super(datacenter);
            datacenterType = datacenter.datacenterType;
        }

        /** For DPL */
        @SuppressWarnings("unused")
        private DatacenterV2() { }

        @Override
        public DatacenterType getDatacenterType() {
            return datacenterType;
        }

        /* (non-Javadoc)
         * @see oracle.kv.impl.topo.Topology.Component#clone()
         */
        @Override
        public DatacenterV2 clone() {
            return new DatacenterV2(this);
        }
    }
}
