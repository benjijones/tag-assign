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

package table;

import java.util.List;

import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Version;
import oracle.kv.table.ArrayValue;
import oracle.kv.table.Index;
import oracle.kv.table.IndexKey;
import oracle.kv.table.MapValue;
import oracle.kv.table.PrimaryKey;
import oracle.kv.table.ReadOptions;
import oracle.kv.table.RecordValue;
import oracle.kv.table.Row;
import oracle.kv.table.Table;
import oracle.kv.table.TableAPI;
import oracle.kv.table.TableIterator;

/**
 * This file includes a number of examples that demonstrate some of the
 * features of tables and secondary indexes in Oracle NoSQL Database.
 * <p>
 * See the {@link table package documentation} for instructions on
 * how to build and run these examples.  They require a running store that
 * has had some required tables and indexes created.  The usage for this
 * program is as follows:
 * <pre>
 * java table.TableAPIExample [-store &lt;instance name&gt;] \
 *                            [-host  &lt;host name&gt;]     \
 *                            [-port  &lt;port number&gt;]   \
 *                            [-example &ltall | example_to_run&gt;]
 * </pre>
 * By default all examples are run.  Multiple examples can be run using the
 * -example flag multiple times.
 * <p>
 * All examples use the default store, host, and port of <i>kvstore, localhost,
 * and 5000</i>.  These are the defaults for KVLite as well.
 * <h3>Description of Examples</h3>
 * <ol>
 * <li>
 * Creates a very simple table and does simple put and get of a row.
 * </li>
 * <li>
 * Uses simple and composite indexese on the table <i>simpleUsers</i> to
 * demonstrate index scans.
 * </li>
 * <li>
 * This example uses a table that has a composite primary key and
 * a defined shard key.  This demonstrates the ability to ensure that
 * rows with the same shard key are stored in the same shard and are
 * therefore accessible in an atomic manner.  Such rows can also be
 * accessed using the various "multi*" API operations.
 * </li>
 * <li>
 * This example uses a parent/child table relationship to demonstrate how
 * to put and get to/from a child table.  It also uses an index on the child
 * table to retrieve child rows via the index.
 * </li>
 * <li>
 * This example uses a table with complex fields (Record, Array, Map) to
 * demonstrate input and output of rows in tables with such fields.
 * </li>
 * <li>
 * This example uses an index on the array from the complex field example
 * table to demonstrate use of an array index.
 * </li>
 * <li>
 * Demonstrates use of table version, row table version, and the Version of a
 * row to handle changes related to table schema evolution.  This example
 * requires running the evolve_table.kvs script to see changes.
 * </li>
 * <li>
 * This example uses tables that were created in R2 compatibility mode where
 * tables are intended to overlay existing R2 data.  This is possible for
 * R2 key-only applications as well as R2 applications with Avro schemas that
 * are compatible with tables (most will be, but not all).
 * </li>
 * <li>
 * This is a simple example of a key-only table.
 * </li>
 * </ol>
 */
public class TableAPIExample {

    private final KVStore store;

    private final TableAPI tableAPI;

    private static String hostName;
    private static String hostPort;
    private static boolean[] runExample = new boolean[10];

