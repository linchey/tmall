package com.lin.toymall.web.VO;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/*注册VO*/
public class RegisterVO implements Serializable {
    @NotEmpty(message="用户名不能为空！")
    private String username;
    @NotEmpty(message="密码不能为空！")
    private  String password;
    @NotEmpty(message="确认密码不能为空！")
    private  String checkPassword;
    @NotEmpty(message="验证码不能为空！")
    private  String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}
