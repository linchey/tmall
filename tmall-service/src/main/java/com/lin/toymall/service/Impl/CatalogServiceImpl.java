package com.lin.toymall.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.CatalogService;
import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.PmsCatalog1;
import com.lin.toymall.bean.PmsCatalog2;
import com.lin.toymall.bean.PmsCatalog3;
import com.lin.toymall.service.mapper.PmsCatalog1Mapper;
import com.lin.toymall.service.mapper.PmsCatalog2Mapper;
import com.lin.toymall.service.mapper.PmsCatalog3Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Service

public class CatalogServiceImpl implements CatalogService {
    @Autowired
    PmsCatalog1Mapper catalog1Mapper;
    @Autowired
    PmsCatalog2Mapper catalog2Mapper;
    @Autowired
    PmsCatalog3Mapper catalog3Mapper;
    @Override
    public List<PmsCatalog1> getCatalog1() {
        List<PmsCatalog1> pmsCatalog1List=catalog1Mapper.selectAll();
        return pmsCatalog1List;
    }

    @Override
    public List<PmsCatalog2> getCatalog2(String catalog1Id) {
        PmsCatalog2 catalog2=new PmsCatalog2();
        catalog2.setCatalog1Id(catalog1Id);
        List<PmsCatalog2> pmsCatalog2List=catalog2Mapper.select(catalog2);
        return pmsCatalog2List;
    }

    @Override
    public List<PmsCatalog3> getCatalog3(String catalog2Id) {
        PmsCatalog3 catalog3=new PmsCatalog3();
        catalog3.setCatalog2Id(catalog2Id);
        List<PmsCatalog3> pmsCatalog3List=catalog3Mapper.select(catalog3);
        return pmsCatalog3List;
    }

    @Override
    public List<PmsCatalog2> getCatalog2() {
        List<PmsCatalog2> pmsCatalog2List=catalog2Mapper.selectAll();
        return pmsCatalog2List;
    }

    @Override
    public List<PmsCatalog3> getCatalog3() {
        List<PmsCatalog3> pmsCatalog3List=catalog3Mapper.selectAll();
        return pmsCatalog3List;
    }
    @Override
    public PageBean wrapPage(int totalCount, int currentPage) {
        PageBean pageBean = new PageBean();
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
    public PageBean<PmsCatalog1> findByCatalog1Page(int currentPage) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        int totalCount=0;
        try{totalCount=catalog1Mapper.selectCatalogCount();}catch (Exception e){
            e.printStackTrace();
        }

        PageBean pageBean=wrapPage(totalCount,currentPage);
        map.put("start",(currentPage-1)*pageBean.getPageSize());
        map.put("size",pageBean.getPageSize());
        List<PmsCatalog1> pmsCatalog1s=null;
        try{       pmsCatalog1s=catalog1Mapper.selectCatalogPage(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        pageBean.setLists(pmsCatalog1s);
        return  pageBean;
    }

    @Override
    public PageBean<PmsCatalog2> findByCatalog2Page(int currentPage) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        int totalCount=0;
        try{totalCount=catalog2Mapper.selectCatalogCount();}catch (Exception e){
            e.printStackTrace();
        }

        PageBean pageBean=wrapPage(totalCount,currentPage);
        map.put("start",(currentPage-1)*pageBean.getPageSize());
        map.put("size",pageBean.getPageSize());
        List<PmsCatalog2> pmsCatalog2s=null;
        try{       pmsCatalog2s=catalog2Mapper.selectCatalogPage(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        pageBean.setLists(pmsCatalog2s);
        return  pageBean;
    }

    @Override
    public PageBean<PmsCatalog3> findByCatalog3Page(int currentPage) {
        HashMap<String,Object> map = new HashMap<>();
        int totalCount=  catalog3Mapper.selectCatalogCount();
        PageBean pageBean=wrapPage(totalCount,currentPage);
        map.put("start",(currentPage-1)*pageBean.getPageSize());
        map.put("size",pageBean.getPageSize());
        List<PmsCatalog3> pmsCatalog3s=catalog3Mapper.selectCatalogPage(map);
        pageBean.setLists(pmsCatalog3s);
        return  pageBean;
    }

    @Override
    public void saveCatalog3(PmsCatalog3 catalog3) {
        try{
        catalog3Mapper.insert(catalog3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delCatalog3(String catalog3Id) {
        try {
            catalog3Mapper.deleteByPrimaryKey( catalog3Id );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String findNameByCatlog2Id( String catalog2Id) {
        return catalog2Mapper.selectName(catalog2Id);
    }

    @Override
    public void modifyCatalog3(PmsCatalog3 catalog3) {
        try {
            catalog3Mapper.updateByPrimaryKey(catalog3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNoExist(String catalog2Id) {
        PmsCatalog2 catalog2=catalog2Mapper.selectByPrimaryKey(catalog2Id);
        if(catalog2!=null)return false;
        return true;
    }

    @Override
    public String findIdByName(String catalog2Name) {
        return catalog2Mapper.selectIdByname(catalog2Name);
    }

    @Override
    public String findNameByCatlog1Id(String catalog1Id) {
        return catalog1Mapper.selectNameById(catalog1Id);
    }

    @Override
    public void delCatalog2(String catalog2Id) {
         catalog2Mapper.deleteByPrimaryKey(catalog2Id);
    }

    @Override
    public String findIdByCatalog1Name(String catalog1Name) {
        return catalog1Mapper.selectIdBycCatalog1Name(catalog1Name);
    }

    @Override
    public void modifyCatalog2(PmsCatalog2 catalog2) {
        catalog2Mapper.updateByPrimaryKey( catalog2);
    }

    @Override
    public void saveCatalog2(PmsCatalog2 catalog2) {
        catalog2Mapper.insert( catalog2 );
    }

    @Override
    public boolean isNoExistCatalog1(String catalog1Id) {
        PmsCatalog1 catalog1=catalog1Mapper.selectByPrimaryKey(catalog1Id);
        if(catalog1!=null)return false;
        return true;
    }

    @Override
    public void saveCatalog1(PmsCatalog1 catalog1) {
        catalog1Mapper.insert( catalog1 );
    }

    @Override
    public void modifyCatalog1(PmsCatalog1 catalog1) {
        catalog1Mapper.updateByPrimaryKey( catalog1 );
    }

    @Override
    public void delCatalog1(String catalog1Id) {
        catalog1Mapper.deleteByPrimaryKey( catalog1Id );
    }

    @Override
    public String findNameByCatlog3Id(String catalog3Id) {
        PmsCatalog3 catalog3=catalog3Mapper.selectByPrimaryKey( catalog3Id );
        String name=catalog3.getName();
        return name;
    }
}
