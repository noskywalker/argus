package com.monitor.argus.dao.usertrace.impl;

import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.bean.usertrace.UserTraceGroupEntity;
import com.monitor.argus.dao.usertrace.IUserTraceDao;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by usr on 2017/4/6.
 */

@Repository("userTraceDao")
public class UserTraceDaoImpl implements IUserTraceDao {

    public static volatile ConcurrentHashMap<String, String> urlMapping = new  ConcurrentHashMap<String, String>();
    public static volatile Set<String> noUrl = new HashSet<>();

    @Autowired
    private IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(UserTraceDaoImpl.class);

    @Override
    public int addUserTraces(List<UserTraceEntity> userTraceEntitys) {
        return baseDao.insertBatch("userTraceMapper.insertBatch", userTraceEntitys);
    }

    @Override
    public List<UserTraceEntity> getAllUsertrace() {
        return baseDao.getList("userTraceMapper.getAllUsertrace");
    }

    @Override
    public int deleteUserTraceData(String date) {
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("userTraceMapper.deleteUserTraceData", map);
        } else {
            return -1;
        }
    }

    @Override
    public List<UserTraceGroupEntity> usertraceHzByDay(String searchTime) {
        if (!StringUtil.isEmpty(searchTime)) {
            return baseDao.getList("userTraceMapper.usertraceHzByDay", searchTime);
        } else {
            return null;
        }
    }

    @Override
    public List<UserTraceConfigEntity> getAllUsertraceConfig() {
        return baseDao.getList("userTraceConfigMapper.getAllUsertraceConfig");
    }

    @Override
    public boolean deleteAllUsertrace() {
        return baseDao.delete("userTraceMapper.deleteAllUsertrace", null);
    }

    @Override
    public boolean deleteUserTraceConfig() {
        return baseDao.delete("userTraceConfigMapper.clearUserTraceUrlConfig", null);
    }

    @Override
    public boolean batchInsertUserTraceConfig(List<UserTraceConfigEntity> userTraceConfigEntities) {
        logger.info("DAO 批量插入Url配置。size={}", userTraceConfigEntities.size());
        Map<String, List<UserTraceConfigEntity>> map = new HashMap<>();
        map.put("list", userTraceConfigEntities);
        return baseDao.insert("userTraceConfigMapper.insertUserTraceConfigBatch", map);
    }



}
