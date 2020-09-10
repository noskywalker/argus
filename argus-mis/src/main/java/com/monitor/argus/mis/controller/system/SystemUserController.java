package com.monitor.argus.mis.controller.system;

import com.monitor.argus.bean.ParentAuthBean;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.mis.controller.system.form.LoginForm;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.service.system.ISystemUserService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.enums.MisResponseCodeEnum;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.RegexUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.security.Md5Util;
import com.monitor.argus.common.util.sms.SmsSendUtil;
import com.monitor.argus.mis.init.AuthCacheService;
import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 系统用户
 * @Author: wangfeng
 */
@Controller
@RequestMapping("/system/user")
public class SystemUserController {

    private static Logger logger = LoggerFactory.getLogger(SystemUserController.class);
    @Autowired
    private ISystemUserService userService;

    @Autowired
    private IUserRedisService userRedisService;

    @Autowired
    private AuthCacheService authCacheService;

    @Autowired
    IMonitorService monitorService;

    @Autowired
    HttpServletRequest request;

    @Value("${sys.testEnabled}")
    private String testEnabled;

    @Value("${systemFlowId}")
    private String systemFlowId;

    /**
     * 用户管理页面
     *
     * @param request
     * @return
     * @Author wangfeng
     */
    @RequestMapping("/userManager")
    public String userManager(HttpServletRequest request) {
        return "/system/userManager";
    }


