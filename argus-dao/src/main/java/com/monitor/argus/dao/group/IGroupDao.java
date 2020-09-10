package com.monitor.argus.dao.group;


import com.monitor.argus.bean.group.GroupEntity;

import java.util.List;


/**
 *
 */
public interface IGroupDao {
    boolean addGroup(GroupEntity groupEntity);

    List<GroupEntity> searchAllGroup();

    GroupEntity getGroupByGroupId(String id);

    boolean updateGroup(GroupEntity groupEntity);

}
