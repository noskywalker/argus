package com.monitor.argus.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * datatable模型
 * 
 * @author 薛菲
 * 
 * @date 2016年7月25日 上午11:53:30
 * 
 * @param <T>
 */
public class DataTable<T> implements java.io.Serializable {

    private static final long serialVersionUID = 6961611396930810006L;

    private Long recordsTotal = 0L;

    private Long recordsFiltered = 0L;

    private List<T> data = new ArrayList<T>();

    private int draw = 0;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}
