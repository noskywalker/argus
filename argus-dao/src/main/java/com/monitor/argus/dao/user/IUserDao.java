package com.monitor.argus.dao.user;


import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.bean.user.WXInfoEntity;

import java.util.List;
import java.util.Map;


/**
 *
 */
public interface IUserDao {
    boolean addUser(UserEntity userEntity);

    int addGroupUserBatch(List<GroupUserEntity> groupUserEntityList);

    List<UserEntity> searchAllUser();

    UserEntity getUserById(String id);

    List<GroupUserEntity> getGroupsByUserId(String id);

    boolean updateUser(UserEntity userEntity);

    boolean deleteGroupUserBatch(String userId);
    boolean deleteUser(String id);

    boolean insertWXInfo(WXInfoEntity wxInfoEntity);

    List<WXInfoEntity> getWXInfoList(Map<String, Object> map);

    boolean disableOpenId(Map<String, Object> map);

    WXInfoEntity getWXInfo(Map<String, Object> map);

    UserEntity getUserInfoByOpenId(Map<String, Object> map);

    UserEntity getUserInfoByEmail(Map<String, Object> map);

}
