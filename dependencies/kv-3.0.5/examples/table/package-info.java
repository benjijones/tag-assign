/**
 * The code and scripts in this package demonstrate some of the basic functions
 * of the Oracle NoSQL Database table API, including creation and use of
 * secondary indexes.
 *<p>
 *
 * All example Java code is in the single class {@link table.TableAPIExample}.
 * There are a number of independent example functions in that class.  These
 * can be run together or independently.  They have no interactions.
 *
 * <h3>Before Running the Table Examples</h3>
 * <h4>Start a Store</h4>
 * The examples require a running KVStore instance that has been populated with
 * the example tables and indexes.  A KVLite instance is suitable as a running
 * store.  These instructions assume that a default KVLite instance has been
 * started using the command:
 * <pre>
 *  java -jar lib/kvstore.jar kvlite
 * </pre>
 *
 * This creates a store named "kvstore" on localhost on port 5000.  If you use
 * another store or other parameters, adjust the instructions below accordingly.
 * By default this creates a store in a new "kvroot" directory in your current
 * directory.  The location can be changed using additional parameters.
 * <h4>Create Tables and Indexes</h4>
 * This only needs to be done once for all examples.  In the directory
 * <install_dir>:
 * <pre>
 *  java -jar lib/kvstore.jar runadmin -host localhost -port 5000 load -file \
 *     create_tables.kvs
 * </pre>
 * This will take a minute or so.
 * <h3>Building and Running Examples</h3>
 * <h4>Build the example code</h4>
 * In the directory <install_dir>/examples/table:
 * <pre>
 *  javac -d . -cp <install_dir>/lib/kvclient.jar TableAPIExample.java
 * </pre>
 * <h4>Run the example code</h4>
 * This only requires the client interface, in kvclient.jar.  In the
 * examples/table directory:
 * <pre>
 *  java -cp .:<install_dir>/lib/kvclient.jar table.TableAPIExample     \
 *    [-host localhost] [-port 5000] [-store kvstore] [-example <1-9>]*
 *
 * All parameters have default values:
 * store -- kvstore
 * host -- localhost
 * port -- 5000
 *</pre>
 * By default all examples are run.  More than one example can be run in a
 * single invocation by using the -example flag multiple times.
 *
 * <h3>NOTES</h3>
 * Example 7 will show different results if the script evolve_table.kvs is run.
 * <pre>
 *  java -jar lib/kvstore.jar runadmin -host localhost -port 5000 load -file \
 *     evolve_tables.kvs
 * </pre>
 *<p>
 * After running the examples run the remove_tables.kvs script to clear the
 * tables from the database.  This removes all data as well.
 * <pre>
 *  java -jar lib/kvstore.jar runadmin -host localhost -port 5000 load -file \
 *     remove_tables.kvs
 * </pre>
 */

package table;
