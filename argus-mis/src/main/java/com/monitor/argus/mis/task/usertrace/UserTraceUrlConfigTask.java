package com.monitor.argus.mis.task.usertrace;

import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.dao.usertrace.IUserTraceDao;
import com.monitor.argus.dao.usertrace.impl.UserTraceDaoImpl;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.mis.task.annotations.Job;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by usr on 2017/4/7.
 */
@Component("UserTraceUrlConfigTask")
public class UserTraceUrlConfigTask {

    private static Logger logger = LoggerFactory.getLogger(UserTraceUrlConfigTask.class);
    private static String lockKey = "UserTraceUrlConfigTaskLock";
    private static String lockValue = "UserTraceUrlConfigTaskValue";

    @Autowired
    IUserTraceDao userTraceDao;

    @Autowired
    RedisService redisService;

    @PostConstruct
    @Job(name = "接口映射关系刷新", cron="0 0 0/1 * * ?")
    public void runUserTraceTask() {
        logger.info("接口映射关系刷新-all====start:{}", DateUtil.getDateLongTimePlusStr(new Date()));
        try {
            List<UserTraceConfigEntity> userTraceConfigs = null;
            if (redisService.setNX(lockKey, lockValue)) {
                userTraceConfigs = userTraceDao.getAllUsertraceConfig();
                if (CollectionUtils.isNotEmpty(userTraceConfigs)) {
                    UserTraceDaoImpl.urlMapping.clear();
                    for (UserTraceConfigEntity userTraceConfigEntity : userTraceConfigs) {
                        UserTraceDaoImpl.urlMapping.put(userTraceConfigEntity.getNodeUrl(), userTraceConfigEntity.getNodeName());
                    }
                }
            }
            logger.info("接口映射关系刷新-all====end:{},list:{},map:{}",
                    DateUtil.getDateLongTimePlusStr(new Date()),
                    userTraceConfigs != null ? userTraceConfigs.size() : "null",
                    UserTraceDaoImpl.urlMapping != null ? UserTraceDaoImpl.urlMapping.size() : "null");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(lockKey, 60);
        }
    }

}
