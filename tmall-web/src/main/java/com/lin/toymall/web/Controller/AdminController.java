package com.lin.toymall.web.Controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.AdminService;
import com.lin.toymall.Service.UserService;
import com.lin.toymall.bean.Admin;
import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.web.Util.EncryptHelper;
import com.lin.toymall.web.Util.EncryptType;
import com.lin.toymall.web.VO.LoginVO;
import com.lin.toymall.web.VO.ResetPasswordVO;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
   @Reference
   AdminService adminService;
   @Reference
    UserService userService;
   @GetMapping("hello")
   public ModelAndView hello(ModelAndView mv){
       mv.setViewName("admin/hello");
       return mv;
   }

    @GetMapping("/index")//等同于（@RequestMapping(value="login" method="GET")）
    public ModelAndView  index(ModelAndView mv){
        mv.setViewName("admin/index");
        return mv;
    }
    /*@GetMapping("home")//等同于（@RequestMapping(value="login" method="GET")）
    public ModelAndView  hello(ModelAndView mv){
        mv.setViewName("admin/home");
        return mv;
    }*/
   @GetMapping("login")//等同于（@RequestMapping(value="login" method="GET")）
   public ModelAndView  adminPage(ModelAndView mv){
       mv.setViewName("admin/login");
      return mv;
   }

   @GetMapping("logout")
   public ModelAndView logout(ModelAndView mv, HttpServletRequest request){
       HttpSession session=request.getSession();
       session.removeAttribute("user");
       session.setMaxInactiveInterval(0);
       mv.setViewName("redirect:./index");
       return mv;
   }
   @GetMapping("/home")
   public ModelAndView toHome(ModelAndView mv,HttpServletRequest request){
       HttpSession session=request.getSession();
       Admin admin= (Admin) session.getAttribute("user");
       if(admin==null){
           mv.setViewName("redirect:login");
           return mv;
       }
       mv.setViewName("admin/home");
       return  mv;
   }
   @PostMapping ("login")
   public ModelAndView loginCheck(HttpServletRequest request, @Valid LoginVO loginVO, Errors errors){
      HttpSession session=request.getSession();
      ModelAndView mv=new ModelAndView();

      if(errors.hasErrors()){
         mv.addObject("logiTips",errors.getFieldError().getDefaultMessage());
         mv.setViewName("admin/login");
         return mv;
      }
       String userName=loginVO.getUsername().trim();
       String password=loginVO.getPassword().trim();

       Admin adminMember=adminService.findAdminByName(userName);
       //用户不存在
       if(adminMember==null){
           mv.addObject("logiTips","用户不存在");
           mv.setViewName("admin/login");
           return mv;
       }
       //密码错误
       //密码加密
       final String Encrypassword= EncryptHelper.encrypt(password, EncryptType.MD5);
       if(!adminMember.getPassword().equals(Encrypassword)){
          mv.addObject("logiTips","用户名或密码错误!");
          mv.setViewName("admin/login");
          return  mv;
       }
       session.setAttribute("user",adminMember);
       mv.setViewName("redirect:home");
       return mv;
   }
    @GetMapping("myInfo")
    public  ModelAndView myInfo(ModelAndView mv,HttpServletRequest request){
        HttpSession session=request.getSession();
        Admin admin=(Admin)session.getAttribute("user");
        if(admin==null){
            mv.setViewName("admin/login");
            return mv;
        }
        mv.setViewName("admin/myInfo");
        return  mv;
    }
    /*修改密码*/
    @GetMapping("resetPanel")
    public ModelAndView resetPassordPage(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("admin/resetPanel");
        return mv;
    }
    @PostMapping("resetPassord")
    public ModelAndView resetPassord(HttpServletRequest request, @Valid ResetPasswordVO ResetPasswordVO, Errors errors){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        Admin admin=(Admin) session.getAttribute("user");
        if(admin==null){
            mv.setViewName("admin/login");
            return mv;
        }
        String username=admin.getUsername();
        String password=admin.getPassword();
        String oldPassword=ResetPasswordVO.getOldPassword().trim();
        String newPassword=ResetPasswordVO.getNewPassword().trim();
        String checkPassword=ResetPasswordVO.getCheckPassword().trim();
        //原密码或新密码为空
        if(errors.hasErrors()){
            mv.addObject("resetTip",errors.getFieldError().getDefaultMessage());
            mv.setViewName("admin/resetPanel");
            return mv;
        }
        //原密码错误
        final String Encrypassword= EncryptHelper.encrypt(oldPassword, EncryptType.MD5);
        if(!password.equals(Encrypassword)){
            mv.addObject("resetTip","原密码错误");
            mv.setViewName("admin/resetPanel");
            return mv;
        }
        //输入的两次密码不一致
        if(!newPassword.equals(checkPassword)){
            mv.addObject("resetTip","两次密码不一致,重新输入");
            mv.setViewName("admin/resetPanel");
            return mv;
        }
        //密码正确
        session.removeAttribute("user");
        final String EncryNewPassword= EncryptHelper.encrypt(newPassword, EncryptType.MD5);
        adminService.resetPassword(EncryNewPassword,username);
        mv.setViewName("admin/login");
        return mv;
    }
    /*用户个人信息*/
    @GetMapping("myInformation")
    public ModelAndView myInfomation(ModelAndView mv,HttpServletRequest request){
        HttpSession session=request.getSession();
        Admin admin=(Admin) session.getAttribute("user");
        if(admin==null){
            return null;
        }
        mv.setViewName("admin/myInfo");
        return  mv;
    }
    /*用户列表*/
    @GetMapping("userList")
    public ModelAndView userList(ModelAndView mv){
        List<UmsMember>umsMembers=userService.getAllUser();
        mv.addObject("userList",umsMembers);
        mv.setViewName("admin/userList");
        return mv;
    }
    /*添加用户*/
    /*分类管理*/
/*    @GetMapping("catalogManage")
    public ModelAndView catalogManage(ModelAndView mv){
        mv.setViewName("admin/product/catalog");
        return mv;
    }*/
}

