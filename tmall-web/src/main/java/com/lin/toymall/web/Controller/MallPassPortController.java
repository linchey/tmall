package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.UserService;
import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.util.CookieUtil;
import com.lin.toymall.util.JwtUtil;
import com.lin.toymall.web.Util.EncryptHelper;
import com.lin.toymall.web.Util.EncryptType;
import com.lin.toymall.web.VO.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MallPassPortController {
    /*@RequestMapping("loginView")

    public String index(String ReturnUrl, ModelMap modelMap){
        modelMap.put( "ReturnUrl",ReturnUrl );
        return "user/loginView";
    }
*/
    @Reference
    UserService userService;
    //登录验证
    @PostMapping("user/loginCheck1")
    @ResponseBody
    public String loginCheck1(@Valid LoginVO loginVO, Errors errors, HttpServletRequest request, HttpServletResponse response){
        String token = "";
        UmsMember umsMember=new UmsMember();
        umsMember.setUsername( loginVO.getUsername() );
        String error=null;
        String pwd=loginVO.getPassword().trim();

        //密码或用户名为空
        if(errors.hasErrors()){
           error= errors.getFieldError().getDefaultMessage();
            return "";
        }
        final String Encrypassword= EncryptHelper.encrypt(pwd, EncryptType.MD5);
        umsMember.setPassword( Encrypassword );
        // 调用用户服务验证用户名和密码
        UmsMember umsMemberLogin = userService.login(umsMember);

        Map<String,Object> userMap = new HashMap<>();
        if(umsMemberLogin!=null){
            // 登录成功
            // 用jwt制作token
            String memberId = umsMemberLogin.getId();
            String username = umsMemberLogin.getUsername();
            userMap.put("memberId",memberId);
            userMap.put("username",username);
            String ip = request.getHeader("x-forwarded-for");// 通过转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip) |ip.equals( "0:0:0:0:0:0:0:1" )){
                    ip = "127.0.0.1";
                }
            }
            // 按照设计的算法对参数进行加密后，生成token
            token = JwtUtil.encode("1997lincy", userMap, ip);

            // 将token存入redis一份
            userService.addUserToken(token,memberId);
            //覆盖cookie的token
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);

            HttpSession session=request.getSession();
            UmsMember user=new UmsMember();
            user.setId( memberId );
            user.setUsername( username );
            session.setAttribute( "umsMember" ,user);

        }else{
            // 登录失败

            token = "fail";

        }

        return token;
    }
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,String currentIp,HttpServletRequest request){

        // 通过jwt校验token真假
        Map<String,String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "1997lincy", currentIp);

        if(decode!=null){
            map.put("status","success");
            map.put("memberId",(String)decode.get("memberId"));
            map.put("username",(String)decode.get("username"));

        }else{
            map.put("status","fail");

        }
        return JSON.toJSONString(map);
    }
}