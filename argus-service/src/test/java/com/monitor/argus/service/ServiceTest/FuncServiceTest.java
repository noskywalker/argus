package com.monitor.argus.service.ServiceTest;

import com.monitor.argus.service.system.ISystemFuncService;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by wangfeng on 16/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class FuncServiceTest {
    @Autowired
    ISystemFuncService systemFuncService;

    @Test
    public void addFuncBean() {
        FuncBean funcBean = new FuncBean();
        funcBean.setAuthId(1);
        funcBean.setFuncUri("aaa/bbb/ccc");
        funcBean.setOperatorId("10");
        systemFuncService.addFuncBean(funcBean);
    }

    @Test
    public void updateFuncBean() {
        FuncBean funcBean = new FuncBean();
        funcBean.setAuthId(1);
        funcBean.setFuncUri("aaa/test/ccc");
        funcBean.setOperatorId("10");
        funcBean.setId(3);
        systemFuncService.updateFuncBean(funcBean);
    }

    @Test
    public void getFuncBean() {
        String funcId = "3";
        FuncBean func = systemFuncService.getFuncBean(funcId);
        System.out.println(JsonUtil.beanToJson(func));
    }

    @Test
    public void getFuncBeanList() {
        FuncBean funcBean = new FuncBean();
        List<FuncBean> list = systemFuncService.getFuncBeanList(funcBean);
        for (FuncBean func : list) {
            System.out.println(JsonUtil.beanToJson(func));
        }

    }
}
