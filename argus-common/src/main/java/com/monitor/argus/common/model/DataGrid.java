package com.monitor.argus.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUI DataGrid模型
 * 
 * @author null
 * 
 * @operator null
 * @date 2016年5月6日 上午11:53:30
 * 
 * @param <T>
 */
public class DataGrid<T> implements java.io.Serializable {

    private static final long serialVersionUID = 6961611396930810006L;

    private Long total = 0L;

    private List<T> rows = new ArrayList<T>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
