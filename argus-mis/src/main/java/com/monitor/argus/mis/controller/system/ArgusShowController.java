package com.monitor.argus.mis.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by usr on 2016/11/14.
 */
@Controller
@RequestMapping("/argusshow")
public class ArgusShowController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "/argusshow/flow";
    }
}
