package com.lin.toymall.web.Controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.CatalogService;
import com.lin.toymall.bean.PmsCatalog1;
import com.lin.toymall.bean.PmsCatalog2;
import com.lin.toymall.bean.PmsCatalog3;
import com.lin.toymall.web.Util.ResultBase;
import com.lin.toymall.web.Util.Status;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*分类*/
@RestController
public class CatalogController {
    @Reference
    CatalogService catalogService;
    //获取分类视图
    @GetMapping("getCatlogView1")
    public ModelAndView getCatlog1(ModelAndView mv) {
        List<PmsCatalog1> catalog1List=catalogService.getCatalog1();
        mv.addObject( "catalog1List", catalog1List);
        mv.setViewName( "admin/product/catalog1" );
        return mv;
    }
    @GetMapping("getCatlogView2")
    public ModelAndView getCatlogView2(ModelAndView mv) {
        List<PmsCatalog2> catalog2List = catalogService.getCatalog2();
        for (PmsCatalog2 catalog2:catalog2List) {
            catalog2.setCatalog1Name( catalogService.findNameByCatlog1Id(catalog2.getCatalog1Id()) );
        }
        List<PmsCatalog1> catalog1List = catalogService.getCatalog1();
        mv.addObject( "catalog1List" ,catalog1List);
        mv.addObject( "catalog2List",catalog2List );
        mv.setViewName( "admin/product/catalog2" );
        return mv;
    }
    @GetMapping("getCatlogView3")
    public ModelAndView getCatlogView3(ModelAndView mv) {
        List<PmsCatalog3> catalog3List = catalogService.getCatalog3();
        for (PmsCatalog3 catalog3:catalog3List) {
            catalog3.setCatalog2Name( catalogService.findNameByCatlog2Id(catalog3.getCatalog2Id()) );
        }
        mv.addObject( "catalog3List",catalog3List );
        List<PmsCatalog2> catalog2List = catalogService.getCatalog2();
        mv.addObject( "catalog2List" ,catalog2List);
        mv.setViewName( "admin/product/catalog3" );
        return mv;
    }
    @PostMapping("/catalog3/add/")
    @ResponseBody
    public  String addCatolog3(PmsCatalog3 catalog3){
        catalogService.saveCatalog3(catalog3);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @GetMapping("/catalog3/delete/{catalog3Id}")
    @ResponseBody
    public  String delCatolog3(@PathVariable String catalog3Id){
        catalogService.delCatalog3(catalog3Id);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/catalog3/modify/")
    @ResponseBody
    public  String modifyCatolog3( PmsCatalog3 catalog3){
        String catalog2Name=catalog3.getCatalog2Name();
        String catalog2Id=catalogService.findIdByName(catalog2Name);
        catalog3.setCatalog2Id( catalog2Id );
        ResultBase result=new ResultBase();
        if(catalogService.isNoExist(catalog2Id)){
            result.setStatus( Status.FAIL );
            result.setMessage("上级分类不存在");
            return JSON.toJSONString(result);
        }
        catalogService.modifyCatalog3(catalog3);
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @GetMapping("/catalog2/delete/{catalog2Id}")
    @ResponseBody
    public  String delCatolog2(@PathVariable String catalog2Id){
        catalogService.delCatalog2(catalog2Id);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/catalog2/modify/")
    @ResponseBody
    public  String modifyCatolog2( PmsCatalog2 catalog2){
        String catalog1Name=catalog2.getCatalog1Name();
        String catalog1Id=catalogService.findIdByCatalog1Name(catalog1Name);
        catalog2.setCatalog1Id( catalog1Id );
        ResultBase result=new ResultBase();
        if(catalogService.isNoExistCatalog1(catalog1Id)){
            result.setStatus( Status.FAIL );
            result.setMessage("上级分类不存在");
            return JSON.toJSONString(result);
        }
        catalogService.modifyCatalog2(catalog2);
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/catalog2/add/")
    @ResponseBody
    public  String addCatolog2(PmsCatalog2 catalog2){
        catalogService.saveCatalog2(catalog2);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/catalog1/add/")
    @ResponseBody
    public  String addCatolog1(PmsCatalog1 catalog1){
        catalogService.saveCatalog1(catalog1);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @PostMapping("/catalog1/modify/")
    @ResponseBody
    public  String modifyCatolog1( PmsCatalog1 catalog1){
        ResultBase result=new ResultBase();
        catalogService.modifyCatalog1(catalog1);
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @GetMapping("/catalog1/delete/{catalog1Id}")
    @ResponseBody
    public  String delCatolog12(@PathVariable String catalog1Id){
        catalogService.delCatalog1(catalog1Id);
        ResultBase result=new ResultBase();
        result.setStatus( Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    @GetMapping("/catalog/getCatalog1")
    @ResponseBody
    public  List<PmsCatalog1>getCatalog1(){
        List<PmsCatalog1> catalog1List=catalogService.getCatalog1();
        return  catalog1List;
    }
    @GetMapping("/catalog/getCatalog2")
    @ResponseBody
    public  List<PmsCatalog2>getCatalog2(String catalog1Id){
        List<PmsCatalog2> catalog2List=catalogService.getCatalog2(catalog1Id);
        return  catalog2List;
    }
    @GetMapping("/catalog/getCatalog3")
    @ResponseBody
    public  List<PmsCatalog3>getCatalog3(String catalog2Id){
        List<PmsCatalog3> catalog3List=catalogService.getCatalog3(catalog2Id);
        return  catalog3List;
    }
}
