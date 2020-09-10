package com.monitor.argus.dao.user.impl;

import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.bean.user.WXInfoEntity;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.user.IUserDao;
import com.monitor.argus.common.util.EmojiUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Repository("UserDao")
public class UserDaoImpl implements IUserDao {

    @Autowired
    private IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public boolean addUser(UserEntity userEntity) {
        logger.info("Dao 插入用户组信息");
        userEntity.setId(UuidUtil.getUUID());
        return baseDao.insert("userMapper.insert", userEntity);
    }

    @Override
    public int addGroupUserBatch(List<GroupUserEntity> groupUserEntityList) {
        logger.info("Dao 插入用户与用户组映射信息");
        for (GroupUserEntity groupUserEntity : groupUserEntityList) {
            groupUserEntity.setId(UuidUtil.getUUID());
        }
        int insertCount = baseDao.insertBatch("groupUserMapper.insert", groupUserEntityList);
        logger.debug("批量增加用户与用户组映射信息-E{}", insertCount);
        return insertCount;
    }

    @Override
    public List<UserEntity> searchAllUser() {
        logger.info("Dao 用户信息");
        return baseDao.getList("userMapper.selectAllUser");
    }

    @Override
    public UserEntity getUserById(String id) {
        logger.info("Dao 通过id查询用户,id = " + id);
        return baseDao.get("userMapper.selectByPrimaryKey", id);
    }

    @Override
    public List<GroupUserEntity> getGroupsByUserId(String id) {
        logger.info("Dao 通过id查询用户组,id = " + id);
        return baseDao.getList("groupUserMapper.getGroupsByUserId", id);
    }

    @Override
    public boolean updateUser(UserEntity userEntity) {
        logger.info("Dao 更新用户信息,name = " + userEntity.getUserName());
        return baseDao.update("userMapper.updateUser", userEntity);
    }

    @Override
    public boolean deleteGroupUserBatch(String userId) {
        logger.info("Dao 删除用户与用户组映射信息");
        return baseDao.delete("groupUserMapper.deleteGroupUser", userId);
    }
    @Override
    public boolean deleteUser(String id) {
        logger.info("Dao 删除用户");
        return baseDao.delete("userMapper.deleteUser", id);
    }

    @Override
    public boolean insertWXInfo(WXInfoEntity wxInfoEntity) {
        logger.info("Dao 插入微信信息");

        // 插入数据
        boolean result = false;
        try {
            result = baseDao.insert("userMapper.insertWXInfo", wxInfoEntity);
        } catch (Exception e) {
            logger.info("{}插入时候失败，名称有特殊符号", JsonUtil.beanToJson(wxInfoEntity));
        }
        // 插入失败
        if (!result) {
            logger.info("第一次插入数据失败，过滤特殊表情再次插入数据", JsonUtil.beanToJson(wxInfoEntity));
            // 过滤Emoji表情
            if (!StringUtil.isEmpty(wxInfoEntity.getNickName())) {
                logger.info(" 过滤前的nickname:{}", wxInfoEntity.getNickName());
                String nickName = EmojiUtil.filterEmoji(wxInfoEntity.getNickName());
                logger.info("过滤后的nickname:{}", nickName);
                wxInfoEntity.setNickName(nickName);
            }
            try {
                result = baseDao.insert("userMapper.insertWXInfo", wxInfoEntity);
            } catch (Exception e) {
                logger.info("{}第二次插入时候失败，名称有特殊符号", JsonUtil.beanToJson(wxInfoEntity));
            }
            // 插入失败
            if (!result) {
                logger.info("第二次插入数据失败，昵称改成未知第三次插入数据");
                if (!StringUtil.isEmpty(wxInfoEntity.getNickName())) {
                    logger.info("改成未知前nickname:{}", wxInfoEntity.getNickName());
                    wxInfoEntity.setNickName("未知");
                }
                try {
                    result = baseDao.insert("userMapper.insertWXInfo", wxInfoEntity);
                } catch (Exception e) {
                    logger.info("{}第三次插入时候失败", JsonUtil.beanToJson(wxInfoEntity));
                }
            }
        }
        return result;
    }

    @Override
    public List<WXInfoEntity> getWXInfoList(Map<String, Object> map) {
        logger.info("Dao 获取可用的微信列表");
        return baseDao.getList("userMapper.getWXInfoList");
    }

    @Override
    public boolean disableOpenId(Map<String, Object> map) {
        logger.info("Dao 更新微信状态");
        return baseDao.update("userMapper.disableOpenId", map);
    }

    @Override
    public WXInfoEntity getWXInfo(Map<String, Object> map) {
        logger.info("Dao 通过openId获取详情");
        return baseDao.get("userMapper.getWXInfo", map);
    }

    @Override
    public UserEntity getUserInfoByOpenId(Map<String, Object> map) {
        logger.info("Dao 通过openId获取用户详情");
        return baseDao.get("userMapper.getUserInfoByOpenId", map);
    }

    @Override
    public UserEntity getUserInfoByEmail(Map<String, Object> map) {
        logger.info("Dao 通过email获取用户详情");
        return baseDao.get("userMapper.getUserInfoByEmail", map);
    }

}
