/** */
package com.monitor.argus.mis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午08:54:00
 * @Version: V1.0
 * 
 * 
 */
@Controller
@RequestMapping("/")
public class CommonPageController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CommonPageController.class);

    @Autowired
    HttpServletRequest request;

    /**
     * 系统登录页面
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public String loginPage(HttpServletRequest request) {
        request.setAttribute("commitId", commitId);
        request.setAttribute("commitUserName", commitUserName);
        request.setAttribute("buildTime", buildTime);
        request.setAttribute("buildVersion", buildVersion);
        return "/login";
    }

    /**
     * 系统登录loginAjax页面
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/loginAjax")
    public String loginAjaxPage(HttpServletRequest request) {
        return "/loginAjax";
    }

    /**
     * 系统登录dialog页面
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/loginDialog")
    public String loginDialogPage(HttpServletRequest request) {
        return "/loginDialog";
    }

    /**
     * 系统首页
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "/index";
    }

    /**
     * 系统页面north
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/north")
    public String north(HttpServletRequest request) {
        return "/north";
    }

    /**
     * 系统页面south
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/south")
    public String south(HttpServletRequest request) {
        request.setAttribute("commitId", commitId);
        request.setAttribute("commitUserName", commitUserName);
        request.setAttribute("buildTime", buildTime);
        request.setAttribute("buildVersion", buildVersion);
        return "/south";
    }

    /**
     * 系统页面west
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/west")
    public String west(HttpServletRequest request) {
        return "/west";
    }

    /**
     * 系统页面about
     * 
     * @author null
     * @date 2016年5月4日 下午9:37:42
     * 
     * @param request
     * @return
     */
    @RequestMapping("/about")
    public String about(HttpServletRequest request) {
        return "/about";
    }
}
