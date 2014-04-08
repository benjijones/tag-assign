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

/**
 * A factory to create operations that can be batched for execution by {@link
 * KVStore#execute KVStore.execute}.
 * <p>
 * Each operation created here operates on a single key and matches the
 * corresponding operation defined in KVStore. For example, the Operation
 * generated by the {@link #createPut createPut} method corresponds to the
 * {@link KVStore#put put} method. The argument pattern for creating the
 * operation is similar. It differs in the following respects:
 * <ul>
 * <li>
 * The durability argument is not passed, since that argument applies to the
 * execution of the entire batch of operations and is passed in to the {@link
 * KVStore#execute execute} method.
 * </li>
 * <li>
 * {@link ReturnValueVersion.Choice} is passed instead of {@link
 * ReturnValueVersion}.
 * </li>
 * <li>
 * An additional argument, {@code abortIfUnsuccessful} is passed.
 * </li>
 * </ul>
 * </p>
 * <p>
 * The return values associated with operations similarly match the
 * descriptions for the corresponding methods described in in {@link KVStore}.
 * They are, however, retrieved differently: the status, return value, previous
 * value and version are packaged together in {@link OperationResult}.
 * </p>
 */
public interface OperationFactory {

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#put put} method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Put operation
     *
     * @see KVStore#put put
     * @see KVStore#execute execute
     */
    public Operation createPut(Key key, Value value);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#put put} method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link KVStore#put put}
     * method returns null.
     *
     * @return the created Put operation
     *
     * @see KVStore#put put
     * @see KVStore#execute execute
     */
    public Operation createPut(Key key,
                               Value value,
                               ReturnValueVersion.Choice prevReturn,
                               boolean abortIfUnsuccessful);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfAbsent putIfAbsent}
     * method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfAbsent putIfAbsent
     * @see KVStore#execute execute
     */
    public Operation createPutIfAbsent(Key key, Value value);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfAbsent putIfAbsent}
     * method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link
     * KVStore#putIfAbsent putIfAbsent} method returns null.
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfAbsent putIfAbsent
     * @see KVStore#execute execute
     */
    public Operation createPutIfAbsent(Key key,
                                       Value value,
                                       ReturnValueVersion.Choice prevReturn,
                                       boolean abortIfUnsuccessful);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfPresent putIfPresent}
     * method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfPresent putIfPresent
     * @see KVStore#execute execute
     */
    public Operation createPutIfPresent(Key key, Value value);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfPresent putIfPresent}
     * method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link
     * KVStore#putIfPresent putIfPresent} method returns null.
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfPresent putIfPresent
     * @see KVStore#execute execute
     */
    public Operation createPutIfPresent(Key key,
                                        Value value,
                                        ReturnValueVersion.Choice prevReturn,
                                        boolean abortIfUnsuccessful);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfVersion putIfVersion}
     * method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfVersion putIfVersion
     * @see KVStore#execute execute
     */
    public Operation createPutIfVersion(Key key, Value value, Version version);

    /**
     * Create a Put operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * are identical to that of the {@link KVStore#putIfVersion putIfVersion}
     * method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link
     * KVStore#putIfVersion putIfVersion} method returns null.
     *
     * @return the created Put operation
     *
     * @see KVStore#putIfVersion putIfVersion
     * @see KVStore#execute execute
     */
    public Operation createPutIfVersion(Key key,
                                        Value value,
                                        Version version,
                                        ReturnValueVersion.Choice prevReturn,
                                        boolean abortIfUnsuccessful);

    /**
     * Create a Delete operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * when executed are identical to that of the {@link KVStore#delete delete}
     * method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Delete operation
     *
     * @see KVStore#delete delete
     * @see KVStore#execute execute
     */
    public Operation createDelete(Key key);

    /**
     * Create a Delete operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * when executed are identical to that of the {@link KVStore#delete delete}
     * method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link KVStore#delete
     * delete} method returns false.
     *
     * @return the created Delete operation
     *
     * @see KVStore#delete delete
     * @see KVStore#execute execute
     */
    public Operation createDelete(Key key,
                                  ReturnValueVersion.Choice prevReturn,
                                  boolean abortIfUnsuccessful);

    /**
     * Create a Delete operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * when executed are identical to that of the {@link
     * KVStore#deleteIfVersion deleteIfVersion} method.
     *
     * <p>The previous value is not returned by this method and the
     * {@code abortIfUnsuccessful} property is false.</p>
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @return the created Delete operation
     *
     * @see KVStore#deleteIfVersion deleteIfVersion
     * @see KVStore#execute execute
     */
    public Operation createDeleteIfVersion(Key key, Version version);

    /**
     * Create a Delete operation suitable for use as an argument to the {@link
     * KVStore#execute execute} method. The semantics of the returned operation
     * when executed are identical to that of the {@link
     * KVStore#deleteIfVersion deleteIfVersion} method.
     *
     * <p>The result of evaluating the operation, or the resulting exception,
     * after the call to {@link KVStore#execute execute} is available as an
     * {@link OperationResult}.</p>
     *
     * @param abortIfUnsuccessful is true if this operation should cause the
     * {@link KVStore#execute execute} transaction to abort when the operation
     * fails, where failure is the condition when the {@link
     * KVStore#deleteIfVersion deleteIfVersion} method returns false.
     *
     * @return the created Delete operation
     *
     * @see KVStore#deleteIfVersion deleteIfVersion
     * @see KVStore#execute execute
     */
    public Operation
        createDeleteIfVersion(Key key,
                              Version version,
                              ReturnValueVersion.Choice prevReturn,
                              boolean abortIfUnsuccessful);
}