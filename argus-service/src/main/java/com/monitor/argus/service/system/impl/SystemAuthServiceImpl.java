package com.monitor.argus.service.system.impl;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.MenuAuth;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.dao.system.ISystemAuthDao;
import com.monitor.argus.dao.system.ISystemFuncDao;
import com.monitor.argus.service.system.ISystemAuthService;
import com.monitor.argus.service.system.ISystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wangfeng on 16/8/22.
 */
@Service("systemAuthService")
public class SystemAuthServiceImpl implements ISystemAuthService {

    @Autowired
    private ISystemAuthDao authDao;
    @Autowired
    private ISystemFuncDao funcDao;
    @Autowired
    private ISystemUserService userService;

    private final Logger logger = LoggerFactory.getLogger(SystemAuthServiceImpl.class);

    @Override
    public boolean addAuthBean(AuthBean authBean) {
        logger.info("Service 添加权限");
        if (!isParentExits(authBean)) {
            throw new RuntimeException("权限的父级菜单非法");
        }
        return authDao.addAuthBean(authBean);
    }

    @Override
    @Transactional
    public boolean updateAuthBean(AuthBean authBean) {
        logger.info("Service 更新权限");
        if (!isParentExits(authBean)) {
            throw new RuntimeException("权限的父级菜单非法");
        }
        AuthBean oldAuth = authDao.getAuthBean(authBean.getId().toString());
        if (oldAuth.getAuthType() == 0 && oldAuth.getParentId() == 0) {
            if ((authBean.getParentId() != null && authBean.getParentId() > 0) || (authBean.getAuthType() != null && authBean.getAuthType() == 1)) {
                AuthBean auth = new AuthBean();
                auth.setParentId(authBean.getId());
                List<AuthBean> list = authDao.getAuthBeanList(auth);
                if (list != null && list.size() > 0) {
                    throw new RuntimeException("存在下级权限,不可修改权限类型或级别");
                }
            }
        }
        authDao.updateAuthBean(authBean);
        authDao.updateParentInfo(authBean);
        funcDao.updateAuthInfo(authBean);

        return true;
    }

    @Override
    public AuthBean getAuthBean(String authId) {
        logger.info("Service 获取权限详情");
        return authDao.getAuthBean(authId);
    }

    @Override
    public Object getAuthBeanList(Integer userId,AuthBean authBean, Integer flag) {
        logger.info("Service 获取权限列表");
        List<AuthBean> list = authDao.getAuthBeanList(authBean);
        if(userId != null && userId>0 ){
            List<AuthBean> authlist = userService.getUserAuthList(userId.toString());
            if (authlist != null) {
                checkAuth(authlist, list);
            }
        }
        if (flag != null && flag == 1) {
            return getMenuAuthList(list);}
        return list;
    }

    private void checkAuth(List<AuthBean> authlist, List<AuthBean> list) {
        Set<Integer> idSet = new HashSet<>();
        for(AuthBean auth:authlist){
            idSet.add(auth.getId());
        }
        for(AuthBean auth:list){
            if(idSet.contains(auth.getId())){
                auth.setChecked(true);
            }else{
                auth.setChecked(false);
            }
        }
    }


    private boolean isParentExits(AuthBean authBean) {
        if (null != authBean.getParentId() && authBean.getParentId() > 0) {
            AuthBean parentAuth = authDao.getAuthBean(authBean.getParentId().toString());
            if (parentAuth == null || parentAuth.getId() <= 0) {
                return false;
            }
            if (parentAuth.getAuthType().intValue() == 1 || parentAuth.getParentId() > 0) {
                return false;
            }
        }
        return true;
    }

    private Object getMenuAuthList(List<AuthBean> list) {
        List<MenuAuth> menulist = new ArrayList<>();
        Map<Integer, MenuAuth> map = new HashMap<>();
        for (AuthBean auth : list) {
            if (auth != null && auth.getParentId() == 0 && auth.getAuthType() == 0) {
                MenuAuth menu = new MenuAuth();
                BeanUtil.copyProperties(menu, auth);
                map.put(menu.getId(), menu);
            }
        }
        for (AuthBean auth : list) {
            if (auth != null && auth.getParentId() != 0) {
                MenuAuth menu = map.get(auth.getParentId());
                if (menu == null) {
                    continue;
                }
                List<AuthBean> kidlist = menu.getKidAuth();
                if (kidlist == null) {
                    kidlist = new ArrayList<>();
                    map.get(auth.getParentId()).setKidAuth(kidlist);
                }
                kidlist.add(auth);
            }
        }
        for (Integer i : map.keySet()) {
            menulist.add(map.get(i));
        }
        return menulist;
    }
}
