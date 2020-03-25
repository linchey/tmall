package com.lin.toymall.service.mapper;


import com.lin.toymall.bean.Admin;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;


public interface AdminMapper extends Mapper<Admin> {
    public  Admin selectByName(String username);
    public  void updateKey(@Param("username") String username, @Param("password") String password);
}
