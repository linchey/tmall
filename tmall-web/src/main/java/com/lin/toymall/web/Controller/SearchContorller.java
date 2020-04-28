package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.CatalogService;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.bean.PmsProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchContorller {
    @Reference
    ProductService productService;
    @Reference
    CatalogService catalogService;
    @GetMapping("catalog2")
    public ModelAndView getProBycata2(String catalog2Id){
        ModelAndView mv=new ModelAndView();
        List<PmsProduct> pmsProductList=productService.findProductBycata2( catalog2Id );
        String keyword=catalogService.findNameByCatlog2Id( catalog2Id );
        mv.addObject( "productList" ,pmsProductList);
        mv.addObject( "keyword" ,keyword);
        mv.setViewName("user/Search/list");
        return mv;
    }
    @GetMapping("catalog3")
    public ModelAndView getProBycata3(String catalog3Id){
        ModelAndView mv=new ModelAndView();
        List<PmsProduct> pmsProductList=productService.findProductBycata3( catalog3Id );
        String keyword=catalogService.findNameByCatlog3Id( catalog3Id );
        mv.addObject( "productList" ,pmsProductList);
        mv.addObject( "keyword" ,keyword);
        mv.setViewName("user/Search/list");
        return mv;
    }
    @GetMapping("productName")
    public  ModelAndView getProByName(String search){
        ModelAndView mv=new ModelAndView(  );
        List<PmsProduct> pmsProductList=productService.findProByName( search );
        mv.addObject( "keyword",search );
        mv.addObject(  "productList",pmsProductList);
        mv.setViewName( "user/Search/list");
        return mv;
    }
}
