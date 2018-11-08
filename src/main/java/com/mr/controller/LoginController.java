package com.mr.controller;

import com.mr.model.TMallUserAccount;
import com.mr.service.UserService;
import com.mr.util.MyCookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("selectLogin")
    public String selectLogin(String userName, String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        TMallUserAccount user = userService.selectLogin(userName, password);
        if (user == null) {
            return "redirect:toLoginPage.do";
        } else {
            session.setAttribute("user",user);
            String yhMch = user.getYhMch();
            //将用户名放入cookie对象中
            MyCookieUtils.setCookie(request,response,"yhMch",yhMch,24*60*60,true);
            return "redirect:toMainPage.do";

        }
    }
    //注销
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:toMainPage.do";
    }

    //先根据账号去数据库查询
    @ResponseBody
    @RequestMapping("selectUserByName")
    public String selectUserByName(String userName){
        TMallUserAccount user=userService.selectUserByName(userName);
        if(user==null){
            return "yes";
        }else{
            return "no";
        }
    }

    @RequestMapping("saveUser")
    public String saveUser(TMallUserAccount user){
        userService.saveUser(user);
        return "redirect:toLoginPage.do";
    }
}
