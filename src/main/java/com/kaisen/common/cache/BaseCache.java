package com.kaisen.common.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.KeyProvider;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.MemcachedClientStateListener;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.ReconnectRequest;
import net.rubyeye.xmemcached.networking.Connector;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.Protocol;

public abstract class BaseCache<T> {
	protected abstract MemcachedClient getMemcachedClient();

	/**
	 * Set the merge factor,this factor determins how many 'get' commands would
	 * be merge to one multi-get command.default is 150
	 * 
	 * @param mergeFactor
	 */
	public void setMergeFactor(final int mergeFactor) {
		getMemcachedClient().setMergeFactor(mergeFactor);
	}

	/**
	 * Get the connect timeout
	 * 
	 */
	public long getConnectTimeout() {
		return getMemcachedClient().getConnectTimeout();
	}

	/**
	 * Set the connect timeout,default is 1 minutes
	 * 
	 * @param connectTimeout
	 */
	public void setConnectTimeout(long connectTimeout) {
		getMemcachedClient().setConnectTimeout(connectTimeout);
	}

	/**
	 * return the session manager
	 * 
	 * @return
	 */
	public Connector getConnector() {
		return getMemcachedClient().getConnector();
	}

	/**
	 * Enable/Disable merge many get commands to one multi-get command.true is
	 * to enable it,false is to disable it.Default is true.Recommend users to
	 * enable it.
	 * 
	 * @param optimizeGet
	 */
	public void setOptimizeGet(final boolean optimizeGet) {
		getMemcachedClient().setOptimizeGet(optimizeGet);
	}

	/**
	 * Enable/Disable merge many command's buffers to one big buffer fit
	 * socket's send buffer size.Default is true.Recommend true.
	 * 
	 * @param optimizeMergeBuffer
	 */
	public void setOptimizeMergeBuffer(final boolean optimizeMergeBuffer) {
		getMemcachedClient().setOptimizeMergeBuffer(optimizeMergeBuffer);
	}

	/**
	 * @return
	 */
	public boolean isShutdown() {
		return getMemcachedClient().isShutdown();
	}

	/**
	 * Aadd a memcached server,the thread call this method will be blocked until
	 * the connecting operations completed(success or fail)
	 * 
	 * @param server
	 *            host string
	 * @param port
	 *            port number
	 */
	public void addServer(final String server, final int port)
			throws IOException {
		getMemcachedClient().addServer(server, port);
	}

	/**
	 * Add a memcached server,the thread call this method will be blocked until
	 * the connecting operations completed(success or fail)
	 * 
	 * @param inetSocketAddress
	 *            memcached server's socket address
	 */
	public void addServer(final InetSocketAddress inetSocketAddress)
			throws IOException {
		getMemcachedClient().addServer(inetSocketAddress);
	}

	/**
	 * Add many memcached servers.You can call this method through JMX or
	 * program
	 * 
	 * @param host
	 *            String like [host1]:[port1] [host2]:[port2] ...
	 */
	public void addServer(String hostList) throws IOException {
		getMemcachedClient().addServer(hostList);
	}

	/**
	 * Get current server list.You can call this method through JMX or program
	 */
	public List<String> getServersDescription() {
		return getMemcachedClient().getServersDescription();
	}

	/**
	 * Remove many memcached server
	 * 
	 * @param host
	 *            String like [host1]:[port1] [host2]:[port2] ...
	 */
	public void removeServer(String hostList) {
		getMemcachedClient().removeServer(hostList);
	}

