package com.monitor.argus.common.enums;

/**
 * 返回码从左向右：
 * <p>
 * 第 1 位为状态代码： 0-成功; 1~8-业务错误; 9-系统异常;
 * <p>
 * 第2~3位为业务代码： 01-产品; 02-用户; 03-订单...99-系统
 * <p>
 * 第4~5位为业务详细信息代码
 * 
 * @author null
 * @date 2016年5月13日 下午2:16:49
 * 
 */
public enum MisResponseCodeEnum {
    SUCCESS("0-00-00", "成功", true),

    PRODUCT_NOT_EXIST("1-01-01", "产品ID对应的产品信息不存在", false),

    PRODUCT_DISTRIBUTED_AMOUNT_SAVED_ERROR("1-01-02", "产品已上架数量保存出错", false),

    PRODUCT_SUB_SAVED_ERROR("1-01-03", "产品上架信息保存出错", false),

    PRODUCT_DISTRIBUTABLE_AMOUNT_ERROR("1-01-04", "增加的上架数大于产品可上架的数量", false),

    DISTRIBUTAED_AMOUNT_LESS_THAN_SOLD_AND_FROZEN("1-01-05", "上架数量小于产品已售数量", false),

    PRODUCT_SUB_START_BEFORE_END("1-01-06", "上架结束时间不能早于上架开始时间！", false),

    PRODUCT_SUB_CONFLICT("1-01-07", "该时间段已经有其他同类产品在上架！", false),

    PRODUCT_SUB_NOT_EXIST("1-01-08", "产品上架信息不存在", false),

    UNLOGIN_LOGIN_AGAIN("1-99-01", "您未登录或登录超时，请进入【首页】重新登录！", false),

    USERNAME_OR_PSW_WRONG("1-99-02", "用户名或密码错误，请重新输入！", false),

    SMS_CHECKCODE_WRONG("1-99-03", "短信验证码错误，请重新输入！", false),

    SMS_CHECKCODE_OUT_OF_DATA("1-99-04", "短信验证码已过期，请重新获取验证码！", false),

    NO_BUNDING_MOBILE("1-99-05", "您的帐号还未绑定手机号，请联系管理员！", false),

    RESET_PSW_OLD_PSW_IS_WRONG("1-99-06", "原密码输入错误，请重新输入！", false),

    RESET_PSW_NEW_PSW_IS_EMPTY("1-99-07", "新密码不能为空！", false),

    LOCAL_EXCEPTION("9-00-00", "系统异常，请联系管理员！", false);

    private String resultCode;
    private String resultDesc;
    private boolean status;

    MisResponseCodeEnum(String resultCode, String resultDesc, boolean status) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.status = status;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
