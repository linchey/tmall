package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.PmsProduct;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface ProductMapper  extends Mapper<PmsProduct> {
    String selectIdByName(@Param( "productName" ) String productName);

    int selectProductCount();

    List<PmsProduct> selectProductPage(HashMap<String, Object> map);

    List<PmsProduct> selectProByCatalog(@Param( "catalog1Id" ) String catalog1Id, @Param( "catalog2Id" )String catalog2Id, @Param( "catalog3Id" )String catalog3Id);

    List<PmsProduct> selectBySearch(@Param( "search" ) String search);
}
