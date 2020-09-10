package com.monitor.argus.mis.interceptor;

import com.monitor.argus.common.util.ArgusUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class LogPushInterceptor extends HandlerInterceptorAdapter {

	private List<String> argusId = new ArrayList<String>();
	/**
	 * 
	 */
	private static final String ARGUS_ID = "ARGUS_ID";
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (ArgusUtils.debugMode) {
			return true;
		}
		
		String argusId = request.getHeader(ARGUS_ID);
		if (StringUtils.isEmpty(argusId) || !argusId.contains(argusId)) {
			IOUtils.write("argus id is invalid", response.getOutputStream());
			return false;
		}
		String content = IOUtils.toString(request.getInputStream());
		if (StringUtils.isEmpty(content)) {
			IOUtils.write("service is runing..", response.getOutputStream());
			return false;
		}

		logger.info("request received!");
		return true;
	}

}
