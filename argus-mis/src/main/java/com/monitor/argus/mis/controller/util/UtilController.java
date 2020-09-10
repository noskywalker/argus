package com.monitor.argus.mis.controller.util;

import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.mis.controller.util.vo.DeleteDbResult;
import com.monitor.argus.service.util.IUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Controller
@RequestMapping("/util")
public class UtilController {
    private static Logger logger = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private IUtilService utilService;
    /**
     * 显示添加用户组页面
     */

    @RequestMapping(value = "/truncate/{tableName}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData truncateSingleTable(@PathVariable String tableName, Model model) {
        ResponseData responseData = new ResponseData();
        int result = utilService.truncateSingleTable(tableName);
        boolean isSuccess = result >= 0 ? true : false;
        responseData.setSuccess(isSuccess);
        responseData.setMsg(isSuccess ? "执行成功" : "执行失败");
        return  responseData;
    }

    @RequestMapping(value = "/truncate/all", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseData truncateAllTables(Model model) {
        ResponseData responseData = new ResponseData();

        List<String> tableNames = new ArrayList<>();

        tableNames.add("t_alarm_group_user");
        tableNames.add("t_alarm_group");
        tableNames.add("t_argus_user");
        tableNames.add("t_argus_alarms");
        tableNames.add("t_monitor_host");
        tableNames.add("t_monitor_strategy");
        tableNames.add("t_monitor_system");
        tableNames.add("t_alarm_strategy");

        boolean totalResult = true;
        List<DeleteDbResult> dbResults = new ArrayList<>();
        for(String tableName : tableNames) {
            DeleteDbResult deleteDbResult = new DeleteDbResult();
            int rowCount = utilService.truncateSingleTable(tableName);
            deleteDbResult.setRowCount(rowCount);
            deleteDbResult.setTableName(tableName);
            boolean singleSuccess = rowCount >= 0 ? true : false;
            deleteDbResult.setSuccess(singleSuccess);
            dbResults.add(deleteDbResult);
        }

        responseData.setSuccess(totalResult);
        responseData.setMsg(totalResult ? "执行成功" : "执行失败");
        responseData.setObj(dbResults);
        return responseData;
    }


}
