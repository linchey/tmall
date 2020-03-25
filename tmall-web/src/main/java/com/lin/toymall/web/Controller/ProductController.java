package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("product")
public class ProductController {
    @Reference
    ProductService productService;
    @GetMapping("/addProductPage")
    public ModelAndView addProductPage(ModelAndView mv){
        mv.setViewName("admin/product/addProductPage");
        return mv;
    }
}
