package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.bean.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController {
    @Reference
    ProductService productService;
    //商品详情
    @RequestMapping("{productId}.html")
    public ModelAndView item(@PathVariable("productId") String productId, ModelAndView mv){
        PmsProduct product=productService.findProductById( productId );
        PmsCatalog1 catalog1=productService.findCatalog1ById(product.getCatalog1Id());
        PmsCatalog2 catalog2=productService.findCatalog2ById(product.getCatalog2Id());
        PmsCatalog3 catalog3=productService.findCatalog3ById(product.getCatalog3Id());

        List<PmsProductImage> productImages=productService.findImgeByProId( productId );
        product.setPmsProductImageList(productImages);
        mv.addObject( "catalog1",catalog1 );
        mv.addObject( "catalog2",catalog2 );
        mv.addObject( "catalog3",catalog3 );
        mv.addObject( "productDetail",product );
        mv.setViewName("user/productDetail");
        return mv;
    }
}
