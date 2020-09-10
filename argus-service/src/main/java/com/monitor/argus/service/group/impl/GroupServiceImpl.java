package com.monitor.argus.service.group.impl;

import com.monitor.argus.bean.group.GroupEntity;
import com.monitor.argus.dao.group.IGroupDao;
import com.monitor.argus.service.group.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
@Service("groupService")
public class GroupServiceImpl implements IGroupService {

    @Autowired
    IGroupDao groupDao;

    @Override
    public boolean addGroup(GroupEntity groupEntity) {
        return groupDao.addGroup(groupEntity);
    }

    @Override
    public List<GroupEntity> searchAllGroup() {
        return groupDao.searchAllGroup();
    }

    @Override
    public GroupEntity getGroupByGroupId(String id) {
        return groupDao.getGroupByGroupId(id);
    }

    @Override
    public boolean updateGroup(GroupEntity groupEntity) {
        return groupDao.updateGroup(groupEntity);
    }
}
