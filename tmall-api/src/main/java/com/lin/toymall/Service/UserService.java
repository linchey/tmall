package com.lin.toymall.Service;

import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
    void addUser(UmsMember umsMember);

    UmsMember findUserByName(String username);

    void resetPassword(String password, String username);

    void modifyInfo(UmsMember umsMember);
}
