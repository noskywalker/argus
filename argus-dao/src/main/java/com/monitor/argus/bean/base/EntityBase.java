/**
 * 
 */
package com.monitor.argus.bean.base;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By alex zhang
 * 
 * @Title: EntityBase.java
 * @Package com.monitor.argus.common.bean
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年6月14日 下午2:31:07
 * @version V1.0
 */

/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	EntityBase.java 
 * @Package com.monitor.argus.common.bean
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年7月1日 下午6:09:38 
 * @version	V1.0   
 */
public class EntityBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3503542355822945168L;
	
	/**
	 * the unique key of a joined up monitor system
	 */
	private String argusId;
	/**
	 * the joined up system's host name/domain/ip addr
	 */
	private String hostName;

	/**
	 * the unique key to identify this log entity(empty temporally)
	 */
	private String id;

	/**
	 * @return the argusId
	 */
	public String getArgusId() {
		return argusId;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param argusId
	 *            the argusId to set
	 */
	public void setArgusId(String argusId) {
		this.argusId = argusId;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
