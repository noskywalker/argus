package com.monitor.argus.service.console;

import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.console.vo.RuntimeAlarmQueueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16.
 */
@Service
public class RuntimeMonitorInfoService {

    @Autowired
    RedisService redisService;
    public List<RuntimeAlarmQueueVO> getQueueListInfo(){
        List<RuntimeAlarmQueueVO> retList = new ArrayList<>();
        List<String> list = redisService.listAll(RedisKeyUtils.ALARM_QUEUE_NAME);
        for(String queueName:list){
            RuntimeAlarmQueueVO vo = new RuntimeAlarmQueueVO(queueName);
            vo.setQueueSize(queueSize(queueName));
            retList.add(vo);
        }
        return retList;
    }

    public int queueSize(String queueName){
        Long ret=redisService.size(queueName);
        return ret==null?0:ret.intValue();
    }

    public List<String> activeThreadCount(){
        //TODO ready to do
//    List<String> threads=redisService.listAll(ArgusUtils.)
        return null;
    }
}