	/**
	 * Get value by key
	 * 
	 * @param <T>
	 * @param key
	 *            Key
	 * @param timeout
	 *            Operation timeout,if the method is not returned in this
	 *            time,throw TimeoutException
	 * @param transcoder
	 *            The value's transcoder
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public T get(final String key, final long timeout) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().get(key, timeout);
	}

	public T get(final String key) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().get(key);
	}

	/**
	 * Just like get,But it return a GetsResponse,include cas value for cas
	 * update.
	 * 
	 * @param <T>
	 * @param key
	 *            key
	 * @param timeout
	 *            operation timeout
	 * @param transcoder
	 * 
	 * @return GetsResponse
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public GetsResponse<T> gets(final String key) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().gets(key);
	}

	/**
	 * @see #gets(String, long, Transcoder)
	 * @param <T>
	 * @param key
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public GetsResponse<T> gets(final String key, final long timeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().gets(key, timeout);
	}

	/**
	 * @see #get(Collection, long, Transcoder)
	 * @param <T>
	 * @param keyCollections
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public Map<String, T> get(final Collection<String> keyCollections)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().get(keyCollections);
	}

	/**
	 * @see #get(Collection, long, Transcoder)
	 * @param <T>
	 * @param keyCollections
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public Map<String, T> get(final Collection<String> keyCollections,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().get(keyCollections, timeout);
	}

	/**
	 * Bulk gets items
	 * 
	 * @param <T>
	 * @param keyCollections
	 *            key collection
	 * @param opTime
	 *            Operation timeout
	 * @param transcoder
	 *            Value transcoder
	 * @return Exists GetsResponse map
	 * @see net.rubyeye.xmemcached.GetsResponse
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public Map<String, GetsResponse<T>> gets(
			final Collection<String> keyCollections) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().gets(keyCollections);
	}

	/**
	 * @see #gets(Collection, long, Transcoder)
	 * @param <T>
	 * @param keyCollections
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public Map<String, GetsResponse<T>> gets(
			final Collection<String> keyCollections, final long timeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().gets(keyCollections, timeout);
	}

	/**
	 * Store key-value item to memcached
	 * 
	 * @param <T>
	 * @param key
	 *            stored key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 *            stored data
	 * @param transcoder
	 *            transocder
	 * @param timeout
	 *            operation timeout,in milliseconds
	 * @return boolean result
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean set(final String key, final int exp, final Object value)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().set(key, exp, value);
	}

	/**
	 * @see #set(String, int, Object, Transcoder, long)
	 */
	public boolean set(final String key, final int exp, final Object value,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().set(key, exp, value, timeout);
	}

	/**
	 * Store key-value item to memcached,doesn't wait for reply
	 * 
	 * @param <T>
	 * @param key
	 *            stored key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 *            stored data
	 * @param transcoder
	 *            transocder
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void setWithNoReply(final String key, final int exp,
			final Object value) throws InterruptedException, MemcachedException {
		getMemcachedClient().setWithNoReply(key, exp, value);
	}

	/**
	 * Add key-value item to memcached, success only when the key is not exists
	 * in memcached.
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 * @param transcoder
	 * @param timeout
	 * @return boolean result
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean add(final String key, final int exp, final Object value)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().add(key, exp, value);
	}

	/**
	 * @see #add(String, int, Object, Transcoder, long)
	 * @param key
	 * @param exp
	 * @param value
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean add(final String key, final int exp, final Object value,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().add(key, exp, value, timeout);
	}

	/**
	 * Add key-value item to memcached, success only when the key is not exists
	 * in memcached.This method doesn't wait for reply.
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 * @param transcoder
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */

	public void addWithNoReply(final String key, final int exp,
			final Object value) throws InterruptedException, MemcachedException {
		getMemcachedClient().addWithNoReply(key, exp, value);
	}

