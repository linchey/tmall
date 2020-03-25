package com.lin.toymall.Service;

import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserAddressService {
    List<UmsMemberReceiveAddress> findByUserId(String id);
    void saveAddress(UmsMemberReceiveAddress userAddress);
    public PageBean wrapPage(int totalCount, int currentPage);
    void deleteAddressById(String id);

    UmsMemberReceiveAddress getAddressById(String addressId);

    void modifyAddress(UmsMemberReceiveAddress address);
}
