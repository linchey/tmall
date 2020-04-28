package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.OrderService;
import com.lin.toymall.annotations.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PayController {
    @Reference
    OrderService orderService;
    @GetMapping ("toPay")
    public ModelAndView toPay(String  orderId){
        ModelAndView mv=new ModelAndView("user/pay/pay");
        mv.addObject( "orderId",orderId );
       // System.out.println(orderId);
        return mv;
    }
    @GetMapping("paying")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String paying(String  orderId, HttpServletRequest request){
        String memberId=(String)request.getAttribute( "memberId" ) ;
        orderService.modifyOrder(orderId,memberId);
        return "success";
    }

}
