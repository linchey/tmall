package com.lin.toymall.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.PmsProduct;
import com.lin.toymall.bean.PmsProductImage;
import com.lin.toymall.service.mapper.ProductImageMapper;
import com.lin.toymall.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductImageMapper productImageMapper;
    @Override
    public void addProduct(PmsProduct product) {
        productMapper.insert( product );
    }

    @Override
    public String findIdByProName(String productName) {
        return productMapper.selectIdByName(productName);
    }

    @Override
    public void addProductImag(PmsProductImage productImage) {
        productImageMapper.insert( productImage );
    }

    @Override
    public boolean isExitProduct(String productName) {
        String id=findIdByProName( productName );
        if(id!=null)return true;
        return false;
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
    public void modifyProName(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setProductName( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }

    @Override
    public void modifyProPrice(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setPrice( Double.valueOf(  newValue) );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);

    }

    @Override
    public void modifyProStock(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setStock( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }

    @Override
    public void modifyProNote(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setNote( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }

    @Override
    public void delProduct(String id) {
        productMapper.deleteByPrimaryKey( id );
    }

    @Override
    public List<PmsProduct> qurProduct(String catalog1Id, String catalog2Id, String catalog3Id) {

        return productMapper.selectProByCatalog(catalog1Id,catalog2Id,catalog3Id);
    }

    @Override
    public PageBean<PmsProduct> findProductByPage(int currPage) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        int totalCount=productMapper.selectProductCount();
        PageBean pageBean=wrapPage(totalCount,currPage);
        map.put("start",(currPage-1)*pageBean.getPageSize());
        map.put("size",pageBean.getPageSize());
        List<PmsProduct> pmsProduct=productMapper.selectProductPage(map);
        pageBean.setLists(pmsProduct);
        return  pageBean;
    }
}