	/**
	 * Runs the TableAPIExample command line program.
	 */
    public static void main(String args[]) {
        try {
            TableAPIExample example = new TableAPIExample(args);
            if (runExample[0] || runExample[1]) {
				example.runExample1();
			}
            if (runExample[0] || runExample[2]) {
				example.runExample2();
			}
            if (runExample[0] || runExample[3]) {
				example.runExample3();
			}
            if (runExample[0] || runExample[4]) {
				example.runExample4();
			}
            if (runExample[0] || runExample[5]) {
				example.runExample5();
			}
            if (runExample[0] || runExample[6]) {
				example.runExample6();
			}
            if (runExample[0] || runExample[7]) {
				example.runExample7();
			}
            if (runExample[0] || runExample[8]) {
				example.runExample8();
			}
            if (runExample[0] || runExample[9]) {
				example.runExample9();
			}
            example.closeStore();
        } catch (Error e) {
            System.err.println(e.getMessage());
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    /**
     * Parses command line args and opens the KVStore.
     */
    TableAPIExample(String argv[]) {
        String storeName = "kvstore";
        hostName = "localhost";
        hostPort = "5000";

        final int nArgs = argv.length;
        int argc = 0;

        runExample[0] = true;
        for (int i = 1; i < 10; i++)
        {
           runExample[i] = false;
        }

        /* Parse command line args */
        while (argc < nArgs) {
            final String thisArg = argv[argc++];

            if (thisArg.equals("-store")) {
                if (argc < nArgs) {
                    storeName = argv[argc++];
                } else {
                    usage("-store requires an argument");
                }
            } else if (thisArg.equals("-host")) {
                if (argc < nArgs) {
                    hostName = argv[argc++];
                } else {
                    usage("-host requires an argument");
                }
            } else if (thisArg.equals("-port")) {
                if (argc < nArgs) {
                    hostPort = argv[argc++];
                } else {
                    usage("-port requires an argument");
                }
            } else if (thisArg.equals("-example")) {
                if (argc < nArgs) {
                    String example = argv[argc++];
                    if (!example.equals("all")) {
                        runExample[0]=false;
                        int run = Integer.parseInt(example);
                        if (run > 9) {
							throw new Error("Invalid example number: " + run
                                    + ". Must be one of [all, 1 ... 9]");
						}
                        runExample[run] = true;
                    }
                } else {
                    usage("-port requires an argument");
                }
            } else if (thisArg.equals("?") || thisArg.equals("help")) {
                usage(null);
            } else {
                usage("Unknown argument: " + thisArg);
            }
        }
        /* Connect to the NoSQL store */
        store = KVStoreFactory.getStore
            (new KVStoreConfig(storeName, hostName + ":" + hostPort));
        tableAPI = store.getTableAPI();
    }

    /**
     * Closes the store.
     */
    private void closeStore() {
        store.close();
    }

    private void usage(String message) {
        if (message != null) {
            System.out.println("\n" + message + "\n");
        }
        System.out.println("usage: " + getClass().getName());
        System.out.println("\t[-store <instance name>] (default: kvstore)\n" +
                           "\t[-host <host name>] (default: localhost)\n" +
                           "\t[-port <port number>] (default: 5000)\n" +
                           "\t[-example <example number>] (default: all)");
        System.exit(1);
    }

    /**
     * Example 1: Hello World!
     *
     * This example shows a simple use of tables, it uses a simple table
     * holding an id field as a primary key and 2 fields -- name and surname --
     * as Strings.  It does basic put and get.
     */
    void runExample1() {
        System.out.println("\nExample 1: Hello World!");
        final String tableName = "simpleUsers";

        Table table = getTable(tableName);

        /* Insert rows to table */
        try {
            /* Insert row */
            Row row = table.createRow();
            row.put("userID", 1);
            row.put("firstName", "Alex");
            row.put("lastName", "Robertson");
            tableAPI.put(row, null, null);
            /* Insert row, if it is not already in the table */
            row = table.createRow();
            row.put("userID", 2);
            row.put("firstName", "John");
            row.put("lastName", "Johnson");
            tableAPI.putIfAbsent(row, null, null);
            /* Insert row, if it is already in the table */
            row = table.createRow();
            row.put("userID", 2);
            row.put("firstName", "John");
            row.put("lastName", "Jameson");
            tableAPI.putIfPresent(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Read a row from table using the primary key
         */
        try {

            /* Create a primary key and assign the field value */
            PrimaryKey key = table.createPrimaryKey();
            key.put("userID", 1);

            /* Get the matching row */
            Row row = tableAPI.get(key, new ReadOptions(null, 0, null));
            if (row == null) {
                throw new Error("No matching row found");
            }

            /* Print the full row as JSON */
            System.out.println(row.toJsonString(true));

            /* Access a specific field */
            System.out.println("firstName field as JSON: " +
                               row.get("firstName").toJsonString(false));
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Example 2: Secondary Index
     *
     * This example uses 2 indexes on the table "simpleUsers."  One is a
     * simple, single-field index on "firstName" and the other is a
     * composite index on "lastName, firstName".
     */
    void runExample2() {
        System.out.println("\nExample 2: Secondary Indexes");
        final String tableName = "simpleUsers";

        Table table = getTable(tableName);

        /* Insert rows into table */
        try {
            /* Insert new rows */
            Row row = table.createRow();
            row.put("userID", 3);
            row.put("firstName", "Joel");
            row.put("lastName", "Robertson");
            tableAPI.putIfAbsent(row, null, null);

            row = table.createRow();
            row.put("userID", 4);
            row.put("firstName", "Bob");
            row.put("lastName", "Jameson");
            tableAPI.putIfAbsent(row, null, null);

            row = table.createRow();
            row.put("userID", 5);
            row.put("firstName", "Jane");
            row.put("lastName", "Jameson");
            tableAPI.putIfAbsent(row, null, null);

            row = table.createRow();
            row.put("userID", 6);
            row.put("firstName", "Joel");
            row.put("lastName", "Jones");
            tableAPI.putIfAbsent(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        TableIterator<Row> iter = null;
        try {

            /*
             * Use the simple index on firstName to retrieve all users with
             * the firstName of "Joel"
             */
            Index simple_index = table.getIndex("simpleIndex");
            if (simple_index == null) {
				throw new Error("Index not found");
			}

            /*
             * Create an IndexKey and assign the firstName value.
             * The IndexKey works similarly to a PrimaryKey and only allows
             * assignment of fields that are part of the index.
             */
            IndexKey simpleIndexKey = simple_index.createIndexKey();
            simpleIndexKey.put("firstName", "Joel");

            /* Get the matching rows */
            iter = tableAPI.tableIterator(simpleIndexKey, null, null);

            /* Print rows as JSON */
            System.out.println("\nUsers with firstName Joel");
            while(iter.hasNext()) {
                System.out.println(iter.next().toJsonString(true));
            }

            /* TableIterator instances must be closed to release resources */
            iter.close();
            iter = null; /* will be reused below */

            /*
             * Use the composite index to match both last and firstname.
             */
            Index compound_index = table.getIndex("compoundIndex");
            if (compound_index == null) {
				throw new Error("Index not found");
			}

            /*
             * Create and initialize the IndexKey
             */
            IndexKey compositeIndexKey = compound_index.createIndexKey();
            compositeIndexKey.put("firstName", "Bob");
            compositeIndexKey.put("lastName", "Jameson");

            /* Get the matching rows */
            iter = tableAPI.tableIterator(compositeIndexKey, null, null);

            /* Print rows as JSON */
            System.out.println("\nUsers with full name Bob Jameson");
            while(iter.hasNext()) {
                System.out.println(iter.next().toJsonString(true));
            }
            iter.close();
            iter = null;

            /*
             * Use the composite index to match all rows with
             * lastName "Jameson".
             */
            compositeIndexKey = compound_index.createIndexKey();
            compositeIndexKey.put("lastName", "Jameson");

            /* Get the matching rows */
            iter = tableAPI.tableIterator(compositeIndexKey, null, null);

            /* Print rows as JSON */
            System.out.println("\nAll users with last name Jameson");
            while(iter.hasNext()) {
                System.out.println(iter.next().toJsonString(true));
            }

        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        } finally {

            /*
             * Make sure that the TableIterator is closed.
             */
            if (iter != null) {
                iter.close();
            }
        }
    }

    /**
     * Example 3: Shard keys
     *
     * This example uses a table that has a composite primary key and
     * a defined shard key.  This demonstrates the ability to ensure that
     * rows with the same shard key are stored in the same shard and are
     * therefore accessible in an atomic manner.  Such rows can also be
     * accessed using the various "multi*" API operations.
     */
    void runExample3() {
        System.out.println("\nExample 3: Shard Keys");
        final String tableName = "shardUsers";

        Table table = getTable(tableName);

        /* Insert rows into table */
        try {
            /*
             * The primary key is (lastName, firstName) and the shard key
             * is lastName.
             */
            Row row = table.createRow();
            row.put("firstName", "Alex");
            row.put("lastName", "Robertson");
            row.put("email", "alero@email.com");

            /*
             * Insert a second row with lastName Robertson.
             * Since the previous row is inserted with the same shard key,
             * this row and the previous are guaranteed to be stored on the
             * same shard.
             */
            row = table.createRow();
            row.put("firstName", "Beatrix");
            row.put("lastName", "Robertson");
            row.put("email", "bero@email.com");
            tableAPI.put(row, null, null);

            /*
             * Insert row with lastName Swanson.
             * Since this row has a different shard key the row may be stored
             * in a different shard.
             */
            row = table.createRow();
            row.put("firstName", "Bob");
            row.put("lastName", "Swanson");
            row.put("email", "bob.swanson@email.com");
            tableAPI.put(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /* Use a complete shard key to allow use of multiGet() */
        try {
            PrimaryKey key = table.createPrimaryKey();

            /* shard key is "lastName" */
            key.put("lastName", "Robertson");

            /*
             * Use the multiget function, to retrieve all the rows with the same
             * shard key.  The tableIterator() API call will also work but
             * may not be atomic.
             */
            List<Row> rows = tableAPI.multiGet(key, null, null);

            /* Print the rows as JSON */
            System.out.println("\nRows with lastName Robertson via multiGet()");
            for (Row row: rows) {
                System.out.println(row.toJsonString(true));
            }
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Example 4: Parent and Child tables
     *
     * This example demonstrates use of parent and child tables.  The parent
     * table is the shardUsers table used above and the child table is a
     * table of addresses for a given user, allowing definition of multiple
     * addresses for a user.
     */
    void runExample4() {
        System.out.println("\nExample 4: Parent and Child tables");
        final String parentTableName = "shardUsers";
        final String childTableName = "address";

        Table parentTable = getTable(parentTableName);
        Table childTable = parentTable.getChildTable(childTableName);

        /*
         * Insert rows into the child table.  Create a parent table
         * record first.
         */
        try {

            /* Create a parent (user) row. */
            Row row = parentTable.createRow();
            row.put("firstName", "Robert");
            row.put("lastName", "Johnson");
            row.put("email", "bobbyswan@email.com");
            tableAPI.put(row, null, null);

            /*
             * Create multiple child rows for the same parent.  To do this
             * create a row using the child table but be sure to set the
             * inherited parent table primary key fields (firstName and
             * lastName).
             */
            row = childTable.createRow();

            /* Parent key fields */
            row.put("firstName", "Robert");
            row.put("lastName", "Johnson");
            /* Child key */
            row.put("addressID", 1);
            /* Child data fields */
            row.put("Street", "Street Rd 132");
            row.put("State", "California");
            row.put("ZIP", 90011);
            row.put("addressName", "home");
            tableAPI.putIfAbsent(row, null, null);

            /*
             * Reuse the Row to avoid repeating all fields.  This is safe.
             * This requires a new child key and data.
             */
            row.put("addressID", 2);
            row.put("Street", "Someplace Ave. 162");
            row.put("State", "California");
            row.put("ZIP", 90014);
            row.put("addressName", "work");
            tableAPI.putIfAbsent(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Retrieve rows from the child table
         */
        try {

            /*
             * The child table primary key is a concatenation if its parent
             * table's primary key and it's own defined fields.
             */
            PrimaryKey key = childTable.createPrimaryKey();
            key.put("firstName", "Robert");
            key.put("lastName", "Johnson");
            key.put("addressID", 1);
            Row row = tableAPI.get(key, null);

            System.out.println(row.toJsonString(true));

            /*
             * There is an index on the "addressName" field of the child
             * table.  Use that to retrieve all "work" addresses.
             */
            Index index = childTable.getIndex("addressIndex");
            IndexKey indexKey = index.createIndexKey();
            indexKey.put("addressName", "work");
            TableIterator<Row> iter =
                tableAPI.tableIterator(indexKey, null, null);

            System.out.println("\nAll \"work\" addresses");
            while(iter.hasNext()) {
                row = iter.next();
                System.out.println(row.toJsonString(true));
            }
            iter.close();
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Example 5: Complex fields
     *
     * This example demonstrates how to create, populate and read complex
     * fields (Array, Map, Record) in a table.
     */
    void runExample5() {
        System.out.println("\nExample 5: Complex fields");
        final String tableName = "complexUsers";

        Table table = getTable(tableName);

        /*
         * Insert rows into table.
         */
        try {

            /*
             * Insert data into complex fields.
             *
             * Starts with an empty row.
             */
            Row row = table.createRow();

            /*
             * The putRecord function creates a RecordValue instance that
             * is then populated with its own fields.
             * The "name" field is a record with 2 fields --
             * firstName and lastName.
             */
            RecordValue recordValue = row.putRecord("name");
            recordValue.put("firstName", "Bob");
            recordValue.put("lastName", "Johnson");

            /*
             * The putArray function returns an ArrayValue instance which
             * is then populated.  In this case the array is an array of
             * String values.
             */
            ArrayValue arrayValue = row.putArray("likes");

            /* use the add() overload that takes an array as input */
            arrayValue.add(new String[]{"sports", "movies"});

            /*
             * The putMap function returns a MapValue instance which is then
             * populated.  In this table the map is a map of String values.
             */
            MapValue mapValue = row.putMap("optionalInformation");
            mapValue.put("email", "bob.johnson@email.com");
            mapValue.put("group", "work");

            /* Insert id */
            row.put("userID", 1);
            tableAPI.putIfAbsent(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Retrieve information from the table, examining the complex fields.
         */
        try {

            /* Get row matching the requested primary key. */
            PrimaryKey key = table.createPrimaryKey();
            key.put("userID", 1);
            Row row = tableAPI.get(key, null);

            /*
             * Read the "name" record field.  Use asRecord() to cast.  If this
             * is done on a field that is not a record an exception is
             * thrown.
             */
            RecordValue record = row.get("name").asRecord();

            /* The RecordValue can be output as JSON */
            System.out.println("\nName record: " + record.toJsonString(false));

            /*
             * Read the "likes" array field.
             * Use the get function to return the field and cast it to
             * ArrayValue using the asArray function.
             */
            ArrayValue array = row.get("phoneNumber").asArray();

            /* The ArrayValue can be output as JSON */
            System.out.println("\nlikes array: " +
                               array.toJsonString(false));

            /*
             * Read the map field.
             */
            MapValue map = row.get("optionalInformation").asMap();

            /* The MapValue can be output as JSON */
            System.out.println("\noptionalInformation map: " +
                               map.toJsonString(false));

            /* Print the entire row as JSON */
            System.out.println("\n The full row:\n" +
                               row.toJsonString(true));
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Example 6: Secondary index on array
     *
     * This example demonstrates use of a secondary index on an array.
     * An important thing to note about indexes on arrays is that a
     * independent index entry is generated for each value in the array.
     * This can lead to an explosion of entries as well as potential for
     * duplication in results (e.g. if the same array value repeats in
     * the same row).
     *
     * This example uses the same table as the complex field example.
     * There is an index on the "likes" array.
     */
    void runExample6(){
        System.out.println("\nExample 6: Secondary index on array");
        final String tableName = "complexUsers";

        Table table = getTable(tableName);

        /*
         * Insert rows into table.
         */
        try {

            Row row = table.createRow();
            RecordValue recordValue = row.putRecord("name");
            recordValue.put("firstName", "Joseph");
            recordValue.put("lastName", "Johnson");
            ArrayValue arrayValue = row.putArray("likes");
            arrayValue.add(new String[]{"sports"});
            MapValue mapValue = row.putMap("optionalInformation");
            mapValue.put("email", "jjson@email.com");
            row.put("userID", 2);
            tableAPI.putIfAbsent(row, null, null);

            row = table.createRow();
            recordValue = row.putRecord("name");
            recordValue.put("firstName", "Burt");
            recordValue.put("lastName", "Nova");
            arrayValue = row.putArray("likes");
            arrayValue.add(new String[]{"sports", "movies", "technology"});
            mapValue = row.putMap("optionalInformation");
            row.put("userID", 3);
            tableAPI.putIfAbsent(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Retrieve information using the secondary index
         */
        try {

            /*
             * Retrieve all the rows with the value "movies" in the
             * "likes" array field.
             */
            Index index = table.getIndex("arrayIndex");
            if (index == null) {
				throw new Error("Indexnot found");
			}

            /*
             * Create an IndexKey to request the information.
             * The IndexKey works similarly to a PrimaryKey but when handling
             * secondary indexes on complex fields like an array use the
             * putArray function to retrieve the ArrayValue and set the value
             * to look for.  When used as an index key only one value may
             * be set in the array.  If used incorrectly an exception is
             * thrown when the resulting IndexKey is used.
             */
            IndexKey indexKey = index.createIndexKey();
            ArrayValue arrayIndex = indexKey.putArray("likes");
            arrayIndex.add("movies"); /* match "movies" */

            TableIterator<Row> iter =
                    tableAPI.tableIterator(indexKey, null, null);
            Row row;
            System.out.println("\nUsers who \"like\" movies");
            while (iter.hasNext()) {
                row = iter.next();
                System.out.println(row.toJsonString(true));
            }
            iter.close();

            /*
             * Do it again with "sports"
             */
            arrayIndex = indexKey.putArray("likes");
            arrayIndex.add("sports");
            iter = tableAPI.tableIterator(indexKey, null, null);
            System.out.println("\nUsers who \"like\" sports");
            while (iter.hasNext()) {
                row = iter.next();
                System.out.println(row.toJsonString(true));
            }
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }


    /**
     * Example 7: Evolve Table
     *
     * This example requires use of the evolve_table.kvs script to help
     * demonstrate how to handle schema evolution of a table.
     *
     * It shows how to evolve a table using CLI commands and how to
     * protect code from accessing older version tables using the
     * getTableVersion and the Version returned by row operations.
     *
     * NOTE: This example needs to run the script evolve_table to show
     * the differences between an evolved table and a new table.
     * kv-> load -file &lt;store directory&gt;/example/tables/evolve_table.kvs
     *
     * The evolution does this:
     * 1. removes the "phoneNumber" field
     * 2. adds a String "comment" field
     *
     * To see a change, run this example, then run evolve_table.kvs and
     * run the example again.  When this is done you will see the default
     * value set for the new "comment" field and the fact that the removed
     * "phoneNumber" field does not appear.
     *
     * In a real application a combination of the table version, the row's
     * table version (the table version used to write the row) and the row's
     * Version object can be used to protect the application from unexpected
     * changes.
     */
    void runExample7() {
        System.out.println("\nExample 7: Evolve table");
        final String tableName = "evolveUsers";

        /*
         * Get table and table version information.
         */
        Table table = getTable(tableName);
        int version = table.getTableVersion();
        System.out.println("Table version is " + version);

        /*
         * See if the row exists from a previous operation
         */
        PrimaryKey key = table.createPrimaryKey();
        key.put("userID", 1);
        Row row = tableAPI.get(key, null);
        if (row != null) {
            System.out.println("Row exists, row was written with table version "
                               + row.getTableVersion() +
                               "\n" + row.toJsonString(true));
        }

        /*
         * Check table version, a table when created starts as version 1 and
         * each time it is modified it increase version by 1.
         */
        row = table.createRow();
        if (version == 1) {
            /* Insert values in table */
            row.put("userID", 1);
            row.put("firstName", "Alex");
            row.put("lastName", "Robertson");
            row.put("phoneNumber", 2125552222);

            /*
             * The put functions of the table API return the row Version of the
             * row being inserted or updated.
             */
            Version rowVersion = tableAPI.put(row, null, null);

            /* Read the inserted row */
            key = table.createPrimaryKey();
            key.put("userID", 1);
            row = tableAPI.get(key, new ReadOptions(null, 0, null));

            System.out.println(row.toJsonString(true));

            /*
             * Update row if no change has been made to the row.
             * This is useful when different applications can modify the
             * contents of the row.
             */
            row = table.createRow();
            row.put("userID", 1);
            row.put("firstName", "Alex");
            row.put("lastName", "Robertson");
            row.put("phoneNumber", 333124987);

            /*
             * The putIfVersion function updates a row if the current version
             * of the row is the same as the the rowVersion. This let us know
             * that no other query has updated this row.
             */
            rowVersion = tableAPI.putIfVersion(row, rowVersion, null, null);
            key = table.createPrimaryKey();
            key.put("userID", 1);
            row = tableAPI.get(key, new ReadOptions(null, 0, null));

            /* Read the updated row */
            System.out.println(row.toJsonString(true));
        } else {
            row.put("userID", 1);
            row.put("firstName", "Alex");
            row.put("lastName", "Robertson");
            row.put("comment", "this is a comment.");
            tableAPI.put(row, null, null);

            /* Read the inserted row */
            key = table.createPrimaryKey();
            key.put("userID", 1);
            row = tableAPI.get(key, new ReadOptions(null, 0, null));
            System.out.println(row.toJsonString(true));
        }
    }

    /**
     * Example 8: R2 compatibility example
     *
     * This example uses a table created using an avro schema and a table
     * with r2 compatibility enabled.
     *
     * These features are intended for migration of the key/value API to the
     * table API.  Such migration is required in order to add indexes on
     * R2 data.
     *
     * TODO: add actual R2 data to this example to show compatibility.
     */
    void runExample8() {
        System.out.println("\nExample 8: compatibility examples");

        /*
         * Get a simple table that is compatible with R2 data.
         *
         * If the -r2-compat flag is not set, the underlying keys
         * generated for the table's entries would not match those used
         * by the R2 key/value application.
         */
        final String r2TableName = "r2Table";

        Table r2Table = getTable(r2TableName);

        /*
         * Get a table loaded from an Avro schema.
         *
         * Tables loaded from an Avro schema are implicitly compatible
         * with R2 applications that use the matching schema.
         */
        final String avroTableName = "avroTable";

        Table avroTable = getTable(avroTableName);

        /*
         *  Insert rows into tables
         */
        try {

            /*
             * Insert a row into the key-only r2 table
             */
            Row row = r2Table.createRow();
            row.put("userID", "ID1235");
            tableAPI.put(row, null, null);

            /*
             * Insert a row into the table created from an Avro schema.
             */
            row = avroTable.createRow();
            RecordValue fullName = row.putRecord("name");
            fullName.put("first", "Bob");
            fullName.put("last", "Johnson");
            row.put("age", 32);
            RecordValue address = row.putRecord("Address");
            address.put("street", "Street Blvd.");
            address.put("city", "Grand City");
            address.put("state", "Big State");
            address.put("zip", 45110);
            row.put("userID", "ID15478");
            tableAPI.put(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Retrieve information from tables
         */
        try {

            /*
             * Read the key-only row.
             */
            PrimaryKey key = r2Table.createPrimaryKey();
            key.put("userID", "ID1235");
            Row row = tableAPI.get(key, null);
            System.out.println(row.toJsonString(true));

            /*
             * Read a row from the Avro-generated table.
             */
            key = avroTable.createPrimaryKey();
            key.put("userID", "ID15478");
            row = tableAPI.get(key, new ReadOptions(null, 0, null));
            System.out.println(row.toJsonString(true));
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Example 9: Primary key table
     *
     * This example shows how all the fields in a table can be assigned
     * as part of the primary key as a key-only table.
     */
    void runExample9() {
        System.out.println("\nExample 9: Primary key table");
        final String tableName = "keyTable";

        Table table = getTable(tableName);

        /*
         * Insert rows into the table
         */
        try {
            Row row = table.createRow();
            row.put("ITEMID", 2435871);
            row.put("ITEMSKU", "XSD4785HT");
            row.putEnum("ITEMGROUP", "TYPE2");
            row.put("ID", 1);
            tableAPI.put(row, null, null);
            row = table.createRow();
            row.put("ITEMID", 1547243);
            row.put("ITEMSKU", "ART5785XT");
            row.putEnum("ITEMGROUP", "TYPE2");
            row.put("ID", 2);
            tableAPI.put(row, null, null);
        } catch (Exception e) {
            System.err.println("Exception during insert: " + e);
        }

        /*
         * Read a row using primary keys
         */
        try {
            PrimaryKey key = table.createPrimaryKey();
            key.put("ITEMID", 1547243);
            key.putEnum("ITEMGROUP", "TYPE2");
            key.put("ITEMSKU", "ART5785XT");
            key.put("ID", 2);
            Row row = tableAPI.get(key, new ReadOptions(null, 0, null));
            System.out.println(row.toJsonString(true));
        } catch (Exception e) {
            System.err.println("Exception during read: " + e);
        }
    }

    /**
     * Gets the named table.  If it is not present an error is generated.
     */
    private Table getTable(String tableName) {
        Table table = tableAPI.getTable(tableName);
        if (table == null) {
            String msg = "Could not locate table: " + tableName +
                ". The script create_tables.kvs must be run";
            throw new Error(msg);
        }
        return table;
    }

    /**
     * Encapsulates example errors.
     */
    private class Error extends RuntimeException {
    	private static final long serialVersionUID = 1L;

		Error(String message) {
            super(message);
        }
    }
}
