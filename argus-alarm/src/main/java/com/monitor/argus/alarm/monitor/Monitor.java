package com.monitor.argus.alarm.monitor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:十一月
 * Version:V1.0.0
 */
@Controller
public class Monitor {

    @ResponseBody
    @RequestMapping("/monitor")
    public String monitor(){
        return "ok";
    }
}
