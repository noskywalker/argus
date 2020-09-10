package com.monitor.argus.dao.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description:Redis key BaseDao接口类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午10:31:56
 * @Version: V1.0
 */
public interface IRedisKeyBaseDao {

    /**
     * Determine if given {@code key} exists.
     * <p>
     * See http://redis.io/commands/exists
     * 
     * @param key
     * @return
     */
    public Boolean exists(String key);

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    public void del(String key);

    /**
     * Delete given {@code keys}.
     * <p>
     * See http://redis.io/commands/del
     * 
     * @param keys
     * @return The number of keys that were removed.
     */
    public void del(List<String> keys);

    /**
     * Delete given {@code keys}.
     * <p>
     * See http://redis.io/commands/del
     * 
     * @param keys
     * @return The number of keys that were removed.
     */
    public Long del(String... keys);

    /**
     * Determine the type stored at {@code key}.
     * <p>
     * See http://redis.io/commands/type
     * 
     * @param key
     * @return
     */
    public DataType type(String key);

    /**
     * Find all keys matching the given {@code pattern}.
     * <p>
     * See http://redis.io/commands/keys
     * 
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern);

    /**
     * Use a {@link Cursor} to iterate over keys.
     * <p>
     * See http://redis.io/commands/scan
     * 
     * @param options
     * @return
     * @since 1.4
     */
    public Cursor<String> scan(ScanOptions options);

    /**
     * Return a random key from the keyspace.
     * <p>
     * See http://redis.io/commands/randomkey
     * 
     * @return
     */
    public String randomKey();

    /**
     * Rename key {@code oleName} to {@code newName}.
     * <p>
     * See http://redis.io/commands/rename
     * 
     * @param oldName
     * @param newName
     */
    public void rename(String oldName, String newName);

    /**
     * Rename key {@code oleName} to {@code newName} only if {@code newName}
     * does not exist.
     * <p>
     * See http://redis.io/commands/renamenx
     * 
     * @param oldName
     * @param newName
     * @return
     */
    public Boolean renameNX(String oldName, String newName);

    /**
     * Set time to live for given {@code key} in seconds.
     * <p>
     * See http://redis.io/commands/expire
     * 
     * @param key
     * @param seconds
     * @return
     */
    public Boolean expire(String key, int seconds);

    /**
     * Set time to live for given {@code key} in milliseconds.
     * <p>
     * See http://redis.io/commands/pexpire
     * 
     * @param key
     * @param millis
     * @return
     */
    public Boolean pExpire(String key, long millis);

    /**
     * Set the expiration for given {@code key} as a {@literal UNIX} timestamp.
     * <p>
     * See http://redis.io/commands/expireat
     * 
     * @param key
     * @param unixTime
     * @return
     */
    public Boolean expireAt(String key, Date date);

    /**
     * Set the expiration for given {@code key} as a {@literal UNIX} timestamp
     * in milliseconds.
     * <p>
     * See http://redis.io/commands/pexpireat
     * 
     * @param key
     * @param unixTimeInMillis
     * @return
     */
    public Boolean pExpireAt(String key, long unixTimeInMillis);

    /**
     * Remove the expiration from given {@code key}.
     * <p>
     * See http://redis.io/commands/persist
     * 
     * @param key
     * @return
     */
    public Boolean persist(String key);

    /**
     * Move given {@code key} to database with {@code index}.
     * <p>
     * See http://redis.io/commands/move
     * 
     * @param key
     * @param dbIndex
     * @return
     */
    public Boolean move(String key, int dbIndex);

    /**
     * Get the time to live for {@code key} in seconds.
     * <p>
     * See http://redis.io/commands/ttl
     * 
     * @param key
     * @return
     */
    public Long ttl(String key);

    /**
     * Get the time to live for {@code key} in milliseconds.
     * <p>
     * See http://redis.io/commands/pttl
     * 
     * @param key
     * @return
     */
    public Long pTtl(String key);

    /**
     * Sort the elements for {@code key}.
     * <p>
     * See http://redis.io/commands/sort
     * 
     * @param key
     * @param params
     * @return
     */
    public List<String> sort(String key, SortParameters params);

    /**
     * Sort the elements for {@code key} and store result in {@code storeKey}.
     * <p>
     * See http://redis.io/commands/sort
     * 
     * @param key
     * @param params
     * @param storeKey
     * @return
     */
    public Long sort(String key, SortParameters params, String storeKey);

    /**
     * Retrieve serialized version of the value stored at {@code key}.
     * <p>
     * See http://redis.io/commands/dump
     * 
     * @param key
     * @return
     */
    public String dump(String key);

    /**
     * Create {@code key} using the {@code serializedValue}, previously obtained
     * using {@link #dump(String)}.
     * <p>
     * See http://redis.io/commands/restore
     * 
     * @param key
     * @param ttlInMillis
     * @param serializedValue
     */
    public void restore(String key, long ttlInMillis, String serializedValue);
}
