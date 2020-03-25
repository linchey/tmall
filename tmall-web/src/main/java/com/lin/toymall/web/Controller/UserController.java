package com.lin.toymall.web.Controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.CatalogService;
import com.lin.toymall.Service.UserAddressService;
import com.lin.toymall.Service.UserService;
import com.lin.toymall.bean.*;
import com.lin.toymall.web.Util.EncryptHelper;
import com.lin.toymall.web.Util.EncryptType;
import com.lin.toymall.web.Util.ResultBase;
import com.lin.toymall.web.Util.Status;
import com.lin.toymall.web.Util.validatecode.GraphicHelper;
import com.lin.toymall.web.VO.LoginVO;
import com.lin.toymall.web.VO.RegisterVO;
import com.lin.toymall.web.VO.ResetKeyVO;
import com.lin.toymall.web.VO.UserInfoVO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;
    @Reference
    UserAddressService userAddressService;
    @Reference
    CatalogService catalogService;
    /*测试*/
    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("user/index");
        return mv;
    }
    /*主页*/
    @GetMapping("/home")
    public ModelAndView home(){
        ModelAndView mv=new ModelAndView();
        List<PmsCatalog1> catalog1List = catalogService.getCatalog1();
        for (PmsCatalog1 catalog1:catalog1List) {
            List<PmsCatalog2> catalog2List=catalogService.getCatalog2(catalog1.getId());
            for (PmsCatalog2 catalog2:catalog2List) {
                List<PmsCatalog3> catalog3List=catalogService.getCatalog3(catalog2.getId());
                catalog2.setCatalog3List(catalog3List);
            }
            catalog1.setCatalog2List(catalog2List);
        }
        mv.addObject("catalogList",catalog1List);
        mv.setViewName("user/home");
        return mv;
    }
    /*获取所有用户信息*/
    @GetMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMemberList=new ArrayList<>();
        umsMemberList=userService.getAllUser();
        return umsMemberList;
    }
    /*获取验证码*/
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession();
        response.setContentType("image/jpeg");
        OutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String identityCode = GraphicHelper.create( 4 , false , 175 , 90 , out , 50 );
        session.setAttribute("IDENTITY_CODE",identityCode);    }

    /*用户注册功能*/
    //跳往注册视图
    @GetMapping("/toRegister")
    public ModelAndView toRegister(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("user/registerPanel");
        return mv;
    }
    //注册验证
    @PostMapping("register")
    public ModelAndView register(@Valid RegisterVO registerVO, Errors errors, HttpServletRequest request){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        String username=registerVO.getUsername().trim();
        String password=registerVO.getPassword().trim();
        String checkPassword=registerVO.getCheckPassword();
        String verifyCode=registerVO.getVerifyCode();
        //密码、验证码或用户名为空
        if(errors.hasErrors()){
            mv.addObject("tips",errors.getFieldError().getDefaultMessage());
            mv.setViewName("user/registerPanel");
            return mv;
        }
        UmsMember umsMember=userService.findUserByName(username);
        //用户已存在
        if(umsMember!=null){
            mv.addObject("tips","该用户已经存在");
            mv.setViewName("user/registerPanel");
            return mv;
        }
        //两次密码不一致
        if(!password.equals(checkPassword)){
            mv.addObject("tips","两次密码不一致,重新输入");
            return mv;
        }
        //验证输入的验证码是否和图片一致
        String icode=(String)session.getAttribute("IDENTITY_CODE");//提取图片中的文字
        if(verifyCode.equalsIgnoreCase(icode)==false){
            mv.addObject("tips","验证码输入错误");
            mv.setViewName("user/registerPanel");
            return mv;
        }
        UmsMember user=new UmsMember();
        //密码加密
        final String Encrypassword= EncryptHelper.encrypt(password, EncryptType.MD5);
        user.setUsername(username);
        user.setPassword(Encrypassword);
        //写入数据库
        userService.addUser(user);
        //跳往登录页面
        mv.setViewName("user/registerSuccess");
        return mv;
    }
    /*用户登录功能*/
    //登录页面
    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("user/login");
        return mv;
    }
    //登录验证
    @PostMapping("loginCheck")
    public ModelAndView loginCheck(HttpServletRequest request, @Valid LoginVO loginVO, Errors errors){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        String username=loginVO.getUsername().trim();
        String password=loginVO.getPassword().trim();
        //密码或用户名为空
        if(errors.hasErrors()){
            mv.addObject("checkInfo",errors.getFieldError().getDefaultMessage());
            mv.setViewName("user/login");
            return mv;
        }
        //用户不存在
        UmsMember umsMember=userService.findUserByName(username);
        if(umsMember==null){
            mv.addObject("checkInfo","用户不存在");
            mv.setViewName("user/login");
            return mv;
        }
        //密码加密
        final String Encrypassword= EncryptHelper.encrypt(password, EncryptType.MD5);
        //密码错误
        if(!Encrypassword.equals(umsMember.getPassword())){
            mv.addObject("checkInfo","用户名或密码错误!");
            mv.setViewName("user/login");
            return  mv;
        }
        //密码正确
        session.setAttribute("umsMember",umsMember);
        mv.setViewName("redirect:home");
        return mv;
    }
    /*用户个人信息*/
    @GetMapping("myInformation")
    @ResponseBody
    public ModelAndView myInfomation(HttpServletRequest request){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        UmsMember umsMember=(UmsMember)session.getAttribute("umsMember");
        if(umsMember==null){
          mv.setViewName("user/login");
          return mv;
        }
        UserInfoVO userInfoVO=new UserInfoVO();
        userInfoVO.setUsername(umsMember.getUsername());
        userInfoVO.setPhone(umsMember.getPhone());
        userInfoVO.setBirthday(umsMember.getBirthday());
        userInfoVO.setGender(umsMember.getGender());
        userInfoVO.setCity(umsMember.getCity());
        userInfoVO.setCreateTime(umsMember.getCreateTime());
        userInfoVO.setIcon(umsMember.getIcon());
        userInfoVO.setNickname(umsMember.getNickname());
        userInfoVO.setSignature(umsMember.getSignature());
        userInfoVO.setJob(umsMember.getJob());
        userInfoVO.setEmail(umsMember.getEmail());
        mv.addObject("userInfoVO",userInfoVO);
        mv.setViewName("user/myInformation");
        return mv;
    }
    /*个人信息修改*/
    @GetMapping("toUpdateUser")
    public ModelAndView updateInfo(){
       ModelAndView mv=new ModelAndView() ;

       mv.setViewName("user/modifyInfo");
       return mv;
    }
    @PostMapping("modifyInfo")
    public ModelAndView modifyInfo(UmsMember umsMember, BindingResult bindingResult){
        ModelAndView mv=new ModelAndView();
        userService.modifyInfo(umsMember);
        UserInfoVO userInfoVO=new UserInfoVO();
        userInfoVO.setUsername(umsMember.getUsername());
        userInfoVO.setPhone(umsMember.getPhone());
        userInfoVO.setBirthday(umsMember.getBirthday());
        userInfoVO.setGender(umsMember.getGender());
        userInfoVO.setCity(umsMember.getCity());
        userInfoVO.setCreateTime(umsMember.getCreateTime());
        userInfoVO.setIcon(umsMember.getIcon());
        userInfoVO.setNickname(umsMember.getNickname());
        userInfoVO.setSignature(umsMember.getSignature());
        userInfoVO.setJob(umsMember.getJob());
        userInfoVO.setEmail(umsMember.getEmail());
        mv.addObject("userInfoVO",userInfoVO);
        mv.setViewName("user/myInformation");
        return mv;
    }
    /*修改密码*/
    @GetMapping("resetPanel")
    public ModelAndView resetPassordPage(HttpServletRequest request){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        UmsMember user=(UmsMember)session.getAttribute("umsMember");

        if(user==null){
            mv.setViewName("user/login");
            return mv;
        }
        mv.setViewName("user/resetPanel");
        return mv;
    }
    @PostMapping("resetPassord")
    public ModelAndView resetPassord(HttpServletRequest request, @Valid ResetKeyVO resetKeyVO, Errors errors){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        UmsMember user=(UmsMember)session.getAttribute("umsMember");

        if(user==null){
            mv.setViewName("user/login");
            return mv;
        }
        String username=user.getUsername();
        String password=user.getPassword();
        String oldPassword=resetKeyVO.getOldPassword().trim();
        String newPassword=resetKeyVO.getNewPassword().trim();
        String verifyCode=  resetKeyVO.getVerifyCode();
        String checkPassword=resetKeyVO.getCheckPassword().trim();
        //原密码或新密码为空
        if(errors.hasErrors()){
            mv.addObject("resetTip",errors.getFieldError().getDefaultMessage());
            mv.setViewName("user/resetPanel");
            return mv;
        }
        //原密码错误
        final String Encrypassword= EncryptHelper.encrypt(oldPassword, EncryptType.MD5);
        if(!password.equals(Encrypassword)){
            mv.addObject("resetTip","原密码错误");
            mv.setViewName("user/resetPanel");
            return mv;
        }
        //输入的两次密码不一致
        if(!newPassword.equals(checkPassword)){
            mv.addObject("resetTip","两次密码不一致,重新输入");
            mv.setViewName("user/resetPanel");
            return mv;
        }
        //验证输入的验证码是否和图片一致
        String icode=(String)session.getAttribute("IDENTITY_CODE");//提取图片中的文字
        if(verifyCode.equalsIgnoreCase(icode)==false){
            mv.addObject("tips","验证码输入错误");
            mv.setViewName("user/resetPanel");
            return mv;
        }
        //密码正确
        session.removeAttribute("umsMember");
        final String EncryNewPassword= EncryptHelper.encrypt(newPassword, EncryptType.MD5);
        userService.resetPassword(EncryNewPassword,username);
        mv.setViewName("user/login");
        return mv;
    }
    /*登出*/
    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request){
        ModelAndView mv=new ModelAndView();
        HttpSession session=request.getSession();
        session.removeAttribute("umsMember");
        mv.setViewName("redirect:home");
        return mv;
    }
    /*用户地址管理*/
    @GetMapping ( "userAddress")
    public ModelAndView userAddress(ModelAndView mv, HttpSession session) {
        UmsMember umsMember= (UmsMember) session.getAttribute("umsMember");
        if(umsMember==null){mv.setViewName("user/login"); return mv;}
        String id=umsMember.getId();
        List<UmsMemberReceiveAddress> userAddressList = userAddressService.findByUserId(id);

        mv.addObject("userAddressList", userAddressList);
        mv.setViewName("user/userAddress");
        return mv;
    }

    /*新增地址*/
    @PostMapping("/userAddress/add")
    @ResponseBody
    public String doAddUserAddress(HttpSession session, UmsMemberReceiveAddress userAddress) {
        UmsMember umsMember= (UmsMember) session.getAttribute("umsMember");
        userAddress.setMemberId(umsMember.getId());
        userAddressService.saveAddress(userAddress);
        ResultBase result=new ResultBase();
        result.setStatus(Status.SUCCESS);
        result.setMessage("成功");
        return JSON.toJSONString(result);
    }
    /*删除地址*/
    @GetMapping ("/userAddress/delete/{addressId}")
    @ResponseBody
    public String delUserAddress(@PathVariable String addressId) {
        userAddressService.deleteAddressById(addressId);
        ResultBase result=new ResultBase();
        result.setStatus(Status.SUCCESS);
        result.setMessage("删除成功");
        return JSON.toJSONString(result);
    }
    @GetMapping("/userAddress/modify/{addressId}")
    @ResponseBody
    public String getAddressById(@PathVariable String addressId) {
        UmsMemberReceiveAddress address=userAddressService.getAddressById(addressId);
        String s=JSON.toJSONString(address);
        return s;
    }
    @PostMapping("/userAddress/modify/")
    @ResponseBody
    public String modifyAddress(UmsMemberReceiveAddress umsMemberReceiveAddress,HttpSession session) {
        UmsMember umsMember= (UmsMember) session.getAttribute("umsMember");
        umsMemberReceiveAddress.setMemberId(umsMember.getId());
        userAddressService.modifyAddress(umsMemberReceiveAddress);
        ResultBase result=new ResultBase();
        result.setStatus(Status.SUCCESS);
        result.setMessage("修改成功");
        return JSON.toJSONString(result);
    }

}
