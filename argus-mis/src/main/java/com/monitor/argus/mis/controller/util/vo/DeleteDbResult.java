package com.monitor.argus.mis.controller.util.vo;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/22/16
 * @Version
 */
public class DeleteDbResult {
    private String tableName;
    private int rowCount;
    private boolean isSuccess;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
