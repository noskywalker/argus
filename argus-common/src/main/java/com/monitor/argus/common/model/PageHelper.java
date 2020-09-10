package com.monitor.argus.common.model;

/**
 * EasyUI 分页帮助类
 * 
 * @author null
 * 
 */
public class PageHelper implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private int page;// 当前页
	private int rows;// 每页显示记录数
	private String sort;// 排序字段
	private String order;// asc/desc
	private int pageStart;//记录开始的index
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		setPageIndex();
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	//limit 开始index设置
	public void setPageIndex() {
		this.pageStart = (this.page - 1) * this.rows;
	}
	

}
