package com.mr.controller;

import com.mr.model.TMallShoppingcar;
import com.mr.model.TMallUserAccount;
import com.mr.service.CartService;
import com.mr.service.UserService;
import com.mr.util.MyCookieUtils;
import com.mr.util.MyJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("selectLogin")
    public String selectLogin(String userName, String password, HttpSession session,
                              String loginSuccessUrl,HttpServletRequest request, HttpServletResponse response,
                              @CookieValue(value="cookieCartList",required = false) String cookieCartList) {
        TMallUserAccount user = userService.selectLogin(userName, password);
        if (user == null) {
            return "redirect:toLoginPage.do";
        } else {
            session.setAttribute("user",user);
            String yhMch = user.getYhMch();
            //将用户名放入cookie对象中
            MyCookieUtils.setCookie(request,response,"yhMch",yhMch,24*60*60,true);


            //更新数据
            //查询cookie中的购物车
            if(!StringUtils.isBlank(cookieCartList)){//有数据
                //将cookie中的数据添加到数据库中
                List<TMallShoppingcar> cartListCookie = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
                //从数据库中获取购物车的数据
                List<TMallShoppingcar> cartListDb = cartService.listCartByUserId(user.getId());
                //循环cookie
                for (int i = 0; i < cartListCookie.size(); i++) {
                    //如何判断当前对象是否重复
                    //根据当前对象的skuid和用户id查询数据
                    TMallShoppingcar cart =
                            cartService.findCartBySkuIdAndUserId(cartListCookie.get(i).getSkuId(),user.getId());

                    if(cart != null){//重复
                        //更新
                        Map<String, Object> cartMap = new HashMap<String,Object>();
                        cartMap.put("skuId",cartListCookie.get(i).getSkuId());
                        cartMap.put("userId",user.getId());
                        //修改对象的数量
                        cartMap.put("tjshl",cartListCookie.get(i).getTjshl()+cart.getTjshl());
                       //BigDecimal jg = new BigDecimal(cartListCookie.get(i).getSkuJg() + "");
                        //BigDecimal shl = new BigDecimal(cartListCookie.get(i).getTjshl()+cart.getTjshl());
                        cartMap.put("hj",CartController.getHj(cartListCookie.get(i)));
                        cartService.updateCartBySkuIdAndUserId(cartMap);
                    }else{//添加当前对象
                        cartListCookie.get(i).setYhId(user.getId());
                       cartService.saveCart(cartListCookie.get(i));
                    }
                }
                //cartService.saveCartList(cartListCookie,user.getId());
                //清空cookie中的购物车
                MyCookieUtils.deleteCookie(request,response,"cookieCartList");
                //清空redis购物车
                redisTemplate.delete("cookieCartList"+user.getId());
            }
            if(!StringUtils.isBlank(loginSuccessUrl)){
                return "redirect:"+loginSuccessUrl+".do";
            }
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
