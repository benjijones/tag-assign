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

package com.sleepycat.util;

import java.io.InputStream;

/**
 * A replacement for ByteArrayInputStream that does not synchronize every
 * byte read.
 *
 * <p>This class extends {@link InputStream} and its <code>read()</code>
 * methods allow it to be used as a standard input stream.  In addition, it
 * provides <code>readFast()</code> methods that are not declared to throw
 * <code>IOException</code>.  <code>IOException</code> is never thrown by this
 * class.</p>
 *
 * @author Mark Hayes
 */
public class FastInputStream extends InputStream {

    protected int len;
    protected int off;
    protected int mark;
    protected byte[] buf;

    /**
     * Creates an input stream.
     *
     * @param buffer the data to read.
     */
    public FastInputStream(byte[] buffer) {

        buf = buffer;
        len = buffer.length;
    }

    /**
     * Creates an input stream.
     *
     * @param buffer the data to read.
     *
     * @param offset the byte offset at which to begin reading.
     *
     * @param length the number of bytes to read.
     */
    public FastInputStream(byte[] buffer, int offset, int length) {

        buf = buffer;
        off = offset;
        len = offset + length;
    }

    // --- begin ByteArrayInputStream compatible methods ---

    @Override
    public int available() {

        return len - off;
    }

    @Override
    public boolean markSupported() {

        return true;
    }

    @Override
    public void mark(int readLimit) {

        mark = off;
    }

    @Override
    public void reset() {

        off = mark;
    }

    @Override
    public long skip(long count) {

        int myCount = (int) count;
        if (myCount + off > len) {
            myCount = len - off;
        }
        skipFast(myCount);
        return myCount;
    }

    @Override
    public int read() {
        return readFast();
    }

    @Override
    public int read(byte[] toBuf) {

        return readFast(toBuf, 0, toBuf.length);
    }

    @Override
    public int read(byte[] toBuf, int offset, int length) {

        return readFast(toBuf, offset, length);
    }

    // --- end ByteArrayInputStream compatible methods ---

    /**
     * Equivalent to <code>skip()<code> but takes an int parameter instead of a
     * long, and does not check whether the count given is larger than the
     * number of remaining bytes.
     * @see #skip(long)
     */
    public final void skipFast(int count) {
        off += count;
    }

    /**
     * Equivalent to <code>read()<code> but does not throw
     * <code>IOException</code>.
     * @see #read()
     */
    public final int readFast() {

        return (off < len) ? (buf[off++] & 0xff) : (-1);
    }

    /**
     * Equivalent to <code>read(byte[])<code> but does not throw
     * <code>IOException</code>.
     * @see #read(byte[])
     */
    public final int readFast(byte[] toBuf) {

        return readFast(toBuf, 0, toBuf.length);
    }

    /**
     * Equivalent to <code>read(byte[],int,int)<code> but does not throw
     * <code>IOException</code>.
     * @see #read(byte[],int,int)
     */
    public final int readFast(byte[] toBuf, int offset, int length) {

        int avail = len - off;
        if (avail <= 0) {
            return -1;
        }
        if (length > avail) {
            length = avail;
        }
        System.arraycopy(buf, off, toBuf, offset, length);
        off += length;
        return length;
    }

    /**
     * Returns the underlying data being read.
     *
     * @return the underlying data.
     */
    public final byte[] getBufferBytes() {

        return buf;
    }

    /**
     * Returns the offset at which data is being read from the buffer.
     *
     * @return the offset at which data is being read.
     */
    public final int getBufferOffset() {

        return off;
    }

    /**
     * Returns the end of the buffer being read.
     *
     * @return the end of the buffer.
     */
    public final int getBufferLength() {

        return len;
    }
}
