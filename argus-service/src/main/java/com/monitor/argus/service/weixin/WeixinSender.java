package com.monitor.argus.service.weixin;

import com.google.common.collect.Maps;
import com.monitor.argus.bean.WeixinEntity;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.HttpClientUtilYMall;
import com.monitor.argus.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usr on 2016/11/22.
 */
@Service
public class WeixinSender {

    private Logger logger = LoggerFactory.getLogger(WeixinSender.class);

    public void sendAsyc(final WeixinEntity weixinEntity, final String wxAlarmUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendWeixinMessage(weixinEntity, wxAlarmUrl);
            }
        }).start();
    }

    /**
     * 调用微信服务平台发送邮件
     *
     * @param weixinEntity
     */
    private boolean sendWeixinMessage(WeixinEntity weixinEntity, String wxAlarmUrl) {
        boolean flag = false;
        Map<String, String> param = Maps.newHashMap();
        param.put("ip", weixinEntity.getIp());
        param.put("logId", weixinEntity.getLogId());
        param.put("message", weixinEntity.getMessage());
        param.put("monitorName", weixinEntity.getMonitorName());
        param.put("receivers", weixinEntity.getReceivers());
        param.put("systemName", weixinEntity.getSystemName());
        param.put("time", weixinEntity.getTime());
        param.put("title", weixinEntity.getTitle());
        param.put("createTime", DateUtil.getDateLongTimePlusStr(weixinEntity.getCreateTime()));
        param.put("level", weixinEntity.getLevel().toString());
        param.put("totalCount", weixinEntity.getTotalCount().toString());
        param.put("alarmId", weixinEntity.getAlarmId());
        try {
            logger.info("发送微信。对象:(" + JsonUtil.beanToJson(weixinEntity) + ")");
            String result = HttpClientUtilYMall.doPost(wxAlarmUrl, param);
            logger.info("接收(" + wxAlarmUrl + ")的返回信息：" + (result == null ? "" : result));
            Map<String, Object> resultBean = (Map<String, Object>) JsonUtil.jsonToBean(result, HashMap.class);
            if (resultBean.get("result").equals("1")) {
                flag = true;
            }
        } catch (Exception e) {
            logger.error("发送微信出错。对象:(" + JsonUtil.beanToJson(weixinEntity) + ")" + e);

        }
        return flag;
    }
}
