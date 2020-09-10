package com.monitor.argus.bean.system;

import com.monitor.argus.common.model.PageHelper;

import java.util.Date;
import java.util.List;

public class ModuleBean extends PageHelper implements java.io.Serializable {

    /**  */
    private static final long serialVersionUID = -7928276189801145877L;

    private Integer id;

    private String moduleName;

    private String moduleUrl;

    private Integer parentId;

    private String parentModuleName;

    private Date createTime;

    private Date updateTime;

    private Integer moduleSort;

    private String creatorId;

    private String operatorId;

    private String memo;

    private String remark1;

    private Integer status;

    /** 查询条件-是够被设置为某roleId的模块:1-是，0-否 */
    private Integer seted;

    private List<ModuleBean> subModules;

    // easyUi Tree参数
    private List<ModuleBean> children;

    public String getParentModuleName() {
        return parentModuleName;
    }

    public void setParentModuleName(String parentModuleName) {
        this.parentModuleName = parentModuleName;
    }

    public List<ModuleBean> getChildren() {
        return children;
    }

    public void setChildren(List<ModuleBean> children) {
        this.children = children;
    }

    public Integer getSeted() {
        return seted;
    }

    public void setSeted(Integer seted) {
        this.seted = seted;
    }

    public List<ModuleBean> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<ModuleBean> subModules) {
        this.subModules = subModules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl == null ? null : moduleUrl.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getModuleSort() {
        return moduleSort;
    }

    public void setModuleSort(Integer moduleSort) {
        this.moduleSort = moduleSort;
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