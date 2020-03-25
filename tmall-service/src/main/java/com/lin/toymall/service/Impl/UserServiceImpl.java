package com.lin.toymall.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.UserService;
import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.bean.UmsMemberReceiveAddress;
import com.lin.toymall.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers=new ArrayList<>();
        umsMembers=userMapper.selectAll();
        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        return null;
    }

    @Override
    public void addUser(UmsMember umsMember) {
        userMapper.insertUser(umsMember);
    }

    @Override
    public UmsMember findUserByName(String username) {
        UmsMember umsMember=userMapper.selectByName(username);
        return umsMember;
    }

    @Override
    public void resetPassword(String password, String username) {
        userMapper.updateUserPassword(password,username);
    }

    @Override
    public void modifyInfo(UmsMember umsMember) {
        String id=userMapper.selectIdByName(umsMember.getUsername());
        umsMember.setId(id);
        userMapper.updateUserInfo(umsMember);
    }
}
