package com.monitor.argus.service.usertrace.impl;

import com.monitor.argus.service.usertrace.IUserTraceService;
import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.bean.usertrace.UserTraceGroupEntity;
import com.monitor.argus.common.util.CSVUtils;
import com.monitor.argus.dao.usertrace.IUserTraceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/4/6.
 */
@Service("userTraceService")
public class UserTraceServiceImpl implements IUserTraceService {

    private static Logger logger = LoggerFactory.getLogger(UserTraceServiceImpl.class);

    @Autowired
    IUserTraceDao userTraceDao;

    @Override
    public List<UserTraceEntity> getAllUsertrace() {
        return userTraceDao.getAllUsertrace();
    }

    @Override
    public List<UserTraceGroupEntity> usertraceHzByDay(String searchTime) {
        return userTraceDao.usertraceHzByDay(searchTime);
    }

    @Override
    public boolean deleteAllUsertrace() {
        return userTraceDao.deleteAllUsertrace();
    }

    @Override
    public List<UserTraceConfigEntity> getUserTraceConfig() {
        return userTraceDao.getAllUsertraceConfig();
    }

    @Override
    public Integer importUserTraceConfig(String filePath) {
        List<String> dataList = CSVUtils.importCsv(new File(filePath));
        List<UserTraceConfigEntity> beanList = new ArrayList<>();
        UserTraceConfigEntity bean;
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                bean = new UserTraceConfigEntity();
                String s = dataList.get(i);
                if (null == s || s.length() == 0) {
                    continue;
                }
                String[] str = s.split(",");
                if(str.length<3){
                    continue;
                }
                //获取词汇做存储。
                bean.setId(Integer.parseInt(str[0].trim()));
                bean.setNodeUrl(str[1]);
                bean.setNodeName(str[2]);
                beanList.add(bean);
            }
            //清空表
            userTraceDao.deleteUserTraceConfig();
            userTraceDao.batchInsertUserTraceConfig(beanList);
        }
        return dataList.size();
    }


}
