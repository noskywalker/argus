package com.monitor.argus.service.user;

import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.bean.user.WXInfoEntity;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
public interface IUserService {
    boolean addUser(UserEntity userEntity);

    int addGroupUserBatch(List<GroupUserEntity> groupUserEntity);

    List<UserEntity> searchAllUser();

    UserEntity getUserById(String id);

    List<GroupUserEntity> getGroupsByUserId(String id);

    boolean updateUser(UserEntity userEntity);

    boolean deleteGroupUserBatch(String userId);
    boolean deleteUser(String id);

    boolean insertWXInfo(WXInfoEntity wxInfoEntity);

    List<WXInfoEntity> getWXInfoList();

    boolean registerOpenId( String openId);

    WXInfoEntity getWXInfoByOpenId(String openId);

    UserEntity getUserInfoByOpenId(String openId);

    UserEntity getUserInfoByEmail(String email);
}
