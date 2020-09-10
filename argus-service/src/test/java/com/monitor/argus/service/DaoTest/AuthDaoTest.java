package com.monitor.argus.service.DaoTest;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemAuthDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by wangfeng on 16/8/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class AuthDaoTest {
    @Autowired
    ISystemAuthDao systemAuthDao;

    @Test
    public void addAuthBean() {
        AuthBean authBean = new AuthBean();
        authBean.setAuthName("测试权限管理");
        authBean.setOperatorId("10");
        authBean.setParentId(1);
        boolean flag = systemAuthDao.addAuthBean(authBean);
        System.out.println(flag);
    }

    @Test
    public void getAuthBean() {

        AuthBean newone = systemAuthDao.getAuthBean("1");
        String d = newone.getCreateTime();
        System.out.println(d);
        System.out.println(JsonUtil.beanToJson(newone));
    }

    @Test
    public void getAuthBeanList() {
        AuthBean authBean = new AuthBean();
        authBean.setAuthType(1);
        List<AuthBean> list = systemAuthDao.getAuthBeanList(authBean);
        for (AuthBean a : list) {
            System.out.println(JsonUtil.beanToJson(a));
        }

    }

    @Test
    public void updateAuthBean() {
        AuthBean authBean = new AuthBean();
        authBean.setAuthName("测试权限管理AAA");
        authBean.setOperatorId("11");
        authBean.setId(7);
        authBean.setParentId(1);
        systemAuthDao.updateAuthBean(authBean);
    }

    @Test
    public void countAuthBeanList() {
        AuthBean authBean = new AuthBean();

        Integer count = systemAuthDao.countAuthBeanList(authBean);
        System.out.println(count);
    }
}
