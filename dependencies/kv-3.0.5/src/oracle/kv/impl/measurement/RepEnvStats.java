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

import java.io.Serializable;

import oracle.kv.impl.util.FormatUtils;

import com.sleepycat.je.rep.ReplicatedEnvironmentStats;
import com.sleepycat.je.utilint.StatGroup;

/**
 * Complete dump of environment stats.
 */
public class RepEnvStats implements ConciseStats, Serializable {

    private static final long serialVersionUID = 1L;
    
    private final ReplicatedEnvironmentStats repEnvStats;
    private final long start;
    private final long end;

    public RepEnvStats(long start, long end, ReplicatedEnvironmentStats stats) {
        this.start = start;
        this.end = end;
        repEnvStats = stats;
    }

    @Override
    public long getStart() {
        return start;
    }

    @Override
    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Environment stats [" + FormatUtils.formatTime(end) +
            "]\n" + repEnvStats.toString();
    }

    public ReplicatedEnvironmentStats getStats() {
        return repEnvStats;
    }

    @Override
    public String getFormattedStats() {
        StringBuilder sb = new StringBuilder();
        for (StatGroup sg: repEnvStats.getStatGroups()) {
            if (sg != null) {
                sb.append(sg.toStringConcise());
            }
        }
        return sb.toString();
    }
}
