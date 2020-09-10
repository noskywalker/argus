package com.monitor.argus.common.model;

/**
 * 
 * JSON模型
 * 
 * 用户后台向前台返回的JSON对象
 * 
 * @author null
 * 
 */
public class Json implements java.io.Serializable {

    private static final long serialVersionUID = -3768578014924652020L;

    private boolean success = false;

    private String msg = "";

    private Object obj = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
