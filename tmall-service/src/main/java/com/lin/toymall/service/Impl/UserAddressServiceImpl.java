package com.lin.toymall.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.UserAddressService;
import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.UmsMemberReceiveAddress;
import com.lin.toymall.service.mapper.UserAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.print.Book;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    UserAddressMapper userAddressMapper;
    @Override
    public List<UmsMemberReceiveAddress> findByUserId(String id) {
        List<UmsMemberReceiveAddress> addressList=userAddressMapper.selectByUserId(id);
        return addressList;
    }

    @Override
    public void saveAddress(UmsMemberReceiveAddress userAddress) {
        userAddressMapper.insertAddress(userAddress);
    }

    @Override
    public PageBean wrapPage(int totalCount, int currentPage) {
        PageBean<Book> pageBean = new PageBean<Book>();
        int pageSize=10;
        //设置当前页数
        pageBean.setCurrPage(currentPage);
        //设置每页显示的数量
        pageBean.setPageSize(pageSize);
        //结果总数
        pageBean.setTotalCount(totalCount);
        //总页数
        double surplus=totalCount%pageSize;
        //刚好到一页的最大值不扩容
        double totalPage=(surplus>0?Math.floor(totalCount/pageSize)+1:Math.floor(totalCount/pageSize));

        pageBean.setTotalPage((int)totalPage);

        return pageBean;
    }



    @Override
    public UmsMemberReceiveAddress getAddressById(String addressId) {
        UmsMemberReceiveAddress address= userAddressMapper.selectAddressById(addressId);
        return address;
    }

    @Override
    public void modifyAddress(UmsMemberReceiveAddress address) {
         userAddressMapper.updateAddressById(address);
    }

    @Override
    public void deleteAddressById(String id) {
        userAddressMapper.deleteByid(id);
    }
}
