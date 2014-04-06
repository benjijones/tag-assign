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

import java.io.File;

/**
 * The base class of all NoSQL password managers.  Password managers provide
 * the ability to store and retrieve passwords to and from a file or directory.
 */
public abstract class PasswordManager {

    /**
     * The name of the standard open-source implementation of password manager
     * included in NoSQL.
     */
    public static final String FILE_STORE_MANAGER_CLASS =
        "oracle.kv.impl.security.filestore.FileStoreManager";

    /**
     * The name of the Oracle proprietary implementation of password manager
     * based on the Oracle Wallet code.  This is available only in the NoSQL
     * DB EE version.
     */
    public static final String WALLET_MANAGER_CLASS =
        "oracle.kv.impl.security.wallet.WalletManager";

    /**
     * List of known implementation classes in order of preference.
     */
    private static final String[] preferredImplementations =
        new String[] { WALLET_MANAGER_CLASS, FILE_STORE_MANAGER_CLASS };

    /**
     * Attempt to load an instance of the specified class as a PasswordManager.
     *
     * @param className the name of the class that extends PasswordManager.
     * @return an instance of PasswordManager
     * @throws ClassNotFoundException if the named class cannot be found
     * @throws IllegalAccessException if the class or default constructor
     *         are inaccessible
     * @throws InstantiationException if the class has no default constructor
     *         or is not an instantiable class.
     * @throws ExceptionInInitializerError if the constructor for the class
     *         throws an exception
     */
    public static PasswordManager load(String className)
        throws ClassNotFoundException, IllegalAccessException,
               InstantiationException {

        final Class<?> pwdMgrClass = Class.forName(className);
        return (PasswordManager) pwdMgrClass.newInstance();
    }

    /**
     * Report the preferred PasswordManager class name for this install.
     * @return the preferred password manager implementation for this
     *   installation.
     */
    public static String preferredManagerClass() {

        for (String s : preferredImplementations) {
            try {
                Class.forName(s);
                return s;
            } catch (ClassNotFoundException cnfe) /* CHECKSTYLE:OFF */ {
                /* Ignore */
            } /* CHECKSTYLE:ON */
        }
        return null;
    }

    /**
     * Get a handle to a password store. This store might not exist yet, which
     * is fine.  The returned handle allows the caller to query for existence
     * and access requiremnets for the store.
     *
     * @param storeLocation an abstract file identifying the location of the
     *   store.  This abstract file may be an actual file or a directory,
     *   as is appropriate for the PasswordManager implementation.
     * @return a PasswordStore handle based on the store location.
     */
    public abstract PasswordStore getStoreHandle(File storeLocation);

    /**
     * Indicate whether the password store implementation requires a directory
     * (to allow for multiple files) or just a file for its storage.
     *
     * @return true if the storeLocation passed to getStoreHandle should be a
     *   directory and false if the storeLocation should be a file.
     */
    public abstract boolean storeLocationIsDirectory();

}
