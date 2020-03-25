package com.lin.toymall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

public class UmsMember implements Serializable {

    @Id
    private String id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String nickname;
    @Column
    private String phone;
    @Column
    private int status;
    @Column
    private LocalDate createTime;
    @Column
    private String icon;
    @Column
    private int  gender;
    @Column
    private LocalDate birthday;
    @Column
    private String city;
    @Column
    private String job;
    @Column
    private String signature;
    @Column
    public String getId() {
        return id;
    }
    @Column
    private LocalDate modifyTime;
    @Column
    private String email;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setModifyTime(LocalDate modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDate getModifyTime() {
        return modifyTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
