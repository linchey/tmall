package com.lin.toymall.bean;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

public class Admin implements Serializable {
    @Id
    private String id;
    @NotEmpty(message="用户名不能为空！")
    private String username;
    @NotEmpty(message="密码不能为空！")
    private  String password;
    private LocalDate createTime;
    private LocalDate modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDate modifyTime) {
        this.modifyTime = modifyTime;
    }
}
