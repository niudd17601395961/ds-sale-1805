package com.mr.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("toMainPage")
    public String toMainPage(){
        return "main";
    }
    @RequestMapping("toLoginPage")
    public String toLoginPage(String loginSuccessUrl, ModelMap map){
        if(!StringUtils.isBlank(loginSuccessUrl)){
            map.put("loginSuccessUrl",loginSuccessUrl);
        }
        return "login";
    }

    @RequestMapping("toRegPage")
    public String toRegPage(){
        return "reg";
    }
}
