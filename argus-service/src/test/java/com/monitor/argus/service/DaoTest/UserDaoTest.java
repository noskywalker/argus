package com.monitor.argus.service.DaoTest;

import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.bean.user.WXInfoEntity;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemUserDao;
import com.monitor.argus.dao.user.IUserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfeng on 16/8/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class UserDaoTest {
    @Autowired
    ISystemUserDao systemUserDao;

    @Autowired
    IUserDao userDao;

    @Test
    public void addUserBean() {
        UserBean userBean = new UserBean();
        userBean.setUserName("HTT");
        userBean.setEmail("ht7833@163.com");
        userBean.setOperatorId("10");
        userBean.setPassword("AABBCCD");
        userBean.setPhone("123546789");
        systemUserDao.addUserBean(userBean);
    }

    @Test
    public void updateUserBean() {
        UserBean userBean = new UserBean();
        userBean.setUserName("HTTAA");
        userBean.setEmail("ht7833@16321.com");
        userBean.setOperatorId("12");
        userBean.setPassword("AABBDCCD");
        userBean.setId("1");
        systemUserDao.updateUserBean(userBean);

    }

    @Test
    public void getUserBeanList() {
        UserBean userBean = new UserBean();
        userBean.setId("1");
        List<UserBean> uu = systemUserDao.getUserBeanList(userBean);
        for (UserBean u : uu) {
            System.out.println(JsonUtil.beanToJson(u));
        }
    }

    @Test
    public void getUserBeanByPK() {
        UserBean u = systemUserDao.getUserBeanByPrimaryKey("1");
        System.out.println(JsonUtil.beanToJson(u));
    }

    @Test
    public void getUserBean() {
        UserBean userBean = new UserBean();
        userBean.setEmail("ht7833@163.com");
        userBean.setPassword("AABBDCCD");
        UserBean uu = systemUserDao.getUserBean(userBean);
        System.out.println(JsonUtil.beanToJson(uu));
    }

    @Test
    public void getUserBeanListCount() {
        UserBean u = new UserBean();
        u.setOperatorId("12");
        Integer c = systemUserDao.getUserBeanListCount(u);
        System.out.println(c);

    }

    @Test
    public void addUserAuth() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        map.put("UserAuthList", list);
        systemUserDao.addUserAuth(map);
    }

    @Test
    public void deleteUserAuth() {
        systemUserDao.deleteUserAuth("1");
    }

    @Test
    public void addWXInfo() {
        WXInfoEntity entity = new WXInfoEntity();
        entity.setOpenId("AAAAA");
        entity.setEnable(0);
        entity.setHeadImgUrl("headimgurl");
        entity.setNickName("nickname");
        entity.setSence("sence");
        userDao.insertWXInfo(entity);
    }

    @Test
    public void getWXInfoList() {
        List<WXInfoEntity> list = userDao.getWXInfoList(null);
        for (WXInfoEntity wx : list) {
            System.out.println(JsonUtil.beanToJson(wx));
        }
    }

    @Test
    public void disableOpenId() {

        // userDao.disableOpenId("AAAAA");
    }

    @Test
    public void getWXInfo() {
        String openId = "AAAAA";
        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        WXInfoEntity en = userDao.getWXInfo(map);
        System.out.println(JsonUtil.beanToJson(en));
    }


}

