package com.monitor.argus.mis.init;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.service.system.ISystemFuncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxiaolei on 2016/8/24.
 */
@Component("AuthCacheService")
public class AuthCacheService {

    @Autowired
    private ISystemFuncService iSystemFuncService;

    private final Logger logger = LoggerFactory.getLogger(AuthCacheService.class);

    // 所有被控制在权限范围内的地址
    public static List<FuncBean> allURI = new ArrayList<FuncBean>();

    // 日志监控排除列表
    public static List<String> notURI = new ArrayList<String>();

    static {
        notURI.add("/statistics/totalBytes");
        notURI.add("/dashboard/index");
        notURI.add("/statistics/realtimeTraffic");
        notURI.add("/statisticsmonitor/systemflow");
        notURI.add("/statisticsmonitor/bytesBykey");
        notURI.add("/statisticsmonitor/dayinfo");
        notURI.add("/statisticsmonitor/monitortimes");
        notURI.add("/node/sumnode");
    }

    @PostConstruct
    public void initAllURI() throws InterruptedException {
        logger.info("获取所有权限列表开始===={}", System.currentTimeMillis());
        // 获取所有功能列表
        FuncBean funcBean = new FuncBean();
        List<FuncBean> allFuncs = iSystemFuncService.getFuncBeanList(funcBean);
        if (allFuncs != null && allFuncs.size() > 0) {
            logger.info("获取所有权限列表为===={}", allFuncs.size());
            allURI.clear();
            for (FuncBean funcb : allFuncs) {
                allURI.add(funcb);
            }
        }
        logger.info("获取所有权限列表结束===={}", System.currentTimeMillis());
    }

    public List<FuncBean> getURIByUser(UserBean resultUserbean) {
        List<FuncBean> userURI = new ArrayList<FuncBean>();
        if (resultUserbean != null) {
            List<AuthBean> authList = resultUserbean.getAuthBeanList();
            if (authList != null && authList.size() > 0) {
                for (AuthBean authBean : authList) {
                    if (authBean != null) {
                        FuncBean funcb = new FuncBean();
                        funcb.setAuthId(authBean.getId());
                        List<FuncBean> userURIByAuth = iSystemFuncService.getFuncBeanList(funcb);
                        userURI.addAll(userURIByAuth);
                    }
                }
            }
            logger.info("获取用户:{}，功能列表:{}===={}", resultUserbean.getEmail(),
                    userURI.size(), System.currentTimeMillis());
        }
        return userURI;
    }

}
