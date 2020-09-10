package com.monitor.argus.mis.task.usertrace;

import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.dao.usertrace.IUserTraceDao;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.mis.task.annotations.Job;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by hxl on 2017/4/6.
 */
@Component("UserTraceTask")
public class UserTraceTask {

    private static Logger logger = LoggerFactory.getLogger(UserTraceTask.class);
    private static String lockKey = "UserTraceTaskLock";
    private static String lockValue = "UserTraceTaskValue";
    private String FORMAT_DATE = "yyyyMM";
    private String collectionNamePrefix = "ying_access_log_%s";
    private int addHours = -48;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    RedisService redisService;

    @Autowired
    IUserTraceDao userTraceDao;

    @Job(name = "用户轨迹汇总计算", cron="0 0 0/2 * * ?")
    public void runUserTraceTask() {
        logger.info("用户轨迹汇总计算-all====start:{}", DateUtil.getDateLongTimePlusStr(new Date()));

        try {
            if (redisService.setNX(lockKey, lockValue)) {
                // 查询数据
                Date newDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newDate);
                calendar.add(Calendar.HOUR_OF_DAY, -2);
                List<ProcessLandLog> plls = getProcessLandLogs(DateUtil.getDateLongTimePlusStr(calendar.getTime()));

                // 汇总用户数据
                HashMap<String, String> userData = statisticsUser(plls);

                // 汇总轨迹数据
                HashMap<String, Integer> userTraceData = statisticsUserTrace(userData);

                // 过滤轨迹数据
                HashMap<String, Integer> userTraceDataFilte = filterUserTraceData(userTraceData);

                // 持久化
                persistUserTraceData(userTraceDataFilte);

                // 保留最近48小时数据
                deleteUserTraceData();

            }
            logger.info("用户轨迹汇总计算-all====end:{}", DateUtil.getDateLongTimePlusStr(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(lockKey, 300);
        }
    }

    /**
     * 查询数据
     * @param time
     * @return
     */
    private List<ProcessLandLog> getProcessLandLogs(String time) {
        logger.info("用户轨迹汇总计算-step1====getProcessLandLogs:start:{},time:{}", DateUtil.getDateLongTimePlusStr(new Date()), time);
        List<ProcessLandLog> plls = null;
        String newCollectionName = "";
        Query query = new Query();
        if (!StringUtil.isEmpty(time)) {
            query.addCriteria(Criteria.where("createTime").gte(time));
            // query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "createTime")));
            newCollectionName = getCollectionName();
            // newCollectionName = "ying_access_log";
            plls = mongoTemplate.find(query, ProcessLandLog.class, newCollectionName);
        }
        logger.info("用户轨迹汇总计算-step1====getProcessLandLogs:end:{},time:{},list:{},collectionName:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), time, plls != null ? plls.size() : "null", newCollectionName);
        return plls;
    }

    /**
     * 汇总用户数据
     * @param plls
     * @return
     */
    private HashMap<String, String> statisticsUser(List<ProcessLandLog> plls) {
        logger.info("用户轨迹汇总计算-step2====statisticsUser:start:{},日志列表:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), plls != null ? plls.size() : "null");
        HashMap<String, String> userData = new HashMap();
        if (CollectionUtils.isNotEmpty(plls)) {
            for (ProcessLandLog pll : plls) {
                if (pll != null && !StringUtil.isEmpty(pll.getAction()) && !StringUtil.isEmpty(pll.getToken()) ) {
                    String trace = userData.get(pll.getToken());
                    if (trace != null) {
                        trace += pll.getAction() + ",";
                    } else {
                        trace = pll.getAction() + ",";
                    }
                    userData.put(pll.getToken(), trace);
                }
            }
        }
        logger.info("用户轨迹汇总计算-step2====statisticsUser:end:{},userData:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userData != null ? userData.size() : "null");
        return userData;
    }

    /**
     * 汇总轨迹数据
     * @param userData
     * @return
     */
    private HashMap<String, Integer> statisticsUserTrace(HashMap<String, String> userData) {
        logger.info("用户轨迹汇总计算-step3====statisticsUserTrace:start:{},userData:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userData != null ? userData.size() : "null");
        HashMap<String, Integer> userTraceData = new HashMap<>();
        if (MapUtils.isNotEmpty(userData)) {
            Collection<String> userTraceCol = userData.values();
            if (CollectionUtils.isNotEmpty(userTraceCol)) {
                Iterator<String> iterator = userTraceCol.iterator();
                while (iterator.hasNext()) {
                    String trace = iterator.next();
                    Integer count = userTraceData.get(trace);
                    if (count == null) {
                        count = 1;
                    } else {
                        count = count + 1;
                    }
                    userTraceData.put(trace, count);
                }
            }
        }
        logger.info("用户轨迹汇总计算-step3====statisticsUserTrace:end:{},userTraceData:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userTraceData != null ? userTraceData.size() : "null");
        return userTraceData;
    }

    private HashMap<String, Integer> filterUserTraceData(HashMap<String, Integer> userTraceData) {
        logger.info("用户轨迹汇总计算-step4====filterUserTraceData:start:{},userTraceData:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userTraceData != null ? userTraceData.size() : "null");
        HashMap<String, Integer> userTraceDataFilte = new HashMap<>();
        int countOne = 0;
        if (MapUtils.isNotEmpty(userTraceData)) {
            Iterator iter = userTraceData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = (Map.Entry) iter.next();
                String traces = entry.getKey();
                Integer count = entry.getValue();
                if (!StringUtil.isEmpty(traces) && count != null) {
                    if (count.intValue() == 1) {
                        countOne ++;
                        if (countOne > 100) {
                            continue;
                        }
                    }
                    String[] urls = StringUtil.split(traces, ",");
                    if (urls != null && urls.length > 4) {
                        Set<String> urlSet = new HashSet<>();
                        for (String tractUrl : urls) {
                            if (!StringUtil.isEmpty(tractUrl)) {
                                urlSet.add(tractUrl);
                            }
                        }
                        if (urlSet.size() > 1) {
                            userTraceDataFilte.put(traces, count);
                        }
                    }
                }
            }
        }
        logger.info("用户轨迹汇总计算-step4====filterUserTraceData:end:{},countOne:{},userTraceDataFilte:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), countOne, userTraceDataFilte != null ? userTraceDataFilte.size() : "null");
        return userTraceDataFilte;
    }

    private void persistUserTraceData(HashMap<String, Integer> userTraceData) {
        logger.info("用户轨迹汇总计算-step5====persistUserTraceData:start:{},userTraceData:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userTraceData != null ? userTraceData.size() : "null");
        List<UserTraceEntity> userTraceEntitys = new ArrayList<>();
        if (MapUtils.isNotEmpty(userTraceData)) {
            // 入库
            Iterator iter = userTraceData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = (Map.Entry) iter.next();
                UserTraceEntity userTraceEntity = new UserTraceEntity();
                userTraceEntity.setUserTrace(entry.getKey());
                userTraceEntity.setCount(entry.getValue());
                userTraceEntitys.add(userTraceEntity);
            }
            if (CollectionUtils.isNotEmpty(userTraceEntitys)) {
                userTraceDao.addUserTraces(userTraceEntitys);
            }
        }
        logger.info("用户轨迹汇总计算-step5====persistUserTraceData:end:{},userTraceEntitys:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), userTraceEntitys != null ? userTraceEntitys.size() : "null");
    }

    private String getCollectionName() {
        Date now = new Date();
        String collectionNameSuffix = DateUtil.formatDate2String(now, FORMAT_DATE);
        String newCollectionName = String.format(collectionNamePrefix, collectionNameSuffix);
        return newCollectionName;
    }

    private void deleteUserTraceData() {
        Date nowDateM = new Date();
        Date nowDateMBefor = DateUtils.addHours(nowDateM, addHours);
        String nowDateMBeforSdf = DateUtil.getDateLongTimePlusStr(nowDateMBefor);
        logger.info("用户轨迹汇总计算-step6====deleteUserTraceData:start:{},time:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), nowDateMBeforSdf);
        int count = 0;
        if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
            count = userTraceDao.deleteUserTraceData(nowDateMBeforSdf);
        }
        logger.info("用户轨迹汇总计算-step6====deleteUserTraceData:end:{},count:{}",
                DateUtil.getDateLongTimePlusStr(new Date()), count);
    }

}
