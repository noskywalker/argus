package com.monitor.argus.service.ServiceTest;

import com.monitor.argus.service.system.ISystemUserService;
import com.monitor.argus.service.user.IUserService;
import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.bean.user.WXInfoEntity;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.security.Md5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfeng on 16/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")

public class UserServiceTest {

    @Autowired
    ISystemUserService systemUserService;

    @Autowired
    IUserService userService;

    @Test
    public void getUserBean() {
        UserBean user = new UserBean();
        user.setEmail("ht7833@163.com");
        user.setPassword("AABBDCCD");
        UserBean u = systemUserService.getUserBean(user);
        System.out.println(JsonUtil.beanToJson(u));
    }

    @Test
    public void addUserBean() {
        UserBean u = new UserBean();
        u.setEmail("haha@email.com");
        u.setOperatorId("10");
        u.setPhone("15210906070");
        u.setUserName("HTTTEST");
        systemUserService.addUserBean(u);
    }

    @Test
    public void updateUserBean() {
        UserBean u = new UserBean();
        u.setOperatorId("1");
        u.setPhone("1880000000000");
        u.setUserName("HTT");
        u.setId("2");
        systemUserService.updateUserBean(u);

    }

    @Test
    public void getUserBeanList() {
        UserBean user = new UserBean();
        user.setEnable(0);
        List<UserBean> userList = systemUserService.getUserBeanList(user);
        for (UserBean u : userList) {
            System.out.println(JsonUtil.beanToJson(u) + "\n");
        }
    }

    @Test
    public void editPassword() {
        String oldPass = "123456";
        String newPass = "132456";
        String userId = "1";
        systemUserService.editPassword(oldPass, newPass, userId);
    }

    @Test
    public void getUserAuthList() {
        String userId = "1";
        List<AuthBean> list = systemUserService.getUserAuthList(userId);
        for (AuthBean a : list) {
            System.out.println(JsonUtil.beanToJson(a));
        }
    }

    @Test
    public void editUserAuth() {
        String userId = "2";
        List<Integer> idList = new ArrayList<Integer>();
        idList.add(1);
        idList.add(3);
        idList.add(4);
        systemUserService.editUserAuth(userId, idList);

    }

    @Test
    public void getPassword() {
        String pass = "520";
        String password = Md5Util.getSysUserPasswordMd5(pass);
        System.out.println(password);
    }

    @Test
    public void setAuthUser() {
        String userId = "1";
        Integer authId = 20;
        StringBuffer s = new StringBuffer("");
        for (Integer i = 1; i <= authId; i++) {
            s = s.append("(" + userId + "," + i + "),");
        }
        System.out.println(s);
    }


    @Test
    public void insertWXInfo() {
        WXInfoEntity entity = new WXInfoEntity();
        entity.setOpenId("AAAAAA");
        entity.setEnable(0);
        entity.setHeadImgUrl("headimgurl");
        entity.setNickName("nickname");
        entity.setSence("sence");
        userService.insertWXInfo(entity);
    }

    @Test
    public void getWXInfoList() {
        List<WXInfoEntity> list = userService.getWXInfoList();
        for (WXInfoEntity e : list) {
            System.out.println(JsonUtil.beanToJson(e));
        }
    }

    @Test
    public void registerOpenId() {
        // userService.registerOpenId("92efa43705464a96848f9206fe200fe6", "AAAAA");
    }
}
