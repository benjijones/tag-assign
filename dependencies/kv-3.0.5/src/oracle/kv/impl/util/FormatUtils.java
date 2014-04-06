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

package oracle.kv.impl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Centralize formatting, to make it as uniform as possible.
 * TODO: make this configurable, based on a Locale
 */
public class FormatUtils {

    public static String defaultTimeZone = "UTC";
    private static final TimeZone tz = TimeZone.getTimeZone(defaultTimeZone);

    public static TimeZone getTimeZone() {
        return tz;
    }

    /**
     * This is the format used by the NoSQL DB logger utilities
     */
    public static DateFormat getDateTimeAndTimeZoneFormatter() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        df.setTimeZone(tz);
        return df;
    }

    /**
     * This is used by the NoSQL DB stats
     */
    public static String formatTime(long time) {
        DateFormat formatter =  new SimpleDateFormat("HH:mm:ss.SSS z");
        formatter.setTimeZone(tz);
        return formatter.format(new Date(time));
    }

    /** 
     * Used to produce a UTC time stamp in the format of yy-MM-dd HH:mm:ss for
     * the .perf files.
     */
    public static String formatPerfTime(long time) {
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        df.setTimeZone(tz);
        return df.format(new Date(time));
    }

    /**
     * This is used by the admin and planner
     */
    public static String formatDateAndTime(long time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        formatter.setTimeZone(tz);
        return formatter.format(new Date(time));
    }
}
