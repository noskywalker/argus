package com.monitor.argus.dao.redis;

import org.springframework.data.redis.connection.RedisListCommands.Position;

import java.util.List;

/**
 * @Description:Redis List BaseDao接口类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-28 上午11:03:17
 * @Version: V1.0
 * 
 */
public interface IRedisListBaseDao {

    /**
     * @Description: 追加（尾部） {@code values} 到 {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:54
     * @Version: V1.0
     * 
     * @param key
     * @param values
     * @return
     * 
     */
    public <Bean> Long rPush(String key, List<Bean> values);

    /**
     * @Description:添加（头部） {@code values} 到 {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:12:53
     * @Version: V1.0
     * 
     * @param key
     * @param values
     * @return
     * 
     */
    public <T> Long lPush(String key, List<T> values);

    /**
     * @Description:追加（尾部） {@code} values 到 {@code key} 当且仅当该list存在.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:13:03
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    public Long rPushX(String key, String value);

    /**
     * @Description:添加（头部） {@code values} 到 {@code key} 当且仅当该list存在.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:13:50
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    public Long lPushX(String key, String value);

    /**
     * @Description:根据key，获取list的长度
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:15:20
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    public Long lLen(String key);

    /**
     * @Description:Get elements between {@code begin} and {@code end} from list
     *                  at {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:15:44
     * @Version: V1.0
     * 
     * @param key
     * @param begin
     * @param end
     * @return
     * 
     */
    public List<String> lRange(String key, long begin, long end);

    /**
     * @Description:Trim list at {@code key} 到 elements between {@code begin}
     *                   and {@code end}
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:16:02
     * @Version: V1.0
     * 
     * @param key
     * @param begin
     * @param end
     * 
     */
    public void lTrim(String key, long begin, long end);

    /**
     * @Description:Get element at {@code index} form list at {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:16:14
     * @Version: V1.0
     * 
     * @param key
     * @param index
     * @return
     * 
     */
    public String lIndex(String key, long index);

    /**
     * @Description:Insert {@code value} {@link Position#BEFORE} or
     *                     {@link Position#AFTER} existing {@code pivot} for
     *                     {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:16:28
     * @Version: V1.0
     * 
     * @param key
     * @param where
     * @param pivot
     * @param value
     * @return
     * 
     */
    public Long lInsert(String key, Position where, String pivot, String value);

    /**
     * @Description:Set the {@code value} list element at {@code index}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:16:46
     * @Version: V1.0
     * 
     * @param key
     * @param index
     * @param value
     * 
     */
    public void lSet(String key, long index, String value);

    /**
     * @Description:Removes the first {@code count} occurrences of {@code value}
     *                      from the list stored at {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:17:05
     * @Version: V1.0
     * 
     * @param key
     * @param count
     * @param value
     * @return
     * 
     */
    public Long lRem(String key, long count, String value);

    /**
     * @Description:Removes and returns first element in list stored at
     *                      {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:17:17
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    public String lPop(String key);

    /**
     * @Description: Removes and returns last element in list stored at
     *               {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:17:33
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    public String rPop(String key);

    /**
     * @Description: Removes and returns first element from lists stored at
     *               {@code keys} (see: {@link #lPop(String)}). <br>
     *               <b>Blocks connection</b> until element available or
     *               {@code timeout} reached.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:17:41
     * @Version: V1.0
     * 
     * @param timeout
     * @param keys
     * @return
     * 
     */
    public List<String> bLPop(int timeout, List<String> keys);

    /**
     * @Description:Removes and returns last element from lists stored at
     *                      {@code keys} (see: {@link #rPop(String)}). <br>
     *                      <b>Blocks connection</b> until element available or
     *                      {@code timeout} reached.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:18:25
     * @Version: V1.0
     * 
     * @param timeout
     * @param keys
     * @return
     * 
     */
    public List<String> bRPop(int timeout, List<String> keys);

    /**
     * @Description:Remove the last element from list at {@code srcKey}, 追加（尾部）
     *                     it 到 {@code dstKey} and return its value.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:18:15
     * @Version: V1.0
     * 
     * @param srcKey
     * @param dstKey
     * @return
     * 
     */
    public String rPopLPush(String srcKey, String dstKey);

    /**
     * @Description:Remove the last element from list at {@code srcKey}, 追加（尾部）
     *                     it 到 {@code dstKey} and return its value (see
     *                     {@link #rPopLPush(String, String)}). <br>
     *                     <b>Blocks connection</b> until element available or
     *                     {@code timeout} reached.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:18:07
     * @Version: V1.0
     * 
     * @param timeout
     * @param srcKey
     * @param dstKey
     * @return
     * 
     */
    public String bRPopLPush(int timeout, String srcKey, String dstKey);

    /**
     * 插入LIST 有失败时间
     * 
     * @param key
     * @param values
     * @param seconds
     * @return
     */
    public <T> Long rPushEx(final String key, List<T> values, int seconds);

    public <T> List<T> getList(Class<T> clazz, String key);

}
