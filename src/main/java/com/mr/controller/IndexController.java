package com.mr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("toMainPage")
    public String toMainPage(){
        return "main";
    }
    @RequestMapping("toLoginPage")
    public String toLoginPage(){
        return "login";
    }

    @RequestMapping("toRegPage")
    public String toRegPage(){
        return "reg";
    }
}
