package com.lin.toymall.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.bean.*;
import com.lin.toymall.service.mapper.*;
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
    @Autowired
    PmsCatalog1Mapper catalog1Mapper;
    @Autowired
    PmsCatalog2Mapper catalog2Mapper;
    @Autowired
    PmsCatalog3Mapper catalog3Mapper;
    /*添加商品*/
    @Override
    public void addProduct(PmsProduct product) {
        productMapper.insert( product );
    }
    //通过名字查找商品id
    @Override
    public String findIdByProName(String productName) {
        return productMapper.selectIdByName(productName);
    }
    //添加商品图片
    @Override
    public void addProductImag(PmsProductImage productImage) {
        productImageMapper.insert( productImage );
    }
    //判断商品是否存在
    @Override
    public boolean isExitProduct(String productName) {
        String id=findIdByProName( productName );
        if(id!=null)return true;
        return false;
    }
    //封装成页面
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
//修改商品名字
    @Override
    public void modifyProName(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setProductName( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }
//修改商品价格
    @Override
    public void modifyProPrice(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setPrice( Double.valueOf(  newValue) );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);

    }
//修改库存
    @Override
    public void modifyProStock(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setStock( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }
//修改商品描述
    @Override
    public void modifyProNote(String id, String newValue) {
        PmsProduct product=new PmsProduct();
        product.setId( id );
        product.setNote( newValue );
        Example example = new Example(PmsProduct.class);
        example.createCriteria().andEqualTo("id", product.getId());
        productMapper.updateByExampleSelective(product,example);
    }
//删除商品
    @Override
    public void delProduct(String id) {
        productMapper.deleteByPrimaryKey( id );
    }
//通过分类级别查询商品
    @Override
    public List<PmsProduct> qurProduct(String catalog1Id, String catalog2Id, String catalog3Id) {

        return productMapper.selectProByCatalog(catalog1Id,catalog2Id,catalog3Id);
    }
//删除商品图片
    @Override
    public void delProductImges(String id) {
        List<PmsProductImage> imgs=findImgeByProId( id );
        if(imgs!=null){
            for(PmsProductImage image:imgs){
                productImageMapper.deleteByPrimaryKey( image.getId() );
            }
        }

    }
//通过商品id查找所有图片
    @Override
    public List<PmsProductImage> findImgeByProId(String productId) {
        List<PmsProductImage>imgs=productImageMapper.selectByProId(productId);
        return imgs;
    }

    @Override
    public PmsProductImage findImageById(String id) {
        return productImageMapper.selectByPrimaryKey( id );
    }

    @Override
    public PmsProduct findProductById(String productId) {
        return productMapper.selectByPrimaryKey( productId );
    }

    @Override
    public void modifyDefImg(PmsProduct product) {
        productMapper.updateByPrimaryKey( product );
    }

    @Override
    public void delImge(String id) {
        productImageMapper.deleteByPrimaryKey( id );
    }

    @Override
    public PmsProduct findProByName(String name) {
        return productMapper.selectBySearch(name);
    }

    @Override
    public PmsCatalog1 findCatalog1ById(String id) {
        return catalog1Mapper.selectByPrimaryKey( id );
    }

    @Override
    public PmsCatalog2 findCatalog2ById(String id) {
        return catalog2Mapper.selectByPrimaryKey( id );
    }

    @Override
    public PmsCatalog3 findCatalog3ById(String id) {
        return catalog3Mapper.selectByPrimaryKey( id );
    }

    //查找第i页的商品
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
