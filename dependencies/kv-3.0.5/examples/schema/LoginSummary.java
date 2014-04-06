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

import oracle.kv.Key;
import oracle.kv.Value;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/**
 * Holds the session summary attributes that are stored as the {@code
 * Value} for the "/user/EMAIL/-/login" {@code Key}.  Illustrates the
 * use of a Key/Value pair for a parent (non-leaf) {@code Key}.
 *
 * <p>In general, be aware that there are performance trade-offs in
 * storing summary data, as shown in this example, versus computing it
 * using a query when it is needed.  For example, we could avoid
 * storing {@code totalLoginCount} and get the count of events
 * efficiently using a range query on the keys. But we would still
 * need to maintain the {@code totalLoginDuration} if we wanted to
 * avoid accessing each {@code LoginSession} to sum the individual
 * values on demand.  In some cases, summary data may be expensive
 * enough to recompute that it's worth storing. This is a trade-off
 * that application designers need to make based upon their knowledge
 * of application access patterns.</p>
 */
class LoginSummary {

    /*
     * The email address is a unique identifier and is used to construct
     * the Key's major path.
     */
    private final String email;

    /* Persistent fields stored in the Value. */
    private int totalLoginCount;
    private long totalLoginDuration;

    /**
     * Constructs a user object with its unique identifier, the email address.
     */
    LoginSummary(String email) {
        this.email = email;
    }

    /**
     * Returns the email identifier.
     */
    String getEmail() {
        return email;
    }

    /**
     * Changes the total login count.
     */
    void setTotalLoginCount(int totalLoginCount) {
        this.totalLoginCount = totalLoginCount;
    }

    /**
     * Returns the total login count.
     */
    int getTotalLoginCount() {
        return totalLoginCount;
    }

    /**
     * Changes the total login duration.
     */
    void setTotalLoginDuration(long totalLoginDuration) {
        this.totalLoginDuration = totalLoginDuration;
    }

    /**
     * Returns the total login duration.
     */
    long getTotalLoginDuration() {
        return totalLoginDuration;
    }

    /**
     * Returns a Key that can be used to write or read the LoginSummary.
     */
    Key getStoreKey() {
        return KeyDefinition.makeLoginSummaryKey(email);
    }

    /**
     * Serializes the summary attributes into the byte array of a Value.
     */
    Value getStoreValue(Bindings bindings) {
        final GenericRecord rec =
            new GenericData.Record(bindings.getLoginSummarySchema());
        rec.put("totalLoginCount", totalLoginCount);
        rec.put("totalLoginDuration", totalLoginDuration);
        return bindings.getLoginSummaryBinding().toValue(rec);
    }

    /**
     * Deserializes the summary attributes from the byte array of a Value.
     */
    void setStoreValue(Bindings bindings, Value value) {
        final GenericRecord rec =
            bindings.getLoginSummaryBinding().toObject(value);
        totalLoginCount = (Integer) rec.get("totalLoginCount");
        totalLoginDuration = (Long) rec.get("totalLoginDuration");
    }

    @Override
    public String toString() {
        return "<LoginSummary " + email +
               "\n    totalLoginCount: " + totalLoginCount +
               ", totalLoginDuration: " +
               KeyDefinition.formatDuration(totalLoginDuration) +
               ">";
    }
}
