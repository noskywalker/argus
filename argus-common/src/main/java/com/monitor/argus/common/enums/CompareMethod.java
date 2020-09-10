package com.monitor.argus.common.enums;

/**
 * Created by huxiaolei on 2016/10.
 */
public enum CompareMethod {

    LARGER("大于", 1), EQUAL("等于", 0), SMALLER("小于", -1);

    private String name;
    private int code;

    private CompareMethod(String name_, int code_){
        this.name = name_;
        this.code = code_;
    }

    public String getTypeName() {
        return name;
    }

    public int getTypeCode() {
        return code;
    }

    public static CompareMethod methodOf(int code_) {
        for (CompareMethod method : CompareMethod.values()) {
            if (code_ == method.getTypeCode()) {
                return method;
            }
        }
        return EQUAL;
    }

    @Override
    public String toString() {
        return name;
    }

}