	/**
	 * Replace the key's data item in memcached,success only when the key's data
	 * item is exists in memcached.This method will wait for reply from server.
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 * @param transcoder
	 * @param timeout
	 * @return boolean result
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean replace(final String key, final int exp, final Object value)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().replace(key, exp, value);
	}

	/**
	 * @see #replace(String, int, Object, Transcoder, long)
	 * @param key
	 * @param exp
	 * @param value
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean replace(final String key, final int exp, final Object value,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().replace(key, exp, value, timeout);
	}

	/**
	 * Replace the key's data item in memcached,success only when the key's data
	 * item is exists in memcached.This method doesn't wait for reply from
	 * server.
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 * @param transcoder
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void replaceWithNoReply(final String key, final int exp,
			final Object value) throws InterruptedException, MemcachedException {
		getMemcachedClient().replaceWithNoReply(key, exp, value);
	}

	/**
	 * @see #append(String, Object, long)
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean append(final String key, final Object value)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().append(key, value);
	}

	/**
	 * Append value to key's data item,this method will wait for reply
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @return boolean result
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean append(final String key, final Object value,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().append(key, value, timeout);
	}

	/**
	 * Append value to key's data item,this method doesn't wait for reply.
	 * 
	 * @param key
	 * @param value
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void appendWithNoReply(final String key, final Object value)
			throws InterruptedException, MemcachedException {
		getMemcachedClient().appendWithNoReply(key, value);
	}

	/**
	 * @see #prepend(String, Object, long)
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean prepend(final String key, final Object value)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().prepend(key, value);
	}

	/**
	 * Prepend value to key's data item in memcached.This method doesn't wait
	 * for reply.
	 * 
	 * @param key
	 * @param value
	 * @return boolean result
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean prepend(final String key, final Object value,
			final long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().prepend(key, value, timeout);
	}

	/**
	 * Prepend value to key's data item in memcached.This method doesn't wait
	 * for reply.
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void prependWithNoReply(final String key, final Object value)
			throws InterruptedException, MemcachedException {
		getMemcachedClient().prependWithNoReply(key, value);
	}

	/**
	 * @see #cas(String, int, Object, Transcoder, long, long)
	 * @param key
	 * @param exp
	 * @param value
	 * @param cas
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, final int exp, final Object value,
			final long cas) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().cas(key, exp, value, cas);
	}

	/**
	 * Cas is a check and set operation which means "store this data but only if
	 * no one else has updated since I last fetched it."
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param value
	 * @param transcoder
	 * @param timeout
	 * @param cas
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, final int exp, final Object value,
			final long timeout, final long cas) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().cas(key, exp, value, timeout, cas);
	}

	/**
	 * cas is a check and set operation which means "store this data but only if
	 * no one else has updated since I last fetched it."
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 *            An expiration time, in seconds. Can be up to 30 days. After 30
	 *            days, is treated as a unix timestamp of an exact date.
	 * @param getsReponse
	 *            gets method's result
	 * @param operation
	 *            CASOperation
	 * @param transcoder
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, final int exp,
			GetsResponse<T> getsReponse, final CASOperation<T> operation)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().cas(key, exp, getsReponse, operation);
	}

	/**
	 * @see #cas(String, int, GetsResponse, CASOperation, Transcoder)
	 * @param <T>
	 * @param key
	 * @param getsResponse
	 * @param operation
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, GetsResponse<T> getsResponse,
			final CASOperation<T> operation) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().cas(key, getsResponse, operation);
	}

	/**
	 * @see #cas(String, int, GetsResponse, CASOperation, Transcoder)
	 * @param <T>
	 * @param key
	 * @param exp
	 * @param operation
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, final int exp,
			final CASOperation<T> operation) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().cas(key, exp, operation);
	}

	/**
	 * @see #cas(String, int, GetsResponse, CASOperation, Transcoder)
	 * @param <T>
	 * @param key
	 * @param operation
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean cas(final String key, final CASOperation<T> operation)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().cas(key, operation);
	}

	/**
	 * 
	 * @param <T>
	 * @param key
	 * @param getsResponse
	 * @param operation
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void casWithNoReply(final String key, GetsResponse<T> getsResponse,
			final CASOperation<T> operation) throws TimeoutException,
			InterruptedException, MemcachedException {
		getMemcachedClient().casWithNoReply(key, getsResponse, operation);
	}

	/**
	 * cas noreply
	 * 
	 * @param <T>
	 * @param key
	 * @param exp
	 * @param getsReponse
	 * @param operation
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void casWithNoReply(final String key, final int exp,
			GetsResponse<T> getsReponse, final CASOperation<T> operation)
			throws TimeoutException, InterruptedException, MemcachedException {
		getMemcachedClient().casWithNoReply(key, exp, getsReponse, operation);
	}

	/**
	 * @see #casWithNoReply(String, int, GetsResponse, CASOperation)
	 * @param <T>
	 * @param key
	 * @param exp
	 * @param operation
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void casWithNoReply(final String key, final int exp,
			final CASOperation<T> operation) throws TimeoutException,
			InterruptedException, MemcachedException {
		getMemcachedClient().casWithNoReply(key, exp, operation);
	}

	/**
	 * @see #casWithNoReply(String, int, GetsResponse, CASOperation)
	 * @param <T>
	 * @param key
	 * @param operation
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void casWithNoReply(final String key, final CASOperation<T> operation)
			throws TimeoutException, InterruptedException, MemcachedException {
		getMemcachedClient().casWithNoReply(key, operation);
	}

	/**
	 * Delete key's date item from memcached
	 * 
	 * @param key
	 * @param opTimeout
	 *            Operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 * @since 1.3.2
	 */
	public boolean delete(final String key, long opTimeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().delete(key, opTimeout);
	}

