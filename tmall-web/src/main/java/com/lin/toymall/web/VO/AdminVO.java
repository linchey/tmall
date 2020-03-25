package com.lin.toymall.web.VO;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class AdminVO implements Serializable {

    private String userName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createTime;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate modifyTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
