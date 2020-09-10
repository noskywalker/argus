package com.monitor.argus.mis.controller.group;

import com.google.common.base.Strings;
import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.group.GroupEntity;
import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.mis.controller.group.form.AddGroupForm;
import com.monitor.argus.mis.controller.group.vo.MyGroupVO;
import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.service.group.IGroupService;
import com.monitor.argus.service.user.IUserService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户组管理
 *
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Controller
@RequestMapping("/group")
public class GroupController {
    private static Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IAlarmService alarmService;

    /**
     * 显示添加用户组页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddGroupPage(Model model) {
        List<AlarmStrategyEntity> alarmStrategy = alarmService.getAlarmStrategy();
        model.addAttribute("addGroupForm", new AddGroupForm());
        model.addAttribute("alarmStrategies", alarmStrategy);
        return "/group/addGroup";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseData addGroupPage(AddGroupForm addGroupForm, Model model) {
        GroupEntity groupEntity = new GroupEntity();
        BeanUtil.copyProperties(groupEntity, addGroupForm);
        groupEntity.setEnable(addGroupForm.isGroupEnable() ? 1 : 0);

        ResponseData jsonResponse = new ResponseData();
        jsonResponse.setSuccess(true);
        if (StringUtil.isEmpty(addGroupForm.getId())) {
            groupService.addGroup(groupEntity);
            jsonResponse.setMsg("保存成功!");

        } else {
            groupService.updateGroup(groupEntity);
            jsonResponse.setMsg("修改成功!");

        }

        return jsonResponse;
    }

    @RequestMapping(value = "/getGroup", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ResponseBody
    @Auth(verifyLogin = false, verifyURL = false)
    public ResponseData searchGroup(Model model) {
        List<GroupEntity> groupEntityList = groupService.searchAllGroup();
        ResponseData jsonResponse = new ResponseData();
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("查询成功！");
        jsonResponse.setObj(groupEntityList);
        return jsonResponse;
    }

    @RequestMapping(value = "/getGroupByUser/{id}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ResponseBody
    @Auth(verifyLogin = false, verifyURL = false)
    public ResponseData getGroupByUser(@PathVariable String id) {
        // 查用户组
        ResponseData jsonResponse = new ResponseData();
        UserEntity userInfo = userService.getUserInfoByOpenId(id);
        if(userInfo != null) {
            List<GroupUserEntity> groups = userService.getGroupsByUserId(userInfo.getId());
            List<MyGroupVO> groupVOList = new ArrayList<>();
            for (GroupUserEntity groupUserEntity : groups) {
                MyGroupVO myGroupVO = new MyGroupVO();
                String groupId = groupUserEntity.getGroupId();
                GroupEntity groupByGroupId = groupService.getGroupByGroupId(groupId);
                myGroupVO.setGroupId(groupId);
                myGroupVO.setGroupName(groupByGroupId.getGroupName());
                groupVOList.add(myGroupVO);
            }
            jsonResponse.setObj(groupVOList);
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("查询成功!");
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("用户不存在!");
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/bindGroupAndUser/", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ResponseBody
    @Auth(verifyLogin = false, verifyURL = false)
    public ResponseData bindGroupAndUser(String groupIds, String openId) {
        ResponseData jsonResponse = new ResponseData();
        String userId = "";
        if(!Strings.isNullOrEmpty(openId)) {
            UserEntity user = userService.getUserInfoByOpenId(openId);
            if(user == null) {
                jsonResponse.setSuccess(false);
                jsonResponse.setMsg("用户不存在!");
                return jsonResponse;
            }
            userId = user.getId();
        }

        List<GroupUserEntity> groupUserEntityList = new ArrayList();
        String[] groupIdArray = groupIds.split(",");
        for (String groupId : groupIdArray) {
            if (!Strings.isNullOrEmpty(groupId)) {
                GroupEntity groupByGroupId = groupService.getGroupByGroupId(groupId);
                if(groupByGroupId != null) {
                    GroupUserEntity groupUserEntity = new GroupUserEntity();
                    groupUserEntity.setGroupId(groupId);
                    groupUserEntity.setUserId(userId);
                    groupUserEntityList.add(groupUserEntity);
                }
            }
        }
        boolean b = userService.deleteGroupUserBatch(userId);
        int operateCount = userService.addGroupUserBatch(groupUserEntityList);
        if (operateCount >= 0) {
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("保存成功!");
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("保存失败!");
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/getAllGroup", method = RequestMethod.GET)
    public String searchAllGroup(Model model) {
        List<GroupEntity> groupEntityList = groupService.searchAllGroup();
        if (!CollectionUtils.isEmpty(groupEntityList)) {
            for (GroupEntity groupEntity : groupEntityList) {
                AlarmStrategyEntity alarmStrategyEntity = alarmService.getAlarmStrategyById(groupEntity.getAlarmId());
                if (alarmStrategyEntity != null) {
                    groupEntity.setAlarmStrategyType(alarmStrategyEntity.getAlarmType());
                    groupEntity.setAlarmName(alarmStrategyEntity.getAlarmName());
                } else {
                    groupEntity.setAlarmStrategyType("");
                    groupEntity.setAlarmName("");
                }
            }
        }
        model.addAttribute("searchResult", groupEntityList);
        return "/group/searchGroups";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditMonitorPage(@PathVariable String id, Model model) {
        List<AlarmStrategyEntity> alarmStrategy = alarmService.getAlarmStrategy();

        GroupEntity groupEntity = groupService.getGroupByGroupId(id);
        AddGroupForm addGroupForm = new AddGroupForm();
        BeanUtil.copyProperties(addGroupForm, groupEntity);
        addGroupForm.setGroupEnable(groupEntity.getEnable() == 1);
        addGroupForm.setGroupEnableHidden(groupEntity.getEnable() == 1);
        model.addAttribute("addGroupForm", addGroupForm);
        model.addAttribute("alarmStrategies", alarmStrategy);
        return "/group/addGroup";
    }

}
