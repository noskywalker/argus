package com.monitor.argus.mis.controller;

import com.monitor.argus.common.util.ResourceBundleUtils;
import com.monitor.argus.common.util.UrlUtils;
import com.monitor.argus.service.system.IUserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 基础控制器,其他控制器继承此控制器，获得日期字段类型转换和防止XSS攻击的功能
 *
 * @author null
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected IUserRedisService userRedisService;

	private static final ResourceBundle bundle = ResourceBundleUtils.getBundle("git");
	protected final static String commitId = ResourceBundleUtils.getValue(bundle, "git.commit.id");
	protected final static String commitUserName = ResourceBundleUtils.getValue(bundle, "git.commit.user.name");
	protected final static String buildTime = ResourceBundleUtils.getValue(bundle, "git.build.time");
	protected final static String buildVersion = ResourceBundleUtils.getValue(bundle, "git.build.version");

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

		/**
		 * 防止XSS攻击
		 */
		// binder.registerCustomEditor(String.class, new
		// StringEscapeEditor(true, false));
	}

	/**
	 * 获取IP地址
	 *
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 用户跳转JSP页面
	 * <p>
	 * 此方法不考虑权限控制
	 *
	 * @param folder  路径
	 * @param jspName JSP名称(不加后缀)
	 * @return 指定JSP页面
	 */
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder, @PathVariable String jspName) {
		return "/" + folder + "/" + jspName;
	}

	/**
	 * 所有Action Map 统一从这里获取
	 *
	 * @return
	 */
	public Map<String, Object> getRootMap() {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		// 添加url到 Map中
		rootMap.putAll(UrlUtils.getUrlMap());
		return rootMap;
	}

	public ModelAndView forword(String viewName, Map context) {
		return new ModelAndView(viewName, context);
	}

	public ModelAndView error(String errMsg) {
		return new ModelAndView("error");
	}

}
