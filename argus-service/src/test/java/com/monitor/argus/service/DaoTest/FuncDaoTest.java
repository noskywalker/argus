package com.monitor.argus.service.DaoTest;

import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemFuncDao;
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
public class FuncDaoTest {
    @Autowired
    ISystemFuncDao systemFuncDao;

    @Test
    public void addFuncBean() {
        FuncBean funcBean = new FuncBean();
        funcBean.setFuncUri("system/func/add");
        funcBean.setAuthId(3);
        funcBean.setOperatorId("10");
        systemFuncDao.addFuncBean(funcBean);
    }

    @Test
    public void getFuncBean() {
        FuncBean f = systemFuncDao.getFuncBean("1");
        System.out.println(JsonUtil.beanToJson(f));
    }

    @Test
    public void getFuncBeanList() {
        FuncBean funcBean = new FuncBean();
        funcBean.setAuthId(3);
        List<FuncBean> list = systemFuncDao.getFuncBeanList(funcBean);
        for (FuncBean f : list) {
            System.out.println(JsonUtil.beanToJson(f));
        }
    }

    @Test
    public void updateFuncBean() {
        FuncBean funcBean = new FuncBean();
        funcBean.setId(1);
        funcBean.setAuthId(999);
        funcBean.setOperatorId("666");
        systemFuncDao.updateFuncBean(funcBean);
    }

    @Test
    public void countFuncBeanList() {
        FuncBean funcBean = new FuncBean();
        funcBean.setAuthId(3);
        Integer c = systemFuncDao.countFuncBeanList(funcBean);
        System.out.println(c);

    }

}
