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

package com.sleepycat.utilint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.sleepycat.je.EnvironmentFailureException;

public class StatLogger {
    private final File logFile;
    private final String fileext;
    private final String filename;
    private final File logDir;
    private int maxFileCount;
    private int maxRowCount;
    private String header = null;
    private String lastVal = null;
    private int currentRowCount;

    /**
     * StatLogger is used to write to a log file that contain a header followed
     * by a set of data rows. Parameters control the size and number of
     * rotating log files used. For a rotating set of files, as each file
     * reaches a given size limit, it is closed, rotated out, and a new
     * file opened. The name of the log file is filename.fileext. Successively
     * older files are named by adding "0", "1", "2", etc into the file name.
     * The format is filename.[version number].fileext.
     *
     * @param logdir Log file directory.
     * @param filename Name of the log file.
     * @param fileext Extent of the log file.
     * @param filecount Maximum number of rotating log files to be saved.
     * @param rowcount Maximum number of rows in a log file.
     *
     * @throws IOException if log file or directory cannot be accessed.
     * @throws IllegalArgumentException if the log directory is not
     * a directory or if the log file is not a file.
     */
    public StatLogger(File logdir,
                      String filename,
                      String fileext,
                      int filecount,
                      int rowcount) throws IOException {
        if (!logdir.exists()) {
            logdir.mkdirs();
        } else if (!logdir.isDirectory()) {
            throw new IllegalArgumentException(
                "Specified statistic log directory " +
                 logdir.getAbsolutePath() + " is not a directory.");
        }

        logFile = new File(logdir.getAbsolutePath() + File.separator +
                           filename + "." + fileext);
        this.maxFileCount = filecount - 1;
        this.maxRowCount = rowcount;
        this.filename = filename;
        this.fileext = fileext;
        this.logDir = logdir;
        if (logFile.exists()) {

            if (!logFile.isFile()) {
                throw new IllegalArgumentException(
                    "Statistic log file" + logFile.getAbsolutePath() +
                     " exists but is not a file.");
            }

            header = getHeader();
            /* set current row count. */
            getLastRow();
        }
    }

    /**
     * Sets the maximum log file row count.
     *
     * @param rowcount The maximum number of rows per log file.
     */
    public void setRowCount(int rowcount) {
        maxRowCount = rowcount;
    }

    /**
     * Set the maximum number of log files to keep after rotation.
     *
     * @param filecount The maximum number of log files to keep.
     */
    public void setFileCount(int filecount) {
       filecount--;
       if (maxFileCount > filecount) {
           /* remove files that are greater then the new filecount */
           for (int i = maxFileCount; i > filecount; i--) {
               File deleme = new File(formFn(i - 2));
               if (deleme.exists()) {
                   deleme.delete();
               }
           }
       }
       maxFileCount = filecount;
    }

    /**
     * Sets the log file header. A new log file may be created if
     * the header does not match the header in the existing file.
     *
     * @param val  Header row data.
     *
     * @throws
     */
    public void setHeader(String val) throws IOException {
        if (!val.equals(header)) {
            if (header != null) {
                /* file headers are different so rotate files */
                rotateFiles();
            }
            currentRowCount++;
            write(val);
            header = val;
            lastVal = null;
        }
    }

    /**
     * log writes the string to the log file.
     *
     * @param val Value to write to the log.
     * @throws IOException
     */
    public void log(String val) throws IOException {
        if (currentRowCount >= maxRowCount)
        {
            rotateFiles();
            currentRowCount++;
            write(header);
        }
        currentRowCount++;
        write(val);
        lastVal = val;

    }

    /**
     * logDelta writes the string if the string is different
     * than the last written log record. The first column is
     * ignored when checking for a difference (current
     * implementation has the time the record is logged as the
     * first column.
     *
     * @param val value write to the log.
     * @throws IOException
     */
    public void logDelta(String val) throws IOException {

        if (header == null) {
            throw EnvironmentFailureException.unexpectedState(
                "Unexpected state logHeader not called before logDelta.");
        }
        if (lastVal == null) {
            lastVal = getLastRow();
        }
        String lastNoFirst = null;
        if (lastVal != null) {
            lastNoFirst = lastVal.substring(lastVal.indexOf(',') + 1);
        }
        if (!val.substring(val.indexOf(',') + 1).equals(lastNoFirst)) {
            log(val);
        }
    }

    private String getHeader() throws IOException {
        String header;
        BufferedReader fr = null;
        try {
            fr = new BufferedReader(new FileReader(logFile));
            header = fr.readLine();
        } catch (FileNotFoundException e) {
            throw new IOException(
                    "Error occured accessing statistic log file " +
                    "FileNotFoundException " +
                     logFile.getAbsolutePath(), e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                }
                catch (IOException e) {
                    /* eat exception */
                }
            }
        }

        return header;
    }

    private String getLastRow() throws IOException {
        String row;
        BufferedReader fr = null;
        currentRowCount = 0;
        try {
            fr = new BufferedReader(new FileReader(logFile));
            String prevrow = null;
            while ((row = fr.readLine()) != null) {
                currentRowCount++;
                prevrow = row;
            }
            return prevrow;

        } catch (FileNotFoundException e) {
            throw new IOException(
                    "Error occured accessing statistic log file " +
                    "FileNotFoundException " +
                     logFile.getAbsolutePath(), e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                }
                catch (IOException e) {
                    /* eat exception */
                }
            }
        }
    }

    private String formFn(int version) {
        if (version < 0) {
            return logDir.getAbsolutePath() + File.separator +
                   filename + "." + fileext;
        } else {
            return logDir.getAbsolutePath() + File.separator + filename +
                "." + version + "." + fileext;
        }
    }

    private void write(String val) throws IOException
    {
        PrintWriter ps = null;
        try {
            ps = new PrintWriter(new FileWriter(logFile, true));
            ps.println(val);
        } catch (FileNotFoundException e) {
            throw new IOException(
                "Error occured accessing statistic log file " +
                "FileNotFoundException " +
                 logFile.getAbsolutePath(), e);
        } finally {
            if (ps != null) {
                ps.flush();
                ps.close();
            }
        }
    }

    private void rotateFiles() {
        File cf = new File(formFn(maxFileCount - 1));
        if (cf.exists()) {
            cf.delete();
        }
        for (int i = maxFileCount - 2; i >= -1; i--) {
            cf = new File(formFn(i));
            if (cf.exists()) {
                cf.renameTo(new File(formFn(i + 1)));
            }
        }
        currentRowCount = 0;
    }
}
