package com.monitor.argus.service.ServiceTest;

import com.monitor.argus.service.system.ISystemAuthService;
import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wangfeng on 16/8/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class AuthServiceTest {

    @Autowired
    ISystemAuthService systemAuthService;

    @Test
    public void addAuthBean() {
        AuthBean auth = new AuthBean();

        auth.setAuthType(0);
        auth.setParentId(1);
        auth.setAuthName("用户管理二号");
        systemAuthService.addAuthBean(auth);
    }

    @Test
    public void updateAuthBean() {
        AuthBean auth = new AuthBean();
        auth.setOperatorId("11");
        auth.setId(1);
        auth.setAuthType(1);
        auth.setAuthName("用户管理二号de erhao");
        boolean flag = systemAuthService.updateAuthBean(auth);
        System.out.println(flag);
    }

    @Test
    public void getAuthBean() {
        String authId = "6";
        AuthBean auth = systemAuthService.getAuthBean(authId);
        System.out.println(JsonUtil.beanToJson(auth));
    }

    @Test
    public void getAuthBeanList() {
        AuthBean auth = new AuthBean();
        auth.setAuthType(0);
        auth.setParentId(0);
//        List<AuthBean> list = systemAuthService.getAuthBeanList(auth, 0);
//        for (AuthBean a : list) {
//            System.out.println(JsonUtil.beanToJson(a) + "\n");
//        }

    }
}
