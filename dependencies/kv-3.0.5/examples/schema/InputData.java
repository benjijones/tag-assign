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

package schema;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Boring static input data.
 */
class InputData {

    static final int N_USERS = 5;
    static final String[] USER_EMAIL = new String[N_USERS];
    static final UserInfo[] USER_INFO = new UserInfo[N_USERS];
    static final UserImage[] USER_IMAGE = new UserImage[N_USERS];
    static final long[][] LOGIN_TIMES = new long[N_USERS][];
    static final int[][] SESSION_DURATIONS = new int[N_USERS][];
    static final int MAX_SESSION_DURATION = 60 * 1000 * 60;
    static final int SESSION_DURATION_INCR = 5 * 1000 * 60;
    static final long CUTOFF_LOGIN_DATE;

    static {
        final long currentTime = System.currentTimeMillis();
        for (int i = 0; i < N_USERS; i += 1) {

            /* Assign a unique email address per user. */
            final String email = "user" + i + "@example.com";
            USER_EMAIL[i] = email;

            /* Create a UserInfo per user for the initial data load. */
            final UserInfo userInfo = new UserInfo(email);
            final Gender gender = (i % 2 == 0) ? Gender.FEMALE : Gender.MALE;
            userInfo.setGender(gender);
            userInfo.setName(((gender == Gender.FEMALE) ? "Ms." : "Mr.") +
                             " Number-" + i);
            userInfo.setAddress("#" + i + " Example St, Example Town, AZ");
            userInfo.setPhone("000.000.0000".replace('0', (char) ('0' + i)));
            USER_INFO[i] = userInfo;

            /* Create a UserImage per user for the initial data load. */
            final UserImage userImage = new UserImage(email);
            userImage.setImage(new byte[1000 + i]);
            USER_IMAGE[i] = userImage;

            /*
             * Starting a few days before the current time, create a variable
             * number of "sessions" (login time and duration) per user.  The
             * date ranges are overlapping.  Each user logs in at the same time
             * of day, but at a different time than the other users.  The
             * session durations decrease each day.  The idea is just to
             * provide some variability in the input data.
             */
            final Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(currentTime);
            cal.add(Calendar.MILLISECOND, MAX_SESSION_DURATION * i);
            cal.add(Calendar.DATE, -(N_USERS + i));

            final int nLogins = (2 * i) + 1;
            LOGIN_TIMES[i] = new long[nLogins];
            SESSION_DURATIONS[i] = new int[nLogins];

            int duration = MAX_SESSION_DURATION;
            for (int j = 0; j < nLogins; j += 1) {
                LOGIN_TIMES[i][j] = cal.getTime().getTime();
                SESSION_DURATIONS[i][j] = duration;
                cal.add(Calendar.DATE, 1);
                duration -= SESSION_DURATION_INCR;
            }
        }

        /*
         * Pick a date roughly in the middle of the date ranges that can be
         * used for querying all sessions before or after this cutoff date.
         */
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(currentTime);
        cal.add(Calendar.DATE, -(N_USERS - 1));
        CUTOFF_LOGIN_DATE = cal.getTime().getTime();
    }
}
