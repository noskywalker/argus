package com.monitor.argus.dao.util.impl;

import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.util.IUtilDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Repository("UtilDao")
public class UtilDaoImpl implements IUtilDao {

    @Autowired
    private IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(UtilDaoImpl.class);

    @Override
    public int truncateSingleTable(String tableName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        return baseDao.delete("utilMapper.deleteSingleTable", params);
    }
}