	/**
	 * Delete key's date item from memcached only if its cas value is the same
	 * as what was read.
	 * 
	 * @param key
	 * @cas cas on delete to make sure the key is deleted only if its value is
	 *      same as what was read.
	 * @param opTimeout
	 *            Operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 * @since 1.3.2
	 */
	public boolean delete(final String key, long cas, long opTimeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().delete(key, cas, opTimeout);
	}

	/**
	 * Set a new expiration time for an existing item
	 * 
	 * @param key
	 *            item's key
	 * @param exp
	 *            New expiration time, in seconds. Can be up to 30 days. After
	 *            30 days, is treated as a unix timestamp of an exact date.
	 * @param opTimeout
	 *            operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean touch(final String key, int exp, long opTimeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().touch(key, exp, opTimeout);
	}

	/**
	 * Set a new expiration time for an existing item,using default opTimeout
	 * second.
	 * 
	 * @param key
	 *            item's key
	 * @param exp
	 *            New expiration time, in seconds. Can be up to 30 days. After
	 *            30 days, is treated as a unix timestamp of an exact date.
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public boolean touch(final String key, int exp) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().touch(key, exp);
	}

	/**
	 * Get item and set a new expiration time for it
	 * 
	 * @param <T>
	 * @param key
	 *            item's key
	 * @param newExp
	 *            New expiration time, in seconds. Can be up to 30 days. After
	 *            30 days, is treated as a unix timestamp of an exact date.
	 * @param opTimeout
	 *            operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public T getAndTouch(final String key, int newExp, long opTimeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().getAndTouch(key, newExp, opTimeout);
	}

	/**
	 * Get item and set a new expiration time for it,using default opTimeout
	 * 
	 * @param <T>
	 * @param key
	 * @param newExp
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public T getAndTouch(final String key, int newExp) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().getAndTouch(key, newExp);
	}

	/**
	 * Get all connected memcached servers's version.
	 * 
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public Map<InetSocketAddress, String> getVersions()
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().getVersions();
	}

	/**
	 * "incr" are used to change data for some item in-place, incrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for incr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @return the new value of the item's data, after the increment operation
	 *         was carried out.
	 * @param key
	 * @param num
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long incr(final String key, final long delta)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().incr(key, delta);
	}

	public long incr(final String key, final long delta, final long initValue)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().incr(key, delta, initValue);
	}

	/**
	 * "incr" are used to change data for some item in-place, incrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for incr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 *            key
	 * @param num
	 *            increment
	 * @param initValue
	 *            initValue if the data is not exists.
	 * @param timeout
	 *            operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long incr(final String key, final long delta, final long initValue,
			long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().incr(key, delta, initValue, timeout);
	}

	/**
	 * "decr" are used to change data for some item in-place, decrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for decr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @return the new value of the item's data, after the decrement operation
	 *         was carried out.
	 * @param key
	 * @param num
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long decr(final String key, final long delta)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().decr(key, delta);
	}

	/**
	 * @see decr
	 * @param key
	 * @param num
	 * @param initValue
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long decr(final String key, final long delta, long initValue)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().decr(key, delta, initValue);
	}

	/**
	 * "decr" are used to change data for some item in-place, decrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for decr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 *            The key
	 * @param num
	 *            The increment
	 * @param initValue
	 *            The initial value if the data is not exists.
	 * @param timeout
	 *            Operation timeout
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public long decr(final String key, final long delta, long initValue,
			long timeout) throws TimeoutException, InterruptedException,
			MemcachedException {
		return getMemcachedClient().decr(key, delta, initValue, timeout);
	}

	/**
	 * Make All connected memcached's data item invalid
	 * 
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void flushAll() throws TimeoutException, InterruptedException,
			MemcachedException {
		getMemcachedClient().flushAll();
	}

	public void flushAllWithNoReply() throws InterruptedException,
			MemcachedException {
		getMemcachedClient().flushAllWithNoReply();
	}

	/**
	 * Make All connected memcached's data item invalid
	 * 
	 * @param timeout
	 *            operation timeout
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void flushAll(long timeout) throws TimeoutException,
			InterruptedException, MemcachedException {
		getMemcachedClient().flushAll(timeout);
	}

	/**
	 * Invalidate all existing items immediately
	 * 
	 * @param address
	 *            Target memcached server
	 * @param timeout
	 *            operation timeout
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void flushAll(InetSocketAddress address) throws MemcachedException,
			InterruptedException, TimeoutException {
		getMemcachedClient().flushAll(address);
	}

	public void flushAllWithNoReply(InetSocketAddress address)
			throws MemcachedException, InterruptedException {
		getMemcachedClient().flushAllWithNoReply(address);
	}

	public void flushAll(InetSocketAddress address, long timeout)
			throws MemcachedException, InterruptedException, TimeoutException {
		getMemcachedClient().flushAll(address, timeout);
	}

	/**
	 * @param address
	 * @param timeout
	 * @return
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public Map<String, String> stats(InetSocketAddress address, long timeout)
			throws MemcachedException, InterruptedException, TimeoutException {
		return getMemcachedClient().stats(address, timeout);
	}

	/**
	 * Get stats from all memcached servers
	 * 
	 * @param timeout
	 * @return server->item->value map
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public Map<InetSocketAddress, Map<String, String>> getStats(long timeout)
			throws MemcachedException, InterruptedException, TimeoutException {
		return getMemcachedClient().getStats(timeout);
	}

	public Map<InetSocketAddress, Map<String, String>> getStats()
			throws MemcachedException, InterruptedException, TimeoutException {
		return getMemcachedClient().getStats();
	}

	/**
	 * Get special item stats. "stats items" for example
	 * 
	 * @param item
	 * @return
	 */
	public Map<InetSocketAddress, Map<String, String>> getStatsByItem(
			String itemName) throws MemcachedException, InterruptedException,
			TimeoutException {
		return getMemcachedClient().getStatsByItem(itemName);
	}

