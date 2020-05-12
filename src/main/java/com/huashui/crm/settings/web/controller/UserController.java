package com.huashui.crm.settings.web.controller;

import com.huashui.crm.exception.LoginException;
import com.huashui.crm.settings.domain.User;
import com.huashui.crm.settings.service.UserService;
import com.huashui.crm.utils.HandleFlag;
import com.huashui.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/9
 */
@Controller
@RequestMapping("/settings/user")
public class UserController {

    @Autowired
    private UserService service;


    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String, Object> login(String loginAct, String loginPwd, String flag, HttpServletRequest request, HttpServletResponse response) throws LoginException {
        //为密码进行MD5形式的加密操作
        loginPwd = MD5Util.getMD5(loginPwd);

        //取得浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("浏览器地址 " + ip);


        User user = service.login(loginAct,loginPwd,ip);

        //如果程序能够顺利走到这一行，则一定登陆成功，user对象已经保存到session中，我们应该为前端返回数据
        request.getSession().setAttribute("user", user);

        if("a".equals(flag)){
            Cookie cookie1 = new Cookie("loginAct", loginAct);
            Cookie cookie2 = new Cookie("loginPwd", loginPwd);

            cookie1.setMaxAge(10*24*60*60);
            cookie2.setMaxAge(10*24*60*60);

            cookie1.setPath("/");
            cookie2.setPath("/");

            response.addCookie(cookie1);
            response.addCookie(cookie2);
        }

        return HandleFlag.successTrue();
    }

    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest request){

        //判断用户是否做了十天免登陆
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            String loginAct = null;
            String loginPwd = null;

            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("loginAct".equals(name)){
                    loginAct = cookie.getValue();

                    continue;
                }

                if("loginPwd".equals(name)){

                    loginPwd = cookie.getValue();
                }
            }

            if(loginAct != null && loginPwd != null){

                String ip = request.getRemoteAddr();

                try {
                    User user =service.login(loginAct, loginPwd, ip);
                    request.getSession().setAttribute("user", user);
                    return "redirect:/workbench/toWorkbenchIndex.do";
                } catch (LoginException e) {

                    e.printStackTrace();

                    return "/login";
                }
            }else {

                return "/login";
            }

        }else {

            return "/login";
        }
    }

    @RequestMapping("logout.do")
    public String logout(HttpSession session,HttpServletResponse response){

        //通过代码手动销毁session对象
        session.invalidate();

        //销毁cookie  但是cookie没有方法销毁，只能用新的代替
        Cookie cookie1 = new Cookie("loginAct", null);
        Cookie cookie2 = new Cookie("loginPwd", null);

        cookie1.setMaxAge(0);
        cookie2.setMaxAge(0);

        cookie1.setPath("/");
        cookie2.setPath("/");

        response.addCookie(cookie1);
        response.addCookie(cookie2);

        return "/login";

    }


}
