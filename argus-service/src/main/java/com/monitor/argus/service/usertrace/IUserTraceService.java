package com.monitor.argus.service.usertrace;

import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.bean.usertrace.UserTraceGroupEntity;

import java.util.List;

/**
 * Created by usr on 2017/4/6.
 */
public interface IUserTraceService {

    public List<UserTraceEntity> getAllUsertrace();

    public List<UserTraceGroupEntity> usertraceHzByDay(String searchTime);

    public boolean deleteAllUsertrace();

    public List<UserTraceConfigEntity> getUserTraceConfig();

    public Integer importUserTraceConfig(String filePath);
}
