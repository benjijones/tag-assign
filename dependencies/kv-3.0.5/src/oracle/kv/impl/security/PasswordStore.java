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
package oracle.kv.impl.security;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface to file-system resident password storage structures.
 */
public interface PasswordStore {

    /**
     * Class used to identify a "Login".  It's a user/database pair.
     */
    public class LoginId {
        private final String database;
        private final String user;

        public LoginId(String database, String user) {
            this.database = database;
            this.user = user;
        }

        public String getDatabase() {
            return database;
        }

        public String getUser() {
            return user;
        }

        @Override
        public int hashCode() {
            return user.hashCode() + database.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != LoginId.class) {
                return false;
            }
            final LoginId oLid = (LoginId) o;
            return user.equals(oLid.user) && database.equals(oLid.database);
        }
    }

    /**
     * Check whether the password store exists.  This is normally useful
     * after getting a PasswordStore, but before attempting to open or
     * create it.
     */
    boolean exists() throws IOException;

    /**
     * Check whether the password store requires a passphrase for access.
     */
    boolean requiresPassphrase() throws IOException;

    /**
     * Check whether the specified passphrase is acceptable to the
     * implementatation.
     */
    boolean isValidPassphrase(char[] passphrase) throws IOException;

    /**
     * Open the password store.
     * @throw IllegalStateException if open() or create() have previously
     * been called on this handle.
     */
    boolean open(char[] passphrase) throws IOException;

    /**
     * Create the password store.
     * @throw IllegalStateException if open() or create() have previously
     * been called on this handle.
     */
    boolean create(char[] passphrase) throws IOException;

    /**
     * Return the list of aliases for secrets held within the password store.
     */
    Collection<String> getSecretAliases() throws IOException;

    /**
     * Set the secret associated with an alias within the password store.
     * @return true if this overrides a previous setting for the alias
     */
    boolean setSecret(String alias, char[] secret) throws IOException;

    /**
     * Get the secret associated with an alias within the password store.
     */
    char[] getSecret(String alias) throws IOException;

    /**
     * Delete the secret associated with an alias within the password store.
     * @return true if the secret was present.
     */
    boolean deleteSecret(String alias) throws IOException;

    /**
     * Return the list of databases for which logins have been set within the
     * password store.
     */
    Collection<LoginId> getLogins() throws IOException;

    /**
     * Set a login within the password store.  There can be only one login
     * per database, so if the password store contains an existing login
     * of { db = q, user = r, pwd = s } and a call to setLogin is made
     * with {db = q, user = t, pwd = u }, this replaces the previous login
     * associated with the database.
     *
     * @return true if the login replaces a previous login
     */
    boolean setLogin(LoginId loginId, char[] password)
        throws IOException;

    /**
     * Look for the LoginId associated with the specified database.
     */
    LoginId getLoginId(String database) throws IOException;

    /**
     * Get the password associated with the specified database.
     * @return null if no entry was found
     */
    char[] getLoginSecret(String database) throws IOException;

    /**
     * Remove the login for the database in the password store.
     */
    boolean deleteLogin(String database) throws IOException;

    /**
     * Set or change the passphrase associated with the password store.
     * @throw UnsupportedOperationException if the password store does not
     *    support the requested change
     */
    boolean setPassphrase(char[] passphrase) throws IOException;

    /**
     * Save changes to the persistent password store.
     */
    void save() throws IOException;

    /**
     * Make a best effort attempt to clear potentially sensitive state
     * information associated with the password store handle.
     */
    void discard();
}
