package com.monitor.argus.common.model;


import com.monitor.argus.common.enums.MisResponseCodeEnum;
import com.monitor.argus.common.util.ConstantsForCommon;

/**
 * 请求响应对象
 * 
 * 向前台返回的ResponseData对象
 * 
 * @author null
 * @date 2016年5月10日 下午5:25:33
 * 
 * @param <T>
 */
public class ResponseData<T> implements java.io.Serializable {

    private static final long serialVersionUID = -3768578014924652020L;

    private boolean success = true;

    private String msg = ConstantsForCommon.EMPTY_STRING;

    private String code = ConstantsForCommon.EMPTY_STRING;

    private T obj = null;

    /**
     * 默认构造方法:默认为成功
     * 
     */
    public ResponseData() {
        this.setResponse(MisResponseCodeEnum.SUCCESS);
    }

    /**
     * 构造方法
     * 
     * @param obj 返回的obj对象
     * @param misResponseCodeEnum 返回的状态、提示信息
     */
    public ResponseData(T obj, MisResponseCodeEnum misResponseCodeEnum) {
        this.obj = obj;
        this.success = misResponseCodeEnum.isStatus();
        this.code = misResponseCodeEnum.getResultCode();
        this.msg = misResponseCodeEnum.getResultDesc();
    }

    /**
     * 使用枚举类，设置返回信息
     * 
     * @author null
     * @date 2016年5月27日 上午11:35:05
     * 
     * @param misResponseCodeEnum
     */
    public void setResponse(MisResponseCodeEnum misResponseCodeEnum) {
        this.success = misResponseCodeEnum.isStatus();
        this.code = misResponseCodeEnum.getResultCode();
        this.msg = misResponseCodeEnum.getResultDesc();
    }

    /**
     * 使用responseData，设置返回信息
     * 
     * @author null
     * @date 2016年5月27日 上午11:36:51
     * 
     * @param responseData
     */
    public void setResponse(ResponseData<?> responseData) {
        this.success = responseData.isSuccess();
        this.code = responseData.getCode();
        this.msg = responseData.getMsg();
    }

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

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
