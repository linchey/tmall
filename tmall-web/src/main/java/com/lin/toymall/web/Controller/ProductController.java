package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.web.Util.UploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
}
