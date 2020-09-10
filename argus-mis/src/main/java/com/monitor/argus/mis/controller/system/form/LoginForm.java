package com.monitor.argus.mis.controller.system.form;

import java.io.Serializable;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
public class LoginForm implements Serializable{
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
