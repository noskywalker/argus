package com.monitor.argus.bean.system;

import com.monitor.argus.common.model.PageHelper;

import java.util.Date;

public class RoleModuleBean extends PageHelper implements java.io.Serializable {

    /**  */
    private static final long serialVersionUID = 5563841914830746790L;

    private String id;

    private String roleId;

    private Integer moduleId;

    private Date createTime;

    private Date updateTime;

    private String creatorId;

    private String operatorId;

    private String memo;

    private String remark1;

    private Integer status;

    /** 查询条件-父模块ID：-1-非跟模块的所有模块... */
    private Integer parentId;

    /** 查询条件-是够被设置为某roleId的模块:1-是，0-否 */
    private Integer seted;

    public Integer getSeted() {
        return seted;
    }

    public void setSeted(Integer seted) {
        this.seted = seted;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1 == null ? null : remark1.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}