package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.PmsProductImage;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductImageMapper extends Mapper<PmsProductImage> {
    List<PmsProductImage> selectByProId(@Param( "productId" ) String productId);
}