	public void shutdown() throws IOException {
		getMemcachedClient().shutdown();
	}

	public boolean delete(final String key) throws TimeoutException,
			InterruptedException, MemcachedException {
		return getMemcachedClient().delete(key);
	}

	public Map<InetSocketAddress, Map<String, String>> getStatsByItem(
			String itemName, long timeout) throws MemcachedException,
			InterruptedException, TimeoutException {
		return getMemcachedClient().getStatsByItem(itemName, timeout);
	}

	/**
	 * get operation timeout setting
	 * 
	 * @return
	 */
	public long getOpTimeout() {
		return getMemcachedClient().getOpTimeout();
	}

	/**
	 * set operation timeout,default is one second.
	 * 
	 * @param opTimeout
	 */
	public void setOpTimeout(long opTimeout) {
		getMemcachedClient().setOpTimeout(opTimeout);
	}

	public Map<InetSocketAddress, String> getVersions(long timeout)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().getVersions(timeout);
	}

	/**
	 * Returns available memcached servers list.
	 * 
	 * @return A available server collection
	 */
	public Collection<InetSocketAddress> getAvailableServers() {
		return getMemcachedClient().getAvailableServers();
	}

	/**
	 * add a memcached server to MemcachedClient
	 * 
	 * @param server
	 * @param port
	 * @param weight
	 * @throws IOException
	 */
	public void addServer(final String server, final int port, int weight)
			throws IOException {
		getMemcachedClient().addServer(server, port, weight);
	}

	public void addServer(final InetSocketAddress inetSocketAddress, int weight)
			throws IOException {
		getMemcachedClient().addServer(inetSocketAddress, weight);
	}

	/**
	 * Delete key's data item from memcached.This method doesn't wait for reply.
	 * This method does not work on memcached 1.3 or later version.See <a href=
	 * 'http://code.google.com/p/memcached/issues/detail?id=3&q=delete%20noreply
	 * ' > i s s u e 3</a> </br><strong>Note: This method is deprecated,because
	 * memcached 1.4.0 remove the optional argument "time".You can still use
	 * this method on old version,but is not recommended.</strong>
	 * 
	 * @param key
	 * @param time
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void deleteWithNoReply(final String key)
			throws InterruptedException, MemcachedException {
		getMemcachedClient().deleteWithNoReply(key);
	}

	/**
	 * "incr" are used to change data for some item in-place, incrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for incr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 * @param num
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void incrWithNoReply(final String key, final long delta)
			throws InterruptedException, MemcachedException {
		getMemcachedClient().incrWithNoReply(key, delta);
	}

	/**
	 * "decr" are used to change data for some item in-place, decrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for decr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 * @param num
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void decrWithNoReply(final String key, final long delta)
			throws InterruptedException, MemcachedException {
		getMemcachedClient().decrWithNoReply(key, delta);
	}

	/**
	 * Set the verbosity level of the memcached's logging output.This method
	 * will wait for reply.
	 * 
	 * @param address
	 * @param level
	 *            logging level
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void setLoggingLevelVerbosity(InetSocketAddress address, int level)
			throws TimeoutException, InterruptedException, MemcachedException {
		getMemcachedClient().setLoggingLevelVerbosity(address, level);
	}

	/**
	 * Set the verbosity level of the memcached's logging output.This method
	 * doesn't wait for reply from server
	 * 
	 * @param address
	 *            memcached server address
	 * @param level
	 *            logging level
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	public void setLoggingLevelVerbosityWithNoReply(InetSocketAddress address,
			int level) throws InterruptedException, MemcachedException {
		getMemcachedClient()
				.setLoggingLevelVerbosityWithNoReply(address, level);
	}

	/**
	 * Add a memcached client listener
	 * 
	 * @param listener
	 */
	public void addStateListener(MemcachedClientStateListener listener) {
		getMemcachedClient().addStateListener(listener);
	}

	/**
	 * Remove a memcached client listener
	 * 
	 * @param listener
	 */
	public void removeStateListener(MemcachedClientStateListener listener) {
		getMemcachedClient().removeStateListener(listener);
	}

	/**
	 * Get all current state listeners
	 * 
	 * @return
	 */
	public Collection<MemcachedClientStateListener> getStateListeners() {
		return getMemcachedClient().getStateListeners();
	}

	public void flushAllWithNoReply(int exptime) throws InterruptedException,
			MemcachedException {
		getMemcachedClient().flushAllWithNoReply(exptime);
	}

	public void flushAll(int exptime, long timeout) throws TimeoutException,
			InterruptedException, MemcachedException {
		getMemcachedClient().flushAll(exptime, timeout);
	}

	public void flushAllWithNoReply(InetSocketAddress address, int exptime)
			throws MemcachedException, InterruptedException {
		getMemcachedClient().flushAllWithNoReply(address, exptime);
	}

	public void flushAll(InetSocketAddress address, long timeout, int exptime)
			throws MemcachedException, InterruptedException, TimeoutException {
		getMemcachedClient().flushAll(address, timeout, exptime);
	}

	/**
	 * If the memcached dump or network error cause connection closed,xmemcached
	 * would try to heal the connection.The interval between reconnections is 2
	 * seconds by default. You can change that value by this method.
	 * 
	 * @param healConnectionInterval
	 *            MILLISECONDS
	 */
	public void setHealSessionInterval(long healConnectionInterval) {
		getMemcachedClient().setHealSessionInterval(healConnectionInterval);
	}

	/**
	 * If the memcached dump or network error cause connection closed,xmemcached
	 * would try to heal the connection.You can disable this behaviour by using
	 * this method:<br/>
	 * <code> client.setEnableHealSession(false); </code><br/>
	 * The default value is true.
	 * 
	 * @param enableHealSession
	 * @since 1.3.9
	 */
	public void setEnableHealSession(boolean enableHealSession) {
		getMemcachedClient().setEnableHealSession(enableHealSession);
	}

	/**
	 * Return the default heal session interval in milliseconds
	 * 
	 * @return
	 */
	public long getHealSessionInterval() {
		return getMemcachedClient().getHealSessionInterval();
	}

	public Protocol getProtocol() {
		return getMemcachedClient().getProtocol();
	}

	/**
	 * Store all primitive type as string,defualt is false.
	 */
	public void setPrimitiveAsString(boolean primitiveAsString) {
		getMemcachedClient().setPrimitiveAsString(primitiveAsString);
	}

	/**
	 * In a high concurrent enviroment,you may want to pool memcached
	 * clients.But a xmemcached client has to start a reactor thread and some
	 * thread pools,if you create too many clients,the cost is very large.
	 * Xmemcached supports connection pool instreadof client pool.you can create
	 * more connections to one or more memcached servers,and these connections
	 * share the same reactor and thread pools,it will reduce the cost of
	 * system.
	 * 
	 * @param poolSize
	 *            pool size,default is one,every memcached has only one
	 *            connection.
	 */
	public void setConnectionPoolSize(int poolSize) {
		getMemcachedClient().setConnectionPoolSize(poolSize);
	}

	/**
	 * Whether to enable heart beat
	 * 
	 * @param enableHeartBeat
	 *            if true,then enable heartbeat,true by default
	 */
	public void setEnableHeartBeat(boolean enableHeartBeat) {
		getMemcachedClient().setEnableHeartBeat(enableHeartBeat);
	}

	/**
	 * Enables/disables sanitizing keys by URLEncoding.
	 * 
	 * @param sanitizeKey
	 *            if true, then URLEncode all keys
	 */
	public void setSanitizeKeys(boolean sanitizeKey) {
		getMemcachedClient().setSanitizeKeys(sanitizeKey);
	}

	public boolean isSanitizeKeys() {
		return getMemcachedClient().isSanitizeKeys();
	}

	/**
	 * Get counter for key,and if the key's value is not set,then set it with 0.
	 * 
	 * @param key
	 * @return
	 */
	public Counter getCounter(String key) {
		return getMemcachedClient().getCounter(key);
	}

	/**
	 * Get counter for key,and if the key's value is not set,then set it with
	 * initial value.
	 * 
	 * @param key
	 * @param initialValue
	 * @return
	 */
	public Counter getCounter(String key, long initialValue) {
		return getMemcachedClient().getCounter(key, initialValue);
	}

	/**
	 * Configure auth info
	 * 
	 * @param map
	 *            Auth info map,key is memcached server address,and value is the
	 *            auth info for the key.
	 */
	public void setAuthInfoMap(Map<InetSocketAddress, AuthInfo> map) {
		getMemcachedClient().setAuthInfoMap(map);
	}

	/**
	 * return current all auth info
	 * 
	 * @return Auth info map,key is memcached server address,and value is the
	 *         auth info for the key.
	 */
	public Map<InetSocketAddress, AuthInfo> getAuthInfoMap() {
		return getMemcachedClient().getAuthInfoMap();
	}

	/**
	 * "incr" are used to change data for some item in-place, incrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for incr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 * @param delta
	 * @param initValue
	 *            the initial value to be added when value is not found
	 * @param timeout
	 * @param exp
	 *            the initial vlaue expire time, in seconds. Can be up to 30
	 *            days. After 30 days, is treated as a unix timestamp of an
	 *            exact date.
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	long decr(String key, long delta, long initValue, long timeout, int exp)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().decr(key, delta, initValue, timeout);
	}

	/**
	 * "incr" are used to change data for some item in-place, incrementing it.
	 * The data for the item is treated as decimal representation of a 64-bit
	 * unsigned integer. If the current data value does not conform to such a
	 * representation, the commands behave as if the value were 0. Also, the
	 * item must already exist for incr to work; these commands won't pretend
	 * that a non-existent key exists with value 0; instead, it will fail.This
	 * method doesn't wait for reply.
	 * 
	 * @param key
	 *            key
	 * @param delta
	 *            increment delta
	 * @param initValue
	 *            the initial value to be added when value is not found
	 * @param timeout
	 *            operation timeout
	 * @param exp
	 *            the initial vlaue expire time, in seconds. Can be up to 30
	 *            days. After 30 days, is treated as a unix timestamp of an
	 *            exact date.
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	long incr(String key, long delta, long initValue, long timeout, int exp)
			throws TimeoutException, InterruptedException, MemcachedException {
		return getMemcachedClient().incr(key, delta, initValue, timeout, exp);
	}

	/**
	 * Return the cache instance name
	 * 
	 * @return
	 */
	public String getName() {
		return getMemcachedClient().getName();
	}

	/**
	 * Set cache instance name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		getMemcachedClient().setName(name);
	}

	/**
	 * Returns reconnecting task queue,the queue is thread-safe and 'weakly
	 * consistent',but maybe you <strong>should not modify it</strong> at all.
	 * 
	 * @return The reconnecting task queue,if the client has not been
	 *         started,returns null.
	 */
	public Queue<ReconnectRequest> getReconnectRequestQueue() {
		return getMemcachedClient().getReconnectRequestQueue();
	}

	/**
	 * Configure wheather to set client in failure mode.If set it to true,that
	 * means you want to configure client in failure mode. Failure mode is that
	 * when a memcached server is down,it would not taken from the server list
	 * but marked as unavailable,and then further requests to this server will
	 * be transformed to standby node if configured or throw an exception until
	 * it comes back up.
	 * 
	 * @param failureMode
	 *            true is to configure client in failure mode.
	 */
	public void setFailureMode(boolean failureMode) {
		getMemcachedClient().setFailureMode(failureMode);
	}

	/**
	 * Returns if client is in failure mode.
	 * 
	 * @return
	 */
	public boolean isFailureMode() {
		return getMemcachedClient().isFailureMode();
	}

	/**
	 * Set a key provider for pre-processing keys before sending them to
	 * memcached.
	 * 
	 * @since 1.3.8
	 * @param keyProvider
	 */
	public void setKeyProvider(KeyProvider keyProvider) {
		getMemcachedClient().setKeyProvider(keyProvider);
	}

	/**
	 * Returns maximum number of timeout exception for closing connection.
	 * 
	 * @return
	 */
	public int getTimeoutExceptionThreshold() {
		return getMemcachedClient().getTimeoutExceptionThreshold();
	}

	/**
	 * Set maximum number of timeout exception for closing connection.You can
	 * set it to be a large value to disable this feature.
	 * 
	 * @see #DEFAULT_MAX_TIMEOUTEXCEPTION_THRESHOLD
	 * @param timeoutExceptionThreshold
	 */
	public void setTimeoutExceptionThreshold(int timeoutExceptionThreshold) {
		getMemcachedClient().setTimeoutExceptionThreshold(
				timeoutExceptionThreshold);
	}

	/**
	 * Invalidate all namespace under the namespace using the default operation
	 * timeout.
	 * 
	 * @since 1.4.2
	 * @param ns
	 *            the namespace
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public void invalidateNamespace(String ns) throws MemcachedException,
			InterruptedException, TimeoutException {
		getMemcachedClient().invalidateNamespace(ns);
	}

	/**
	 * Invalidate all items under the namespace.
	 * 
	 * @since 1.4.2
	 * @param ns
	 *            the namespace
	 * @param opTimeout
	 *            operation timeout in milliseconds.
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public void invalidateNamespace(String ns, long opTimeout)
			throws MemcachedException, InterruptedException, TimeoutException {
		getMemcachedClient().invalidateNamespace(ns, opTimeout);
	}

	/**
	 * Remove current namespace set for this memcached client.It must begin with
	 * {@link #beginWithNamespace(String)} method.
	 * 
	 * @see #beginWithNamespace(String)
	 */
	public void endWithNamespace() {
		getMemcachedClient().endWithNamespace();
	}

	/**
	 * set current namespace for following operations with memcached client.It
	 * must be ended with {@link #endWithNamespace()} method.For example:
	 * 
	 * <pre>
	 * memcachedClient.beginWithNamespace(userId);
	 * try {
	 * 	memcachedClient.set(&quot;username&quot;, 0, username);
	 * 	memcachedClient.set(&quot;email&quot;, 0, email);
	 * } finally {
	 * 	memcachedClient.endWithNamespace();
	 * }
	 * </pre>
	 * 
	 * @see #endWithNamespace()
	 * @see #withNamespace(String, MemcachedClientCallable)
	 * @param ns
	 */
	public void beginWithNamespace(String ns) {
		getMemcachedClient().beginWithNamespace(ns);
	}

	/**
	 * With the namespae to do something with current memcached client.All
	 * operations with memcached client done in callable will be under the
	 * namespace. {@link #beginWithNamespace(String)} and
	 * {@link #endWithNamespace()} will called around automatically. For
	 * example:
	 * 
	 * <pre>
	 *   memcachedClient.withNamespace(userId,new MemcachedClientCallable<Void>{
	 *     public Void call(MemcachedClient client) throws MemcachedException,
	 * 			InterruptedException, TimeoutException{
	 *      client.set("username",0,username);
	 *      client.set("email",0,email);
	 *      return null;
	 *     }
	 *   });
	 *   //invalidate all items under the namespace.
	 *   memcachedClient.invalidateNamespace(userId);
	 * </pre>
	 * 
	 * @since 1.4.2
	 * @param ns
	 * @param callable
	 * @see #beginWithNamespace(String)
	 * @see #endWithNamespace()
	 * @return
	 */
	public T withNamespace(String ns, MemcachedClientCallable<T> callable)
			throws MemcachedException, InterruptedException, TimeoutException {
		return getMemcachedClient().withNamespace(ns, callable);
	}
}
