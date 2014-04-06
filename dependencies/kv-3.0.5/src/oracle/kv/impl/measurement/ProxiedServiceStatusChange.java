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

package oracle.kv.impl.measurement;

import oracle.kv.impl.topo.ResourceId;
import oracle.kv.impl.util.ConfigurableService.ServiceStatus;
import oracle.kv.impl.util.FormatUtils;

/**
 * Used by a third party service (usually the SNA) to report a status change
 * experienced by another service (usually a RepNode).  This is useful when the
 * RepNode is shutting down or handling errors and may not be up to provide its
 * own service status.
 */
public class ProxiedServiceStatusChange extends ServiceStatusChange {

    private static final long serialVersionUID = 1L;

    /* The resource that experienced the change. */
    private final ResourceId target;

    /**
     * @param target the resource that experienced the change.
     * @param newStatus the new service status.
     */
    public ProxiedServiceStatusChange(ResourceId target,
                                      ServiceStatus newStatus) {
        super(newStatus);
        this.target = target;
    }

    @Override
    public ResourceId getTarget(ResourceId reportingResource) {
        return target;
    }

    @Override
    public String toString() {
        return target + ": Service status: " + newStatus + " " +
            FormatUtils.formatDateAndTime(now);
    }
}
