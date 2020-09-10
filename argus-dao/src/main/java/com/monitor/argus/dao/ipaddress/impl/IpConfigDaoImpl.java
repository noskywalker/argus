package com.monitor.argus.dao.ipaddress.impl;

import com.monitor.argus.bean.ipaddress.IpConfigEntity;
import com.monitor.argus.dao.ipaddress.IIpConfigDao;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huxiaolei on 2016/11/7.
 */
@Repository("ipConfigDao")
public class IpConfigDaoImpl implements IIpConfigDao {

    @Autowired
    IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(IpConfigDaoImpl.class);

    @Override
    public List<IpConfigEntity> getAllIpConfigList() {
        return baseDao.getList("ipConfigMapper.getAllIpConfigList");
    }

    @Override
    public List<IpConfigEntity> getShortIpConfigList() {
        return baseDao.getList("ipConfigMapper.getShortIpConfigList");
    }

    @Override
    public boolean insertIpConfig(IpConfigEntity ipConfigEntity) {
        logger.info("插入IP地址配置信息,param:{}", JsonUtil.beanToJson(ipConfigEntity));
        return baseDao.insert("ipConfigMapper.insert", ipConfigEntity);
    }

    @Override
    public List<String> getShortAddress() {
        logger.info("获取城市名称LIST");
        return baseDao.getList("ipConfigMapper.getShortAddress");
    }

}
