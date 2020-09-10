package com.monitor.argus.service.user.impl;

import com.monitor.argus.service.user.IUserService;
import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.bean.user.WXInfoEntity;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.dao.user.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuefei on 7/7/16.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDao userDao;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public boolean addUser(UserEntity userEntity) {
        if (userEntity.getOpenId() != null && userEntity.getOpenId().length() > 0) {
            registerOpenId(userEntity.getOpenId());
        }
        return userDao.addUser(userEntity);

    }

    @Override
    public int addGroupUserBatch(List<GroupUserEntity> groupUserEntityList) {
        return userDao.addGroupUserBatch(groupUserEntityList);
    }

    @Override
    public List<UserEntity> searchAllUser() {
        return userDao.searchAllUser();
    }

    @Override
    public UserEntity getUserById(String id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<GroupUserEntity> getGroupsByUserId(String id) {
        return userDao.getGroupsByUserId(id);
    }

    @Override
    @Transactional
    public boolean updateUser(UserEntity userEntity) {
        UserEntity oldUser = userDao.getUserById(userEntity.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", oldUser.getOpenId());
        map.put("enable", 0);
        if (userEntity != null && !StringUtil.isEmpty(userEntity.getOpenId())
                && !StringUtil.isEmpty(oldUser.getOpenId())) {
            if (!oldUser.getOpenId().equals(userEntity.getOpenId())) {
                userDao.disableOpenId(map);
                registerOpenId(userEntity.getOpenId());
            }
        } else if (StringUtil.isEmpty(oldUser.getOpenId()) && !StringUtil.isEmpty(userEntity.getOpenId())) {
            registerOpenId(userEntity.getOpenId());
        } else if (!StringUtil.isEmpty(oldUser.getOpenId()) && StringUtil.isEmpty(userEntity.getOpenId())) {
            userDao.disableOpenId(map);
        }
        if (StringUtil.isEmpty(userEntity.getOpenId())) {
            userEntity.setOpenId("");
        }
        return userDao.updateUser(userEntity);
    }

    @Override
    public boolean deleteGroupUserBatch(String userId) {
        return userDao.deleteGroupUserBatch(userId);
    }

    @Override
    public boolean deleteUser(String id) {
        UserEntity oldUser = userDao.getUserById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", oldUser.getOpenId());
        map.put("enable", 0);
        userDao.disableOpenId(map);
        return userDao.deleteUser(id);
    }

    @Override
    public boolean insertWXInfo(WXInfoEntity wxInfoEntity) {
        logger.info("Service 插入微信信息");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", wxInfoEntity.getOpenId());
        WXInfoEntity entity = userDao.getWXInfo(map);
        if (null != entity) {
            throw new RuntimeException("该OpenId已存在");
        }
        return userDao.insertWXInfo(wxInfoEntity);
    }

    @Override
    public List<WXInfoEntity> getWXInfoList() {
        logger.info("Service 获取微信未注册的OpenId列表");
        return userDao.getWXInfoList(null);
    }

    @Override
    @Transactional
    public boolean registerOpenId(String openId) {
        logger.info("Service 注册OpenId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", openId);
        map.put("enable", 0);
        WXInfoEntity entity = userDao.getWXInfo(map);
        if (null == entity) {
            throw new RuntimeException("该OpenId不存在或已注册,请刷新重试");
        }
        map.put("enable", 1);
        return userDao.disableOpenId(map);


    }

    @Override
    public WXInfoEntity getWXInfoByOpenId(String openId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", openId);
        WXInfoEntity entity = userDao.getWXInfo(map);
        return entity;
    }

    @Override
    public UserEntity getUserInfoByOpenId(String openId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", openId);
        UserEntity userInfo = userDao.getUserInfoByOpenId(map);
        return userInfo;
    }

    @Override
    public UserEntity getUserInfoByEmail(String email) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        UserEntity userInfo = userDao.getUserInfoByEmail(map);
        return userInfo;
    }
}
