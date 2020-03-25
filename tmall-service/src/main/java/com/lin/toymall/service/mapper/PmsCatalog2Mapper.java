package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.PmsCatalog2;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface PmsCatalog2Mapper extends Mapper<PmsCatalog2> {
    int selectCatalogCount();

    List<PmsCatalog2> selectCatalogPage(HashMap<String, Object> map);

    String selectName(@Param( "catalog2Id" )String catalog2Id);

    String selectIdByname(@Param( "catalog2Name")String catalog2Name);
}
