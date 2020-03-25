package com.lin.toymall.web.VO;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/*重置密码VO*/
public class ResetKeyVO implements Serializable {
    @NotEmpty(message="原密码不能为空！")
    private  String oldPassword;
    @NotEmpty(message="新密码不能为空！")
    private  String newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}
