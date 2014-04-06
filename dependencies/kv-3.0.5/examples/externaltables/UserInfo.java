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

package externaltables;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import oracle.kv.Key;
import oracle.kv.Value;

/**
 * A simple class which represents a User in the External Tables Cookbook
 * example.
 */
class UserInfo {

    /*
     * The email address is a unique identifier and is used to construct
     * the Key's major path.
     */
    private final String email;

    /* Persistent fields stored in the Value. */
    private String name;
    private char gender;
    private String address;
    private String phone;

    private static final String INFO_PROPERTY_NAME = "info";

    /**
     * Constructs a user object with its unique identifier, the email address.
     */
    UserInfo(final String email) {
        this.email = email;
    }

    /**
     * Returns the email identifier.
     */
    String getEmail() {
        return email;
    }

    /**
     * Changes the name attribute.
     */
    void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the name attribute.
     */
    String getName() {
        return name;
    }

    /**
     * Changes the gender attribute.
     */
    void setGender(final char gender) {
        this.gender = gender;
    }

    /**
     * Returns the gender attribute.
     */
    char getGender() {
        return gender;
    }

    /**
     * Changes the address attribute.
     */
    void setAddress(final String address) {
        this.address = address;
    }

    /**
     * Returns the address attribute.
     */
    String getAddress() {
        return address;
    }

    /**
     * Changes the phone attribute.
     */
    void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * Returns the phone attribute.
     */
    String getPhone() {
        return phone;
    }

    /**
     * Returns a Key that can be used to write or read the UserInfo.
     */
    Key getStoreKey() {
        return Key.createKey(Arrays.asList(LoadCookbookData.USER_OBJECT_TYPE,
                                           email),
                             INFO_PROPERTY_NAME);
    }

    /**
     * Serializes user info attributes into the byte array of a Value.
     */
    Value getStoreValue() {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(baos);

        try {
            writeString(dos, name);
            dos.writeChar(gender);
            writeString(dos, address);
            writeString(dos, phone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Value.createValue(baos.toByteArray());
    }

    /**
     * Utility that writes a UTF string and accomodates null values.
     */
    private void writeString(final DataOutput out, final String val)
        throws IOException {

        if (val == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        out.writeUTF(val);
    }

    @Override
    public String toString() {
        return "<UserInfo " + email +
               "\n    name: " + name + ", gender: " + gender + "," +
               "\n    address: " + address + ", phone: " + phone +
               ">";
    }
}
