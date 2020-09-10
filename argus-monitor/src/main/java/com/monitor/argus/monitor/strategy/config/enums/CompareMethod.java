package com.monitor.argus.monitor.strategy.config.enums;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
public enum CompareMethod {

    LARGER(1),EQUAL(0),SMALLER(-1);

    private int code;

    private CompareMethod(int code_){
        this.code=code_;
    }

    public int getCode() {
        return code;
    }

    public static CompareMethod methodOf(int code_) {
        for (CompareMethod method : CompareMethod.values()) {
            if (code_ == method.getCode()) {
                return method;
            }
        }
        return EQUAL;
    }
}
