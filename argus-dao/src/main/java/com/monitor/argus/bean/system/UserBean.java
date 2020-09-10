package com.monitor.argus.bean.system;

import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.util.DateUtil;

import java.util.Date;
import java.util.List;

public class UserBean extends PageHelper implements java.io.Serializable {

    /**  */
    private static final long serialVersionUID = -1658856785145772111L;

    private String id;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private Date lastLoginTime;

    private String operatorId;

    private Date createTime;

    private Date updateTime;

    private Integer enable;

    private List<AuthBean> authBeanList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getLastLoginTime() {
        if (lastLoginTime == null) {
            return null;
        }
        return DateUtil.getDateLongTimePlusStr(lastLoginTime);
    }

    public void setLastLoginTime(Date lastLoginTime) {

        this.lastLoginTime = lastLoginTime;
    }

    public String getOperatorId() {

        return operatorId;
    }

    public void setOperatorId(String operatorId) {

        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return DateUtil.getDateLongTimePlusStr(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        if (updateTime == null) {
            return null;
        }
        return DateUtil.getDateLongTimePlusStr(updateTime);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public List<AuthBean> getAuthBeanList() {
        return authBeanList;
    }

    public void setAuthBeanList(List<AuthBean> authBeanList) {
        this.authBeanList = authBeanList;
    }
}