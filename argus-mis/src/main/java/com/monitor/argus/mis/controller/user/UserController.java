package com.monitor.argus.mis.controller.user;

import com.google.common.base.Strings;
import com.monitor.argus.bean.group.GroupEntity;
import com.monitor.argus.bean.groupuser.GroupUserEntity;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.bean.user.UserEntity;
import com.monitor.argus.bean.user.WXInfoEntity;
import com.monitor.argus.service.group.IGroupService;
import com.monitor.argus.service.mail.Email;
import com.monitor.argus.service.mail.JmsMailSender;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.service.user.IUserService;
import com.monitor.argus.common.aes.AESUtils;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.mis.controller.user.form.AddUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IUserRedisService userRedisService;
    @Autowired
    JmsMailSender mailSender;

    @Value("${weixin.url}")
    private String weixinUrl;

    /**
     * 显示添加用户组页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddUserPage(Model model) {
        List<GroupEntity> groupEntityList = groupService.searchAllGroup();
        List<WXInfoEntity> wxlist = userService.getWXInfoList();
        model.addAttribute("addUserForm", new AddUserForm());
        model.addAttribute("groupList", groupEntityList);
        model.addAttribute("openIdList", wxlist);
        return "/user/addUser";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseData showAddUserPage(AddUserForm addUserForm, Model model) {

        UserEntity userEntity = new UserEntity();
        BeanUtil.copyProperties(userEntity, addUserForm);

        int operateCount = 0;
        boolean operateUserResult;
        String passUserId = addUserForm.getId();
        if (StringUtil.isEmpty(passUserId)) {
            operateUserResult = userService.addUser(userEntity);
        } else {
            operateUserResult = userService.updateUser(userEntity);
        }

        if (operateUserResult) {
            String userId = userEntity.getId();
            List<String> groups = addUserForm.getGroupIds();
            List<GroupUserEntity> groupUserEntityList = new ArrayList();
            for (String groupId : groups) {
                GroupUserEntity groupUserEntity = new GroupUserEntity();
                groupUserEntity.setGroupId(groupId);
                groupUserEntity.setUserId(userId);
                groupUserEntityList.add(groupUserEntity);
            }

            if (passUserId.equals(userId)) {
                userService.deleteGroupUserBatch(userId);
            }
            operateCount = userService.addGroupUserBatch(groupUserEntityList);
        }

        ResponseData jsonResponse = new ResponseData();

        if (operateCount > 0) {
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("保存成功!");
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("保存失败!");
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public String searchAllUser(Model model, String hanId) {
        List<UserEntity> userEntityList = userService.searchAllUser();
        if (!CollectionUtils.isEmpty(userEntityList)) {
            for (UserEntity userEntity : userEntityList) {
                // 查微信
                String niceName = "";
                if (!StringUtil.isEmpty(userEntity.getOpenId())) {
                    WXInfoEntity wXInfoEntity = userService.getWXInfoByOpenId(userEntity.getOpenId());
                    if (wXInfoEntity != null) {
                        if (!StringUtil.isEmpty(wXInfoEntity.getNickName())) {
                            niceName = wXInfoEntity.getNickName();
                        }
                    }
                }

                // 查用户组
                StringBuffer groupnames = new StringBuffer("");
                List<GroupUserEntity> groups = userService.getGroupsByUserId(userEntity.getId());
                if (!CollectionUtils.isEmpty(groups)) {
                    for (GroupUserEntity group : groups) {
                        GroupEntity groupEntity = groupService.getGroupByGroupId(group.getGroupId());
                        if (groupEntity != null) {
                            groupnames.append(groupEntity.getGroupName() + "，");
                        }
                    }
                }

                userEntity.setGroups(groupnames.toString());
                userEntity.setWxName(niceName);
            }
        }
        model.addAttribute("searchResult", userEntityList);
        model.addAttribute("hanId", hanId);
        return "/user/searchUsers";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showEditUserPage(@RequestParam(value = "id", required = true) String id, Model model) {
        UserEntity userEntity = userService.getUserById(id);
        AddUserForm addUserForm = new AddUserForm();
        BeanUtil.copyProperties(addUserForm, userEntity);

        List<GroupUserEntity> groups = userService.getGroupsByUserId(id);
        List<String> groupIds = new ArrayList();
        for (GroupUserEntity group : groups) {
            groupIds.add(group.getGroupId());
        }
        addUserForm.setGroupIds(groupIds);

        List<WXInfoEntity> wxList = userService.getWXInfoList();
        if (addUserForm.getOpenId() != null && addUserForm.getOpenId().length() > 0) {
            WXInfoEntity wxInfo = userService.getWXInfoByOpenId(addUserForm.getOpenId());
            if (wxInfo != null) {
                wxList.add(wxInfo);
            }
        }
        List<GroupEntity> groupEntityList = groupService.searchAllGroup();
        model.addAttribute("groupList", groupEntityList);
        model.addAttribute("addUserForm", addUserForm);
        model.addAttribute("openIdList", wxList);
        return "/user/addUser";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/addWXInfo")
    @ResponseBody
    public ResponseData addWXInfo(WXInfoEntity wxInfo, HttpServletRequest request) {
        logger.info("添加微信信息接口 param:" + JsonUtil.beanToJson(wxInfo));
        ResponseData jsonResponse = new ResponseData();
        boolean flag;
        try {
            wxInfo.setNickName(URLDecoder.decode(wxInfo.getNickName(), "UTF-8"));
            flag = userService.insertWXInfo(wxInfo);
        } catch (Exception e) {
            logger.info("插入微信信息失败,msg:" + e.getMessage());
            jsonResponse.setMsg(e.getMessage());
            jsonResponse.setSuccess(false);
            return jsonResponse;
        }

        if (!flag) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("保存失败!");
        }
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/WXList")
    @ResponseBody
    public ResponseData getWXInfoList() {
        logger.info("获取微信列表接口");
        ResponseData jsonResponse = new ResponseData();
        List<WXInfoEntity> list = userService.getWXInfoList();
        jsonResponse.setObj(list);
        return jsonResponse;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id, Model model, String hanId, HttpServletRequest request) {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("删除报警用户:{},操作人:{}", id, operator.getEmail());
        boolean flag = userService.deleteGroupUserBatch(id);
        if (flag) {
            userService.deleteUser(id);
        }


        List<UserEntity> userEntityList = userService.searchAllUser();
        if (!CollectionUtils.isEmpty(userEntityList)) {
            for (UserEntity userEntity : userEntityList) {
                // 查微信
                String niceName = "";
                if (!StringUtil.isEmpty(userEntity.getOpenId())) {
                    WXInfoEntity wXInfoEntity = userService.getWXInfoByOpenId(userEntity.getOpenId());
                    if (wXInfoEntity != null) {
                        if (!StringUtil.isEmpty(wXInfoEntity.getNickName())) {
                            niceName = wXInfoEntity.getNickName();
                        }
                    }
                }

                // 查用户组
                StringBuffer groupnames = new StringBuffer("");
                List<GroupUserEntity> groups = userService.getGroupsByUserId(userEntity.getId());
                if (!CollectionUtils.isEmpty(groups)) {
                    for (GroupUserEntity group : groups) {
                        GroupEntity groupEntity = groupService.getGroupByGroupId(group.getGroupId());
                        if (groupEntity != null) {
                            groupnames.append(groupEntity.getGroupName() + "，");
                        }
                    }
                }
                userEntity.setGroups(groupnames.toString());
                userEntity.setWxName(niceName);
            }
        }
        model.addAttribute("searchResult", userEntityList);
        model.addAttribute("hanId", hanId);
        return "/user/searchUsers";
    }

    @RequestMapping(value = "/sendAddUserRequest", method = RequestMethod.POST)
    @Auth(verifyLogin = false, verifyURL = false)
    @ResponseBody
    public ResponseData sendAddUserRequest(@RequestParam(value = "name", required = true) String name,
                          @RequestParam(value = "email", required = true) String email,
                          @RequestParam(value = "mobile", required = true) String mobile,
                          @RequestParam(value = "openId", required = true) String openId,
                          HttpServletRequest request) {
        ResponseData jsonResponse = new ResponseData();

        if (Strings.isNullOrEmpty(name) ||
                Strings.isNullOrEmpty(email) ||
                Strings.isNullOrEmpty(mobile) ||
                Strings.isNullOrEmpty(openId)) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("参数不可以为空!");
            return jsonResponse;
        }

        if(!email.toLowerCase().endsWith("monitor.cn") && !email.toLowerCase().endsWith("monitor.com")) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("请使用公司邮箱注册!");
            return jsonResponse;
        }

        //todo 查询输入姓名与邮箱前缀是否匹配
        UserEntity userInfoByEmail = userService.getUserInfoByEmail(email);
        if(userInfoByEmail != null) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("用户已存在");
            return jsonResponse;
        }

        String content = URLEncoder.encode(name) + "&" + URLEncoder.encode(email) + "&" + URLEncoder.encode(mobile) + "&" + URLEncoder.encode(openId);
        String encrypt_content = AESUtils.encrypt(content);
        String url = weixinUrl + "/AlarmController/checkAccount.action?rel=" + URLEncoder.encode(encrypt_content);
        logger.info("email send url:" + url);


        Email emailObj = new Email();
        emailObj.setFrom("noreply@monitor.cn");
        emailObj.setAddressee(email);
        emailObj.setContent("邮件激活");
        emailObj.setSubject("接入报警系统用户激活邮件");

        Map<String, Object> mailType = new HashMap<>();
        mailType.put("EMAIL_TYPE", "110110110");
        mailType.put("url", url);
        emailObj.setModel(mailType);
        boolean sendSuccess = mailSender.sendCommonMail(emailObj);
        if(sendSuccess) {
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("激活邮件已发送");
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("激活邮件发送失败");
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @Auth(verifyLogin = false, verifyURL = false)
    @ResponseBody
    public ResponseData addUser(@RequestParam(value = "rel", required = true) String rel,
                                HttpServletRequest request) {
        ResponseData jsonResponse = new ResponseData();

        if (Strings.isNullOrEmpty(rel)) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("参数不可以为空!");
            return jsonResponse;
        }

        String decrypt_content = AESUtils.decrypt(rel);
        String[] params = decrypt_content.split("&");
        String name = URLDecoder.decode(params[0]);
        String email = URLDecoder.decode(params[1]);
        String mobile = URLDecoder.decode(params[2]);
        String openId = URLDecoder.decode(params[3]);

        UserEntity userInfoByEmail = userService.getUserInfoByEmail(email);
        if (userInfoByEmail != null) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("用户已存在");
            return jsonResponse;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setCellphone(mobile);
        userEntity.setEmail(email);
        userEntity.setUserName(name);
        userEntity.setTelephone(mobile);
        userEntity.setOpenId(openId);
        try {
            boolean saveSuccess = userService.addUser(userEntity);
            if (saveSuccess) {
                jsonResponse.setSuccess(true);
                jsonResponse.setMsg("激活邮件已发送");
            } else {
                jsonResponse.setSuccess(false);
                jsonResponse.setMsg("激活邮件发送失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jsonResponse;
    }
}
