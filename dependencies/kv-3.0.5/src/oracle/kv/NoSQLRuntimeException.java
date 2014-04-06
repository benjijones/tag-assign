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

package oracle.kv;

import oracle.kv.impl.util.NoSQLMessagesResourceBundle;
import oracle.kv.util.ErrorMessage;

import java.text.MessageFormat;

/**
 * Generic exception class for generating runtime exceptions whose messages are
 * derived from a locale specific message file.
 *
 * @since 2.0
 */
public class NoSQLRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static NoSQLMessagesResourceBundle resourceBundle = null;
    private static final Object lock = new Object();

    private String message;

    /**
     * For internal use only.
     * @hidden
     */
    public NoSQLRuntimeException(final ErrorMessage messageKey) {
        synchronized (lock) {
            if (resourceBundle == null) {
                resourceBundle = new NoSQLMessagesResourceBundle();
            }
        }

        message = (String)
            resourceBundle.handleGetObject(messageEnumToKey(messageKey));
    }

    /**
     * For internal use only.
     * @hidden
     */
    public NoSQLRuntimeException(final ErrorMessage messageKey,
                                 Object ... args) {
        synchronized (lock) {
            if (resourceBundle == null) {
                resourceBundle = new NoSQLMessagesResourceBundle();
            }
        }

        message = MessageFormat.format
            ((String) resourceBundle.handleGetObject
             (messageEnumToKey(messageKey)), args);
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Convert the supplied Enum into a String key that can be used for message
     * file lookup.
     *
     * @param messageKey The Enum to convert
     *
     * @return A token that can be used to look up the error message in the
     * messages file
     */
    private String messageEnumToKey(final ErrorMessage messageKey) {
        final String[] tokens = messageKey.name().split("_");
        return tokens[1];
    }
}
