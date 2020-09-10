package com.monitor.argus.service.node;

import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wangfeng on 16/9/20.
 */
public interface IMonitorNodeService {
    public void insertNode(NodeEntity nodeEntity);

    public void updateNode(NodeEntity nodeEntity);

    public NodeEntity getNodeByKey(String nodeKey);

    public List<NodeEntity> getAllNodeList();

    public List<NodeEntity> getNodeList(NodeEntity nodeEntity);

    public List<NodeEntity> getAllEnableNodeList();

    Map<String, List<TopoAnalyEntity>> selectAnalyByMinutes(String nodeKey);

    Map<String, List<TopoAnalyEntity>> selectAnalyByHours(String nodeKey);

    List<TopoAnalyEntity> selectAnalyByDays(String nodeKey);

}
