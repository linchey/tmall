package com.lin.toymall.service.mapper;

import com.lin.toymall.bean.PmsCatalog3;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface PmsCatalog3Mapper extends Mapper<PmsCatalog3> {
    List<PmsCatalog3> selectCatalogPage(HashMap<String, Object> map);

    int selectCatalogCount();


}