    /**
     * @Description: 修改系统用户记录
     * @Author:wangfeng
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseData updateUser(@RequestParam(value = "id", required = false) String userId, @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "enable", required = false) Integer enable, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        UserBean userBean = new UserBean();
        userBean.setId(userId);
        userBean.setUserName(userName);
        userBean.setPhone(phone);
        userBean.setEmail(email);
        userBean.setEnable(enable);
        userBean.setOperatorId(operator.getId());
        logger.info("UpdateUser 更新系统用户记录信息\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(userBean));
        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;
        if (userBean == null || null == userBean.getId() || Integer.parseInt(userBean.getId()) == 0) {
            jsonResponse.setSuccess(boo);
            jsonResponse.setMsg("填写信息有误");
            return jsonResponse;
        }
        try {
            boo = userService.updateUserBean(userBean);

        } catch (RuntimeException e) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg(e.getMessage());
            return jsonResponse;
        }
        if (boo) {
            UserBean nowUser = userRedisService.getUserBeanSessionLocal(request);
            // 如果是当前用户
            if (!StringUtil.isEmpty(nowUser.getId()) &&
                    nowUser.getId().equals(userId)) {
                UserBean findUserBean = new UserBean();
                findUserBean.setId(userId);
                UserBean resultUserbean = userService.getUserBean(findUserBean);
                userRedisService.setUserBeanSessionLocal(request, resultUserbean);
            }

            return jsonResponse;
        } else {
            jsonResponse.setSuccess(boo);
            jsonResponse.setMsg("Unknown Error.");
            return jsonResponse;
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseData addUser(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phone", required = false) String phone, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        userBean.setEmail(email);
        userBean.setPhone(phone);
        logger.info("AddUser 增加系统用户记录信息\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。Json:" + JsonUtil.beanToJson(userBean));
        userBean.setOperatorId(operator.getId());
        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;
        if (userBean == null || null == userBean.getEmail()) {
            jsonResponse.setSuccess(boo);
            jsonResponse.setMsg("填写信息有误");
            return jsonResponse;
        }
        try {
            boo = userService.addUserBean(userBean);
        } catch (RuntimeException e) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg(e.getMessage());
            return jsonResponse;
        }
        if (boo) {
            return jsonResponse;
        } else {
            jsonResponse.setSuccess(boo);
            jsonResponse.setMsg("Unknown Error.");
            return jsonResponse;
        }
    }

    /**
     * @Description: 修改系统用户密码
     * @date
     * @version V1.0
     */
    @RequestMapping("/updateUserPassword")
    @ResponseBody
    public ResponseData updateUserPassword(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "oldPass", required = false) String oldPass, @RequestParam(value = "newPass", required = false) String newPass, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("updateUserPassword 修改系统用户密码\n\t@by : " + operator.getId() + "(" + operator.getUserName() + ") Start。Param:userId={}", userId);
        ResponseData response = new ResponseData();
        boolean flag;
        try {
            flag = userService.editPassword(oldPass, newPass, userId);
        } catch (RuntimeException e) {
            response.setMsg(e.getMessage());
            response.setSuccess(false);
            return response;
        }
        if (flag) {
            return response;
        } else {
            response.setResponse(MisResponseCodeEnum.LOCAL_EXCEPTION);
            response.setMsg("修改密码失败！");
            response.setSuccess(false);
            return response;
        }
    }

    /**
     * @Description:获取系统用户信息
     * @date
     * @version V1.0
     */

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/infoPage")
    public String getUserInfoPage(UserBean userBean, HttpServletRequest request) {
        request.setAttribute("userBean", userBean);
        return "/system/personInfo";

    }

    /**
     * @Description:获取系统用户列表
     * @date
     * @version V1.0
     */

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/listPage")
    public String getUserListPage(UserBean userBean, HttpServletRequest request) {
        request.setAttribute("userBean", userBean);
        return "/system/userManage";

    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/list")
    @ResponseBody
    public ResponseData getUserList(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "enable", required = false) Integer enable, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        userBean.setEmail(email);
        userBean.setEnable(enable);
        if (page != null) {
            userBean.setPage(page);
            userBean.setRows(rows);
        }
        logger.info("list 获取用户列表 \n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。Json:" + JsonUtil.beanToJson(userBean));
        ResponseData jsonResponse = new ResponseData();
        List<UserBean> userList = userService.getUserBeanList(userBean);
        jsonResponse.setObj(userList);
        jsonResponse.setSuccess(true);
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/authlist")
    @ResponseBody
    public ResponseData getUserAuthList(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("authlist 获取用户权限列表\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。userId:" + userId);
        ResponseData jsonResponse = new ResponseData();
        List<AuthBean> authList = userService.getUserAuthList(userId);
        jsonResponse.setObj(authList);
        jsonResponse.setSuccess(true);
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/edituserauth")
    @ResponseBody
    public ResponseData editUserAuth(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "authIds", required = false) List<Integer> authIds, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("editUserAuth 编辑用户权限\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。userId:" + userId + ",authIds:" + authIds.toString());
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        try {
            flag = userService.editUserAuth(userId, authIds);
        } catch (RuntimeException e) {
            jsonResponse.setMsg(e.getMessage());
            jsonResponse.setSuccess(false);
            return jsonResponse;
        }
        if (!flag) {
            jsonResponse.setMsg("Unknown Error");
            jsonResponse.setSuccess(true);
        } else {
            // 当前用户刷新权限缓存
            UserBean nowUser = userRedisService.getUserBeanSessionLocal(request);
            // 如果是当前用户
            if (!StringUtil.isEmpty(nowUser.getId()) &&
                    nowUser.getId().equals(userId)) {
                UserBean findUserBean = new UserBean();
                findUserBean.setId(userId);
                UserBean resultUserbean = userService.getUserBean(findUserBean);
                List<FuncBean> userURI = authCacheService.getURIByUser(resultUserbean);
                userRedisService.setUserFuncSessionLocal(request, userURI);
            }
        }
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/userinfo")
    @ResponseBody
    public ResponseData getUserInfo(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("getUserInfo\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。userInfo:" + JsonUtil.beanToJson(operator));
        ResponseData jsonResponse = new ResponseData();
        UserBean user = new UserBean();
        user.setId(operator.getId());
        user.setUserName(operator.getUserName());
        user.setPhone(operator.getPhone());
        user.setEmail(operator.getEmail());
        user.setEnable(operator.getEnable());
        jsonResponse.setObj(user);
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/usermenu")
    @ResponseBody
    public ResponseData getUserMenu(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("getUserMenu\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start。");
        ResponseData jsonResponse = new ResponseData();
        List<ParentAuthBean> menuList = userService.getAuthMenus(operator.getId() == null ? userId : operator.getId());
        jsonResponse.setObj(menuList);
        return jsonResponse;
    }

    /**
     * 登录运营后台
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-10-23 下午05:09:25
     * @Version V1.0
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/login")
    public String login(HttpServletRequest request, LoginForm loginForm, Model model) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        if (!StringUtil.isEmpty(email) && !StringUtil.isEmpty(password)) {
            // 获取登录用户
            UserBean findUserBean = new UserBean();
            findUserBean.setEmail(email);
            findUserBean.setPassword(Md5Util.getSysUserPasswordMd5(password));
            UserBean resultUserbean = userService.getUserBean(findUserBean);
            boolean isCanLogin = checkIsCanLogin(resultUserbean);
            if (isCanLogin) {
                List<FuncBean> userURI = authCacheService.getURIByUser(resultUserbean);
                // 设置session
                // 单机session，没有分布式
                // 放入用户权限
                userRedisService.setUserFuncSessionLocal(request, userURI);
                // 放入用户信息
                userRedisService.setUserBeanSessionLocal(request, resultUserbean);
                logger.info("user:{},email:{} 登录成功", resultUserbean.getUserName(), resultUserbean.getEmail());
                return loginSuccessUrl();
            } else {
                return loginFailUrl();
            }
        } else {
            return loginFailUrl();
        }

        /*String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        if (!StringUtil.isEmpty(email) && !StringUtil.isEmpty(password) && email.matches(".*monitor.cn$") && password.equals("test123456")) {
            UserBean userBean = new UserBean();
            userBean.setEmail(email);
            userBean.setPassword(password);
            request.getSession().setAttribute(SEESION_KEY_LOGIN_USER, userBean);
            return "/dashboard/index";
        } else {
            return "redirect:/";
        }*/
    }

    /**
     * 查询该用户是否可以登录
     *
     * @param resultUserbean
     * @return
     */
    private boolean checkIsCanLogin(UserBean resultUserbean) {
        if (resultUserbean == null || resultUserbean.getAuthBeanList() == null
                || resultUserbean.getAuthBeanList().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 登录成功统一跳转地址
     *
     * @return
     */
    private String loginSuccessUrl() {
        return "/dashboard/index";
    }

    /**
     * 登录失败统一跳转地址
     *
     * @return
     */
    private String loginFailUrl() {
        return "/login";
    }

    /**
     * 获取登录短信验证码
     *
     * @param userBean
     * @return
     * @Author null
     * @Date 2014-10-23 下午05:09:25
     * @Version V1.0
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/getLoginSmsCheckCode")
    @ResponseBody
    public ResponseData<String> getLoginSmsCheckCode(UserBean userBean) {
        String userName = userBean.getUserName();
        logger.info("获取登录短信验证码userName={}", JsonUtil.beanToJson(userName));
        ResponseData<String> responseData = new ResponseData<String>();
        if (StringUtil.isEmpty(userName)) {
            responseData.setResponse(MisResponseCodeEnum.USERNAME_OR_PSW_WRONG);
            return responseData;
        }
        userBean.setPassword(Md5Util.getSysUserPasswordMd5(userBean.getPassword()));
        //userBean.setIsDisabled(0);
        UserBean userBea = userService.getUserBean(userBean);
        if (userBea == null) {
            responseData.setResponse(MisResponseCodeEnum.USERNAME_OR_PSW_WRONG);
            return responseData;
        } else {

            if (StringUtil.isEmpty(userBea.getPhone()) || !RegexUtil.isMobileNo(userBea.getPhone())) {
                responseData.setResponse(MisResponseCodeEnum.NO_BUNDING_MOBILE);
                return responseData;
            } else {
                String smsCheckCode = StringUtil.getRandomString(6);
                boolean sendBoo = SmsSendUtil.sendLoginSmsCheckCode(userBea.getPhone(), smsCheckCode);
                if (sendBoo) {
                    boolean sendRedisBoo = userRedisService.addUserLoginSmsCheckCode(userBea.getPhone(), smsCheckCode);
                    if (sendRedisBoo) {
                        responseData.setResponse(MisResponseCodeEnum.SUCCESS);
                        responseData.setMsg("发送成功！");
                        if (!StringUtil.isEmpty(testEnabled) && testEnabled.equals("1")) {
                            responseData.setObj(smsCheckCode);
                        }
                        return responseData;
                    } else {
                        responseData.setResponse(MisResponseCodeEnum.LOCAL_EXCEPTION);
                        return responseData;
                    }
                } else {
                    responseData.setResponse(MisResponseCodeEnum.LOCAL_EXCEPTION);
                    return responseData;
                }
            }
        }
    }

    /**
     * 退出操作
     *
     * @return
     * @Author null
     * @Date 2014-10-28 下午04:26:55
     * @Version V1.0
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        /*userRedisService.deleteUserBeanSession(request);
        ResponseData<?> responseData = new ResponseData();
        responseData.setResponse(MisResponseCodeEnum.SUCCESS);
        return responseData;*/

        userRedisService.removeUserBeanSessionLocal(request);
        return "/login";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/dashboard/address")
    public String address(Model model, String hanId) {
        List<MonitorSystemEntity> systemEntityList = monitorService.getAllSystems();
        String systemDefaultId = systemFlowId;
        model.addAttribute("systemList", systemEntityList);
        model.addAttribute("hanId", hanId);
        model.addAttribute("systemDefaultId", systemDefaultId);
        return "/dashboard/address";
    }

}
