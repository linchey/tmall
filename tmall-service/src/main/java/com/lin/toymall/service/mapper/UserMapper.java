package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.UmsMember;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UmsMember> {
    public void insertUser(@Param("umsMember") UmsMember umsMember);
    UmsMember selectByName(String username);
    void updateUserPassword(@Param("password") String password, @Param("username") String username);

    void updateUserInfo(UmsMember umsMember);

    String selectIdByName(String username);
}
