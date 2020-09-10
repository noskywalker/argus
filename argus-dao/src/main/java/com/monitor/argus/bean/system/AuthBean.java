package com.monitor.argus.bean.system;

import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.util.DateUtil;

import java.util.Date;

/**
 * Created by wangfeng on 16/8/17.
 */
public class AuthBean extends PageHelper implements java.io.Serializable {

    private static final long serialVersionUID = 2549905903572354136L;
    private Integer id;
    private String authName;
    private Integer parentId;
    private Integer authType;
    private Integer enable;
    private Date createTime;
    private Date updateTime;
    private String operatorId;
    private String parentName;
    private String funcUri;
    private boolean checked;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getFuncUri() {
        return funcUri;
    }

    public void setFuncUri(String funcUri) {
        this.funcUri = funcUri;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
