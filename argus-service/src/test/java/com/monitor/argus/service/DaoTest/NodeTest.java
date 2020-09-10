package com.monitor.argus.service.DaoTest;

import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.dao.node.IMonitorNodeDao;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by wangfeng on 16/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class NodeTest {
    @Autowired
    IMonitorNodeDao monitorNodeDao;

    @Autowired
    IArgusTopologyDatalandDao argusTopologyDatalandDao;

    @Test
    public void addNew() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("AAA");
        entity.setNodeKey("AAAkey");
        entity.setNodeSystemName("AAAsystem");
        entity.setNodeSystemId("systemId");
        entity.setNodeUrl("AAAURL");
        monitorNodeDao.insertNode(entity);
    }

    @Test
    public void updateNew() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("AAAName");
        entity.setNodeKey("AAAkey");
        entity.setNodeSystemName("AAAsystem");
        entity.setNodeSystemId("systemId");
        entity.setNodeUrl("AAAURLUpdate");
        monitorNodeDao.updateNode(entity);
    }

    @Test
    public void getNodeInfo() {
        NodeEntity node = monitorNodeDao.getNodeByKey("AAAkey");
        System.out.println(JsonUtil.beanToJson(node));
    }

    @Test
    public void getNodeList() {
        List<NodeEntity> list = monitorNodeDao.getAllNodeList();
        for (NodeEntity node : list) {
            System.out.println(JsonUtil.beanToJson(node));
        }
    }

    @Test
    public void getList() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("AAAName");
        List<NodeEntity> list = monitorNodeDao.getNodeList(entity);
        for (NodeEntity node : list) {
            System.out.println(JsonUtil.beanToJson(node));
        }
    }

    @Test
    public void getData() {
        Date endDate = new Date();
        System.out.println(endDate);
        Date beginDate = DateUtils.addHours(endDate, -12);
        System.out.println(beginDate);
        String begin = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
        String end = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);

        List<ArgusTopologyDatalandEntity> list = argusTopologyDatalandDao.getDataByDate(begin, end);
        for (ArgusTopologyDatalandEntity a : list) {
            System.out.println(JsonUtil.beanToJson(a));
        }
    }

    @Test
    public void hatest() {
        Date endDate = new Date();
        System.out.println(endDate);
        Date beginDate = DateUtils.addHours(endDate, -12);
        System.out.println(beginDate);
        String begin = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
        String end = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);
        System.out.println(begin);
        System.out.println(end);

    }
}
