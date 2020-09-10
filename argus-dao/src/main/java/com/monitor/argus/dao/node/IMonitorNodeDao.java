package com.monitor.argus.dao.node;

import com.monitor.argus.bean.dataland.AnalyTopologyHourITEntity;
import com.monitor.argus.bean.node.CollectDataObject;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.TopoAnalyDayUVEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;

import java.util.List;

/**
 * Created by wangfeng on 16/9/19.
 */
public interface IMonitorNodeDao {

    boolean insertNode(NodeEntity nodeEntity);

    List<NodeEntity> getAllNodeList();

    boolean updateNode(NodeEntity nodeEntity);

    NodeEntity getNodeByKey(String nodeKey);

    List<NodeEntity> getNodeList(NodeEntity nodeEntity);

    List<NodeEntity> getAllEnableNodeList();

    List<TopoAnalyEntity> selectAnalyByHours(String beginDate, String endDate, String nodeKey);

    List<TopoAnalyEntity> selectAnalyByMinutes(String beginDate, String endDate, String nodeKey);

    List<TopoAnalyEntity> selectAnalyByDays(String beginDate, String endDate, String nodeKey);


    List<TopoAnalyDayUVEntity> selectDayUVByNodeKey(String beginDate, String endDate, String nodeKey);

    List<CollectDataObject> selectCountPVByDateCount(String beginDate, String endDate);

    List<CollectDataObject> selectCountPVByDate(String beginDate, String endDate);

    List<CollectDataObject> selectCountUVByDateCount(String beginDate, String endDate);

    List<CollectDataObject> selectCountUVByDate(String beginDate, String endDate);

    List<AnalyTopologyHourITEntity> selectInterTimeByNodeKey(String beginDate, String endDate, String nodeKey);

    List<AnalyTopologyHourITEntity> getAvgPertimeByNodeKeyAndDate(String beginDate, String endDate, String nodeKey);

    List<AnalyTopologyHourITEntity> getInterRankingList();

    List<AnalyTopologyHourITEntity> getInterRankingListAsc();

}
