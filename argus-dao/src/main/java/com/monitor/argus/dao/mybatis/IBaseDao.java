package com.monitor.argus.dao.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * @Description BaseDao接口类
 * @author CuiErwei monitor
 * @date 2013-12-20
 */
public interface IBaseDao {

    /**
     * 插入操作
     * 
     * @param key mybatis执行sql的：命名空间+id
     * @param object 参数对象
     * @return 返回插入的对象 @
     */
    public Boolean insert(String key, Object object);

    /**
     * 批量插入操作
     * 
     * @param key mybatis执行sql的：命名空间+id
     * @param list 参数对象列表
     * @return 返回插入的行数 @
     */
    public <T> int insertBatch(String key, List<T> list);

    /**
     * 更新操作
     * 
     * @param key mybatis执行sql的：命名空间+id
     * @param object 参数对象
     * @return @
     */
    public Boolean update(String key, Object object);

    /**
     * 更新操作 返回条数
     * 
     * @param key mybatis执行sql的：命名空间+id
     * @param object 参数对象
     * @return @
     */
    public int updateBatch(String key, Object object);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param list
     * @return @
     */
    public <T> int updateBatch(String key, List<T> list);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param id
     * @return @
     */
    public Boolean delete(String key, Serializable id);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param object 参数对象
     * @return @
     */
    public int delete(String key, Object object);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param list
     * @return @
     */
    public <T> int deleteBatch(String key, List<T> list);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @return @
     */
    public <T> T get(String key);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param params 参数对象
     * @return @
     */
    public <T> T get(String key, Object params);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @return @
     */
    public <T> List<T> getList(String key);

    /**
     * @param key mybatis执行sql的：命名空间+id
     * @param params 参数对象
     * @return @
     */
    public <T> List<T> getList(String key, Object params);

}
