package com.monitor.argus.dao.mybatis.impl;

import com.monitor.argus.dao.mybatis.IBaseDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author CuiErwei monitor
 * @Description BaseDao接口实现类
 * @date 2013-12-20
 */
@Repository("baseDao")
public class BaseDao extends SqlSessionDaoSupport implements IBaseDao {

    private final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public Boolean insert(String key, Object object) {
        try {
            int i = getSqlSession().insert(key, object);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.debug("保存数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }


    public <T> int insertBatch(String key, List<T> list) {
        int result = 0;
        try {
            for (int i = 0; i < list.size(); i++) {
                Object o = getSqlSession().insert(key, list.get(i));
                if (o != null) {
                    result++;
                }
            }
        } catch (Exception e) {
            logger.debug("批量保存数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public Boolean update(String key, Object object) {
        try {
            int i = getSqlSession().update(key, object);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.debug("更新数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    public int updateBatch(String key, Object object) {
        int result = 0;
        try {
            result = getSqlSession().update(key, object);
            return result;
        } catch (Exception e) {
            logger.debug("更新数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> int updateBatch(String key, List<T> list) {
        int result = 0;
        try {
            for (int i = 0; i < list.size(); i++) {
                int k = getSqlSession().update(key, list.get(i));
                result += k;
            }
        } catch (Exception e) {
            logger.error("批量更新数据失败{}异常{}", key, e.getMessage());
            logger.debug("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public Boolean delete(String key, Serializable id) {
        try {
            int i = getSqlSession().delete(key, id);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.debug("根据主键ID删除数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    public int delete(String key, Object object) {
        int result = 0;
        try {
            result = getSqlSession().delete(key, object);
        } catch (Exception e) {
            logger.debug("删除数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public <T> int deleteBatch(String key, List<T> list) {
        int result = 0;
        try {
            for (int i = 0; i < list.size(); i++) {
                int k = getSqlSession().delete(key, list.get(i));
                result += k;
            }
        } catch (Exception e) {
            logger.debug("批量删除数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public <T> T get(String key) {
        try {
            return getSqlSession().selectOne(key);
        } catch (Exception e) {
            logger.debug("查询数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T get(String key, Object params) {
        try {
            return getSqlSession().selectOne(key, params);
        } catch (Exception e) {
            logger.debug("查询数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> List<T> getList(String key) {
        try {
            return getSqlSession().selectList(key);
        } catch (Exception e) {
            logger.debug("查询数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> List<T> getList(String key, Object params) {
        try {
            return getSqlSession().selectList(key, params);
        } catch (Exception e) {
            logger.debug("查询数据失败{}异常{}", key, e.getMessage());
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
