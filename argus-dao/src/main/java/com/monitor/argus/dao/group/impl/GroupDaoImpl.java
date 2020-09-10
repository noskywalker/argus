package com.monitor.argus.dao.group.impl;

import com.monitor.argus.bean.group.GroupEntity;
import com.monitor.argus.dao.group.IGroupDao;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Repository("GroupDao")
public class GroupDaoImpl implements IGroupDao {

    @Autowired
    private IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

    @Override
    public boolean addGroup(GroupEntity groupEntity) {
        logger.info("Dao 插入用户组信息");
        groupEntity.setId(UuidUtil.getUUID());
        return baseDao.insert("groupMapper.insert",groupEntity);
    }

    @Override
    public List<GroupEntity> searchAllGroup() {
        logger.info("Dao 搜索组信息");
        return baseDao.getList("groupMapper.selectAllGroup");
    }

    @Override
    public GroupEntity getGroupByGroupId(String id) {
        logger.info("Dao 通过id查询用户组,id = " + id);
        return baseDao.get("groupMapper.selectByPrimaryKey", id);
    }

    @Override
    public boolean updateGroup(GroupEntity groupEntity) {
        logger.info("Dao 更新用户组信息,groupEntity = " + groupEntity.getGroupName());
        return baseDao.update("groupMapper.updateGroup", groupEntity);
    }
}
