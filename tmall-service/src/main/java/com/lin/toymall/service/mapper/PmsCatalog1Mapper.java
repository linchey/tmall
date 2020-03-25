package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.PmsCatalog1;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface PmsCatalog1Mapper extends Mapper<PmsCatalog1> {
   
     List<PmsCatalog1> selectCatalogPage(HashMap<String,Object> map);

    int selectCatalogCount();

    String selectNameById(@Param( "catalog1Id" ) String catalog1Id);

    String selectIdBycCatalog1Name(@Param( "catalog1Name" )String catalog1Name);
}
