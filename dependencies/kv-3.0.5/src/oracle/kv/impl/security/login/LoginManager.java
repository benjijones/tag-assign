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
package oracle.kv.impl.security.login;

import oracle.kv.impl.security.SessionAccessException;
import oracle.kv.impl.topo.ResourceId;
import oracle.kv.impl.topo.ResourceId.ResourceType;
import oracle.kv.impl.util.HostPort;

/**
 * LoginManager defines the interface by which RMI interface APIs acquire
 * LoginTokens for called methods.
 */
public interface LoginManager {

    /**
     * Get the username associated with the LoginManager.
     * @return the associated user name, or null if there is no associated
     * username, as with internal logins.
     */
    String getUsername();

    /**
     * Get a local login appropriate for the specified target.
     *
     * @param target the target host/port being accessed
     * @param rtype the type of resource being accessed
     * @return a LoginHandle appropriate for accessing the specified
     * resource type
     * @throws UnsupportedOperationException if the implementation does not
     * support the specified resource type
     */
    LoginHandle getHandle(HostPort target, ResourceType rtype);

    /**
     * Get a login appropriate for the specified target resource.
     * Some implementations might not support this method.
     *
     * @throws UnsupportedOperationException if the implementation has no
     *    support for this method
     * @throws IllegalStateException if the implementation has support for
     *    this method, but does not have enough state to resolve resource ids
     */
    LoginHandle getHandle(ResourceId target);

    /*
     * Log out the user against all known targets
     */
    void logout()
        throws SessionAccessException;

}
