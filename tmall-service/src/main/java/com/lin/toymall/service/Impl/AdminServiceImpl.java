package com.lin.toymall.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.AdminService;
import com.lin.toymall.bean.Admin;
import com.lin.toymall.service.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public Admin findAdminByName(String username) {


        Admin admin= adminMapper.selectByName(username);

        return admin;
    }

    @Override
    public void resetPassword(String encryNewPassword, String username) {
        adminMapper.updateKey(username,encryNewPassword);
    }
}
