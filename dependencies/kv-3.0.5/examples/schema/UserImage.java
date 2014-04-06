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

import java.nio.ByteBuffer;

import oracle.kv.Key;
import oracle.kv.Value;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/**
 * Holds the image user attribute that is stored as the {@code Value}
 * for the "/user/EMAIL/-/image" {@code Key}.  Illustrates the use of
 * a single-attribute Key/Value pair.
 *
 * <p>Because the image is expected to be large and is not accessed along
 * with other attributes, it is stored separately from the
 * multi-attribute {@code UserInfo} Key/Value pair.</p>
 */
class UserImage {

    /*
     * The email address is a unique identifier and is used to construct
     * the Key's major path.
     */
    private final String email;

    /* Persistent fields stored in the Value. */
    private byte[] image;

    /**
     * Constructs a user object with its unique identifier, the email address.
     */
    UserImage(String email) {
        this.email = email;
    }

    /**
     * Returns the email identifier.
     */
    String getEmail() {
        return email;
    }

    /**
     * Changes the image bytes.
     */
    void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Returns the image bytes.
     */
    byte[] getImage() {
        return image;
    }

    /**
     * Returns a Key that can be used to write or read the UserImage.
     */
    Key getStoreKey() {
        return KeyDefinition.makeUserImageKey(email);
    }

    /**
     * Deserializes the image into the byte array of a Value.
     * <p>
     * Note that since there is only one field, a byte array, the Value's byte
     * array could be stored directly.  But using an Avro binding allows for
     * the possibility of adding additional fields in the future.
     */
    Value getStoreValue(Bindings bindings) {
        final GenericRecord rec =
            new GenericData.Record(bindings.getUserImageSchema());
        rec.put("image", ByteBuffer.wrap(image));
        return bindings.getUserImageBinding().toValue(rec);
    }

    /**
     * Deserializes the image from the byte array of a Value.
     * <p>
     * Note that since there is only one field, a byte array, the Value's byte
     * array could be stored directly.  But using an Avro binding allows for
     * the possibility of adding additional fields in the future.
     */
    void setStoreValue(Bindings bindings, Value value) {
        final GenericRecord rec =
            bindings.getUserImageBinding().toObject(value);
        final ByteBuffer buf = (ByteBuffer) rec.get("image");
        image = buf.array();
        assert buf.position() == 0;
        assert image.length == buf.limit();
    }

    @Override
    public String toString() {
        return "<UserImage " + email +
               "\n    imageLength: " + image.length + ">";
    }
}
