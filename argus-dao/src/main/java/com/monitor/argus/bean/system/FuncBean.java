package com.monitor.argus.bean.system;

import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.util.DateUtil;

import java.util.Date;

/**
 * Created by guomoyi on 16/8/17.
 */
public class FuncBean extends PageHelper implements java.io.Serializable {

    private static final long serialVersionUID = -8863881766812993086L;
    private Integer id;
    private String funcUri;
    private Integer authId;
    private String authName;
    private Date createTime;
    private String operatorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncUri() {
        return funcUri;
    }

    public void setFuncUri(String funcUri) {
        this.funcUri = funcUri;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getCreateTime() {
        if (createTime == null) {
            return null;
        } else {
            return DateUtil.getDateLongTimePlusStr(createTime);
        }
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
