package com.lin.toymall.Service;


import com.lin.toymall.bean.Admin;

public interface AdminService {

    Admin findAdminByName(String userName);

    void resetPassword(String encryNewPassword, String username);
}
