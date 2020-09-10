package com.monitor.argus.service.ServiceTest;

import com.monitor.argus.service.dataland.IArgusTopologyDatalandService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.common.util.JsonUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by wangfeng on 16/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class NodeServiceTest {
    @Autowired
    IMonitorNodeService nodeService;

    @Autowired
    IArgusTopologyDatalandService argusTopologyDatalandService;

    @Test
    public void addNew() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("CCC");
        entity.setNodeSystemName("AAAsystem");
        entity.setNodeSystemId("systemId");
        entity.setNodeUrl("CCCURL");
        nodeService.insertNode(entity);
    }

    @Test
    public void updateNew() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("BBBName");
        entity.setNodeKey("BBBkey");
        entity.setNodeSystemName("AAAsystem");
        entity.setNodeSystemId("systemId");
        entity.setNodeUrl("BBBURLUpdate");
        nodeService.updateNode(entity);
    }

    @Test
    public void getNodeInfo() {
        NodeEntity node = nodeService.getNodeByKey("BBBkey");
        System.out.println(JsonUtil.beanToJson(node));
    }

    @Test
    public void getNodeList() {
        List<NodeEntity> list = nodeService.getAllNodeList();
        for (NodeEntity node : list) {
            System.out.println(JsonUtil.beanToJson(node));
        }
    }

    @Test
    public void getLisaat() {
        NodeEntity entity = new NodeEntity();
        entity.setNodeName("BBBName");
        List<NodeEntity> list = nodeService.getNodeList(entity);
        for (NodeEntity node : list) {
            System.out.println(JsonUtil.beanToJson(node));
        }
    }

    @Test
    public void getData() {
        Map<String, List<ArgusTopologyDatalandEntity>> map = argusTopologyDatalandService.getDataByDate(null);
        List<ArgusTopologyDatalandEntity> lista = map.get("todayData");
        List<ArgusTopologyDatalandEntity> listb = map.get("yesterdayData");
        System.out.println("today");
        for (ArgusTopologyDatalandEntity a : lista) {
            System.out.println(JsonUtil.beanToJson(a));
        }
        System.out.println("yesterday");
        for (ArgusTopologyDatalandEntity a : listb) {
            System.out.println(JsonUtil.beanToJson(a));
        }

    }

    @Test
    public void Testss() {
        Map<String, List<ArgusTopologyDatalandEntity>> map = argusTopologyDatalandService.getDataByDate(24);
        if (map != null) {
            List<ArgusTopologyDatalandEntity> todayList = map.get("todayData");
            List<ArgusTopologyDatalandEntity> yesterdayList = map.get("yesterdayData");
            List<String> numLista = new ArrayList<>();
            List<String> dateLista = new ArrayList<>();
            List<String> numListb = new ArrayList<>();
            List<String> dateListb = new ArrayList<>();
            getList(todayList, dateLista, numLista);
            getList(yesterdayList, dateListb, numListb);
            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("todaytime", dateLista);
            mapResult.put("todaydata", numLista);
            mapResult.put("yesterdaytime", dateListb);
            mapResult.put("yesterdaydata", numListb);

            String json = JsonUtil.beanToJson(mapResult);
            System.out.println(json);
        }
    }

    private void getList(List<ArgusTopologyDatalandEntity> todayList, List<String> dateList, List<String> numList) {
        List<Date> dateAList = new ArrayList<>();
        for (ArgusTopologyDatalandEntity entity : todayList) {
            if (dateAList != null && dateAList.size() > 0) {
                int a = dateAList.get(dateAList.size() - 1).getHours();
                int b = entity.getCreateDate().getHours();
                Integer diff = b - a;
                if (diff > 1) {
                    while (diff-- > 1) {
                        dateAList.add(DateUtils.addHours(dateAList.get(dateAList.size() - 1), 1));
                        numList.add("0");
                    }
                }
            }
            dateAList.add(entity.getCreateDate());
            numList.add("0");
        }
        Integer i = 0;
        for (Date date : dateAList) {
            for (ArgusTopologyDatalandEntity entity : todayList) {
                if (date.equals(entity.getCreateDate())) {
                    numList.set(i, entity.getDiffLogBytes());
                }
            }
            i++;
        }

        for (Date d : dateAList) {
            dateList.add(d.getHours() + ":00");
        }

    }

}
