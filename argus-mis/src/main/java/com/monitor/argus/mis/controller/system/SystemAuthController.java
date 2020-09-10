package com.monitor.argus.mis.controller.system;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.service.system.ISystemAuthService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangfeng on 16/8/23.
 */
@Controller
@RequestMapping("/system/auth")
public class SystemAuthController {
    private static Logger logger = LoggerFactory.getLogger(SystemAuthController.class);

    @Autowired
    private ISystemAuthService authService;
    @Autowired
    private IUserRedisService userRedisService;
    @Autowired
    HttpServletRequest request;

    /**
     * @Description: 新增权限
     * @Author:wangfeng
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData addAuth(@RequestParam(value = "authName",required = false)String authName,@RequestParam(value = "parentId",required = false)Integer parentId,@RequestParam(value = "parentName",required = false)String parentName,@RequestParam(value="authType",required = false)Integer authType, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        AuthBean authBean = new AuthBean();
        authBean.setAuthName(authName);
        authBean.setParentId(parentId);
        authBean.setParentName(parentName);
        authBean.setAuthType(authType);
        authBean.setOperatorId(operator.getId());
        logger.info("addAuth 新增权限\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(authBean));
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        try {
            flag = authService.addAuthBean(authBean);
        } catch (Exception e) {
            jsonResponse.setMsg(e.getMessage());
            jsonResponse.setSuccess(false);
            return jsonResponse;
        }
        if (!flag) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("Unknown error");
        }
        return jsonResponse;
    }

    /**
     * @Description: 更新权限
     * @Author:wangfeng
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData editAuth(@RequestParam(value = "authName",required = false)String authName,@RequestParam(value = "parentId",required = false)Integer parentId,@RequestParam(value = "parentName",required = false)String parentName,@RequestParam(value="authType",required = false)Integer authType, @RequestParam(value = "authId",required = false)Integer authId,@RequestParam(value = "enable",required = false)Integer enable, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        AuthBean authBean = new AuthBean();
        authBean.setId(authId);
        authBean.setAuthName(authName);
        authBean.setParentId(parentId);
        authBean.setParentName(parentName);
        authBean.setAuthType(authType);
        authBean.setOperatorId(operator.getId());
        authBean.setEnable(enable);
        logger.info("editAuth 更新权限\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(authBean));
        authBean.setOperatorId(operator.getId());
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        try {
            flag = authService.updateAuthBean(authBean);
        } catch (Exception e) {
            jsonResponse.setMsg(e.getMessage());
            jsonResponse.setSuccess(false);
            return jsonResponse;
        }
        if (!flag) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("Unknown error");
        }
        return jsonResponse;
    }


    /**
     * @Description:获取系统权限列表
     * @date
     * @version V1.0
     */

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/authlistPage")
    public String getAuthListPage(AuthBean authBean, HttpServletRequest request) {
        request.setAttribute("authBean", authBean);
        return "/system/powerManage";

    }

    /**
     * @Description: 权限list
     * @Author:wangfeng
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/list")
    @ResponseBody
    public ResponseData getAuthList(@RequestParam(value = "authName",required = false)String authName,
                                    @RequestParam(value = "parentId",required = false)Integer parentId,
                                    @RequestParam(value = "parentName",required = false)String parentName,
                                    @RequestParam(value="authType",required = false)Integer authType,
                                    @RequestParam(value = "authId",required = false)Integer authId,
                                    @RequestParam(value = "enable",required = false)Integer enable,
                                    @RequestParam(value = "flag", required = false) Integer flag,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "rows", required = false) Integer rows,
                                    @RequestParam(value = "userId", required = false) Integer userId,
                                    HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        AuthBean authBean = new AuthBean();
        authBean.setAuthName(authName);
        authBean.setParentId(parentId);
        authBean.setAuthType(authType);
        authBean.setEnable(enable);
        if (page != null) {
            authBean.setPage(page);
            authBean.setRows(rows);
        }

        logger.info("getAuthList 获取权限列表\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(authBean) + ",flag=" + flag);
        ResponseData jsonResponse = new ResponseData();
        Object list = authService.getAuthBeanList(userId,authBean, flag);
        jsonResponse.setSuccess(true);
        jsonResponse.setObj(list);
        return jsonResponse;
    }


}
