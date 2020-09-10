package com.monitor.argus.service.system.impl;

import com.monitor.argus.bean.ParentAuthBean;
import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.security.Md5Util;
import com.monitor.argus.dao.system.ISystemAuthDao;
import com.monitor.argus.dao.system.ISystemFuncDao;
import com.monitor.argus.dao.system.ISystemUserDao;
import com.monitor.argus.service.system.ISystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @Description:用户Service接口实现类
 * @Author: wangfeng
 * @Version: V1.0
 */
@Service("systemUserService")
public class SystemUserServiceImpl implements ISystemUserService {

    @Autowired
    private ISystemUserDao userDao;

    @Autowired
    private ISystemAuthDao authDao;
    @Autowired
    private ISystemFuncDao funcDao;

    private final Logger logger = LoggerFactory.getLogger(SystemUserServiceImpl.class);

    @Override
    public boolean addUserBean(UserBean userBean) {
        logger.info("Service 插入用户信息");
        UserBean userMail = new UserBean();
        userMail.setEmail(userBean.getEmail());
        UserBean user = userDao.getUserBean(userMail);
        if (null != user && Integer.parseInt(user.getId()) > 0) {
            throw new RuntimeException("Mail对应用户已经存在");
        }
        userBean.setPassword(Md5Util.getSysUserPasswordMd5("888888"));
        return userDao.addUserBean(userBean);
    }

    @Override
    public boolean updateUserBean(UserBean userBean) {
        logger.info("Service 修改用户信息");
        if (!isUserExits(userBean.getId())) {
            throw new RuntimeException("用户ID无对应用户");
        }
        return userDao.updateUserBean(userBean);
    }

    @Override
    public UserBean getUserBean(UserBean userBean) {
        logger.info("Service 获取用户信息");
        UserBean user = userDao.getUserBean(userBean);
        if (user == null) {
            logger.info("用户名或密码错误-{}", JsonUtil.beanToJson(userBean));
            return null;
        }
        if (user.getEnable() == null || user.getEnable() == 1) {
            logger.info("该账户已被禁用-{}", JsonUtil.beanToJson(userBean));
            return null;
        }
        List<Integer> authIdList = userDao.getAuthListByUserId(user.getId());
        if (authIdList != null && authIdList.size() > 0) {
            List<AuthBean> authList = authDao.getAuthListByIds(authIdList);
            user.setAuthBeanList(authList);
        } else {
            user.setAuthBeanList(null);
        }
        return user;
    }

    @Override
    public List<UserBean> getUserBeanList(UserBean userBean) {
        logger.info("Service 获取用户信息List");
        List<UserBean> userBeanList = userDao.getUserBeanList(userBean);
        return userBeanList;
    }

    @Override
    public boolean editPassword(String oldPass, String newPass, String userId) {
        logger.info("Service 修改用户密码");
        UserBean userBean = new UserBean();
        userBean.setId(userId);
        oldPass = Md5Util.getSysUserPasswordMd5(oldPass);
        userBean.setPassword(oldPass);
        UserBean user = userDao.getUserBean(userBean);
        if (null == user || Integer.parseInt(user.getId()) <= 0) {
            throw new RuntimeException("密码错误");
        }
        newPass = Md5Util.getSysUserPasswordMd5(newPass);
        userBean.setPassword(newPass);
        return userDao.updateUserBean(userBean);
    }

    @Override
    public List<AuthBean> getUserAuthList(String userId) {
        logger.info("Service 获取用户权限列表");
        List<Integer> authIdList = userDao.getAuthListByUserId(userId);
        if (null == authIdList || authIdList.size() == 0) {
            return null;
        }
        List<AuthBean> authList = authDao.getAuthListByIds(authIdList);
        return authList;
    }

    @Override
    @Transactional
    public Boolean editUserAuth(String userId, List<Integer> authIds) {
        logger.info("Service 编辑用户权限-用户ID:{},权限IDs:{}", userId, authIds);
        if (userId == null || !isUserExits(userId)) {
            throw new RuntimeException("用户ID无对应用户,userId=" + userId);
        }
        userDao.deleteUserAuth(userId);
        List<AuthBean> listAuth = authDao.getAuthListByIds(authIds);
        Set<Integer> idSet = new HashSet<>(authIds);
        for (AuthBean auth : listAuth) {
            idSet.add(auth.getParentId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("list", idSet);
        return userDao.addUserAuth(map);

    }

    @Override
    public List<ParentAuthBean> getAuthMenus(String userId) {
        logger.info("Service 获取菜单列表 userId={}", userId);
        List<Integer> authIdList = userDao.getAuthListByUserId(userId);
        if (authIdList == null || authIdList.size() == 0) {
            return null;
        }
        List<AuthBean> authList = authDao.getAuthListByIds(authIdList);
        if (authList == null || authList.size() == 0) {
            return null;
        }
        List<FuncBean> funcList = funcDao.getListByAuth(authIdList);
        Map<Integer, String> funcMap = new HashMap<>();
        for (FuncBean func : funcList) {
            funcMap.put(func.getAuthId(), func.getFuncUri());
        }
        List<ParentAuthBean> parentMenus = new ArrayList<>();
        for (AuthBean auth : authList) {
            auth.setFuncUri(funcMap.get(auth.getId()));
            if (auth.getAuthType() == 0 && (null == auth.getParentId() || auth.getParentId() == 0)) {
                ParentAuthBean parent = new ParentAuthBean();
                parent.setKidList(new ArrayList<AuthBean>());
                BeanUtil.copyProperties(parent, auth);
                for (AuthBean kid : authList) {
                    if (kid.getParentId() == parent.getId() && kid.getAuthType() == 0) {
                        parent.getKidList().add(kid);
                    }
                }
                parentMenus.add(parent);
            }
        }
        return parentMenus;
    }

    private boolean isUserExits(String userId) {
        UserBean userBeanExsit = userDao.getUserBeanByPrimaryKey(userId);
        if (userBeanExsit == null) {
            return false;
        }
        return true;
    }


}
