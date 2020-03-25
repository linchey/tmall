package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.UmsMemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserAddressMapper extends Mapper<UmsMemberReceiveAddress> {
    List<UmsMemberReceiveAddress> selectByUserId(String id);
    void insertAddress( UmsMemberReceiveAddress userAddress);

    void deleteByid(String id);

    UmsMemberReceiveAddress selectAddressById(String id);

    void updateAddressById(UmsMemberReceiveAddress address);
}
