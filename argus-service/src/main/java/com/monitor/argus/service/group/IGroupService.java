package com.monitor.argus.service.group;

import com.monitor.argus.bean.group.GroupEntity;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
public interface IGroupService {
    boolean addGroup(GroupEntity groupEntity);

    List<GroupEntity> searchAllGroup();

    GroupEntity getGroupByGroupId(String id);

    boolean updateGroup(GroupEntity groupEntity);
}
