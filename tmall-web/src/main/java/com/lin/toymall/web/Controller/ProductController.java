package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.PmsProduct;
import com.lin.toymall.bean.PmsProductImage;
import com.lin.toymall.web.Util.ProductField;
import com.lin.toymall.web.Util.ResultBase;
import com.lin.toymall.web.Util.Status;
import com.lin.toymall.web.Util.UploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {
    @Reference
    ProductService productService;
    /**
     * 商品添加视图
     * */
    @GetMapping("/productPage")
    public ModelAndView addProductPage(ModelAndView mv,@RequestParam(value = "currPage",required = false,defaultValue = "1") int currPage){
       PageBean<PmsProduct>productPageBean=productService.findProductByPage(currPage);
       mv.addObject( "productList",productPageBean );
        mv.setViewName("admin/product/addProductPage");
        return mv;
    }
    /**
     * 商品查询页面
     * */
    @GetMapping("/qurProductPage")
    public ModelAndView qurProductPage(ModelAndView mv){
        mv.setViewName( "admin/product/qurProductPage" );
        return mv;
    }

    /***
     * 图片上传功能
     */
    @PostMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imgUrl= UploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }
    /**
     * 添加商品信息
     * */
    @PostMapping("/addProduct")
    @ResponseBody
    public  String addProduct( @RequestBody PmsProduct product){
        String defaultImg=product.getProductImages().get( 0 );
        product.setDefaultImg( defaultImg );
        String productName=product.getProductName();
        ResultBase result=new ResultBase();
        if(productService.isExitProduct(productName)){
            result.setStatus( Status.FAIL);
            result.setMessage("失败");
            return JSON.toJSONString(result);
        }
        productService.addProduct(product);
        String productId=productService.findIdByProName(productName);
        PmsProductImage productImage=new PmsProductImage();
        productImage.setProductId(productId);
        List<String> productImages=product.getProductImages();
        for(String productUrl:productImages){
            productImage.setImgUrl(productUrl);
            productService.addProductImag(productImage);
        }
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("modify")
    @ResponseBody
    public  String modifyProduct(@RequestParam(value = "id") String id,@RequestParam(value = "field")String field,@RequestParam(value = "newValue")String newValue){
        ResultBase result=new ResultBase();
        if (ProductField.PRODUCTNAME.equals( field )){
            productService.modifyProName(id,newValue);
            result.setStatus( Status.SUCCESS);
            result.setMessage("成功");
            return JSON.toJSONString(result);
        }
        if( ProductField.PRICE.equals( field )){
            productService.modifyProPrice(id,newValue);
            result.setStatus( Status.SUCCESS);
            result.setMessage("成功");
            return JSON.toJSONString(result);
        }
        if( ProductField.STOCK.equals( field )){
            productService.modifyProStock(id,newValue);
            result.setStatus( Status.SUCCESS);
            result.setMessage("成功");
            return JSON.toJSONString(result);
        }
        if(ProductField.NOTE.equals( field )){
            productService.modifyProNote(id,newValue);
            result.setStatus( Status.SUCCESS);
            result.setMessage("成功");
            return JSON.toJSONString(result);
        }
        result.setStatus( Status.FAIL);
        result.setMessage("失败");
        return JSON.toJSONString(result);
    }
    @PostMapping("delete/{id}")
    @ResponseBody
    public String delProduct(@PathVariable("id") String id){
        ResultBase result=new ResultBase();
        productService.delProduct(id);
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/query")
    @ResponseBody
    public  List<PmsProduct> queryProduct(@RequestParam("catalog1Id")String catalog1Id,@RequestParam("catalog2Id")String catalog2Id,@RequestParam("catalog3Id")String catalog3Id){
        List<PmsProduct> products=productService.qurProduct(catalog1Id,catalog2Id,catalog3Id);
        return products;
    }
}
