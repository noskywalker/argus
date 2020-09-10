package com.monitor.argus.mis.controller.system;

import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.service.system.ISystemFuncService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.mis.init.AuthCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wangfeng on 16/8/24.
 */
@Controller
@RequestMapping("/system/func")
public class SystemFuncController {
    private static Logger logger = LoggerFactory.getLogger(SystemFuncController.class);
    @Autowired
    private ISystemFuncService funcService;
    @Autowired
    private IUserRedisService userRedisService;
    @Autowired
    private AuthCacheService authCacheService;
    @Autowired
    HttpServletRequest request;

    /**
     * @Description: 新增功能
     * @Author:wangfeng
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData addFunc(@RequestParam(value = "funcUri", required = false) String funcUri, @RequestParam(value = "authId", required = false) Integer authId, @RequestParam(value = "authName", required = false) String authName, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        FuncBean funcBean = new FuncBean();
        funcBean.setFuncUri(funcUri);
        funcBean.setAuthId(authId);
        funcBean.setAuthName(authName);
        logger.info("addFunc 新增功能\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(funcBean));
        funcBean.setOperatorId(operator.getId());
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        try {
            flag = funcService.addFuncBean(funcBean);
            if (flag) {
                authCacheService.initAllURI();
            }
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
     * @Description: 删除功能
     * @Author:wangfeng
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData deleteFunc(@RequestParam(value = "funcId", required = false) String funcId, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("deleteFunc 删除功能\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.FuncId:" + funcId);
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        flag = funcService.deleteFuncBean(funcId);
        if (flag) {
            authCacheService.initAllURI();
        }
        if (!flag) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("Unknown error");
        }
        return jsonResponse;
    }


    /**
     * @Description:获取系统功能列表
     * @date
     * @version V1.0
     */

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/functionlistPage")
    public String getFuncListPage(FuncBean funcBean, HttpServletRequest request) {
        request.setAttribute("funcBean", funcBean);
        return "/system/functionManage";

    }

    /**
     * @Description: 获得功能列表
     * @Author:wangfeng
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/list")
    @ResponseBody
    public ResponseData getFuncList(@RequestParam(value = "authId", required = false) Integer authId, @RequestParam(value = "funcUri", required = false) String funcUri, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        FuncBean funcBean = new FuncBean();
        funcBean.setAuthId(authId);
        funcBean.setFuncUri(funcUri);
        if (page != null) {
            funcBean.setPage(page);
            funcBean.setRows(rows);
        }

        logger.info("getFuncList 获得功能列表\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(funcBean));
        ResponseData jsonResponse = new ResponseData();
        List<FuncBean> list = funcService.getFuncBeanList(funcBean);
        jsonResponse.setObj(list);
        return jsonResponse;
    }
}
