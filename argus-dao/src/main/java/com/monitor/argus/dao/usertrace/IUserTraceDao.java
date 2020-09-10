package com.monitor.argus.dao.usertrace;

import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.bean.usertrace.UserTraceGroupEntity;

import java.util.List;

/**
 * Created by usr on 2017/4/6.
 */
public interface IUserTraceDao {

    int addUserTraces(List<UserTraceEntity> userTraceEntitys);

    List<UserTraceEntity> getAllUsertrace();

    int deleteUserTraceData(String date);

    List<UserTraceGroupEntity> usertraceHzByDay(String searchTime);

    List<UserTraceConfigEntity> getAllUsertraceConfig();

    boolean deleteAllUsertrace();

    boolean deleteUserTraceConfig();

    boolean batchInsertUserTraceConfig(List<UserTraceConfigEntity> list);


}
