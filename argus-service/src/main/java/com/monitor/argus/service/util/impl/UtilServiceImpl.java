package com.monitor.argus.service.util.impl;

import com.monitor.argus.service.util.IUtilService;
import com.monitor.argus.dao.util.IUtilDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuefei on 7/7/16.
 */
@Service("utilService")
public class UtilServiceImpl implements IUtilService {

    @Autowired
    IUtilDao utilDao;

    @Override
    public int truncateSingleTable(String tableName) {
        return utilDao.truncateSingleTable(tableName);
    }
}
