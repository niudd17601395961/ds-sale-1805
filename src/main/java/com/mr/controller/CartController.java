package com.mr.controller;

import com.mr.model.TMallShoppingcar;
import com.mr.model.TMallUserAccount;
import com.mr.util.MyCookieUtils;
import com.mr.util.MyJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个cookieName:cookieCartList
 *
 *
 * Created by niudd on 2018/11/7.
 */
@Controller
public class CartController {
    /**
     *  @CookieValue作用是从cookie对象中获取名为key的value值
     *  将list对象转为json字符串存放在cookie中
     *required = false如果不加第一次进来如果未空会抛出异常
     * @param cart
     * @param session
     * @param cookieCartList
     * @return
     */
    @RequestMapping("saveCart")
    public String saveCart(TMallShoppingcar cart, HttpSession session,
                               @CookieValue(value="cookieCartList",required = false) String cookieCartList,
        HttpServletRequest request,HttpServletResponse response){
        //cookie中是以集合的形式存放数据，所以要先把对象放入集合中
        //定义一个集合将对象放入集合中
        List<TMallShoppingcar> cartList=new ArrayList<TMallShoppingcar>();
        //判断是否登录
        TMallUserAccount user =(TMallUserAccount)session.getAttribute("user");
        if(user==null){//用户不存在，没有登录
            if(StringUtils.isBlank(cookieCartList)){//cookie中集合为空  则直接添加到cookie中
                //给购物车集合添加数据
                cartList.add(cart);
            }else{//有数据则进行判断更新
                //如何判断cookie中存在相同数据  先获取cookie中的集合
                cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
                boolean b1=false;//默认设置为不存在
                for (int i = 0; i < cartList.size(); i++) {
                    if(cartList.get(i).getSkuId()==cart.getSkuId()){//如果cookie中与需要添加相等则存在
                        b1=true;
                    }
                }
                if(b1){//判断数据库中是否存在相同数据
                    //循环遍历cookie中集合  cookie中存放的是字符串，所以要进行格式转换 并且在后边加泛型
                    cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
                    //用商品的skuId进行比较
                    for (int i = 0; i <cartList.size() ; i++) {
                        if(cartList.get(i).getSkuId()==cart.getSkuId()){//如果存在 则更新数量
                            //更新购物车的数量
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl()+cart.getTjshl());
                            cartList.get(i).setHj(cartList.get(i).getSkuJg()*cartList.get(i).getTjshl());
                        }else{
                            cartList.add(cart);
                        }
                    }
                }else{//不存在则直接放入cookie中
                    //给购物车集合添加数据
                    cartList.add(cart);
                }
            }
            //把购物车集合添加到cookie中
           // MyCookieUtils.setCookie(request,response,"cookieCartList",
             //       MyJsonUtil.objectToJson(cartList),3*24*60*60,true);
            Cookie cookie = new Cookie("cookieCartList", MyJsonUtil.objectToJson(cartList));   //把对象放到cookie中
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

        }else{//用户存在
        }
        return "cart-success";
    }

    /**
     *查询mini购物车
     * @return
     */
   @RequestMapping("findMiniCart")
    public String findMiniCart(HttpSession session, ModelMap map,
                               @CookieValue(value="cookieCartList",required = true) String cookieCartList){
       List<TMallShoppingcar> cartList=new ArrayList<TMallShoppingcar>();
       //判断是否登陆
       TMallUserAccount user =(TMallUserAccount)session.getAttribute("user");
       if(user==null){//说明没有登陆
           //获取cookie中的数据
          cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
       }else{
       }
       //计算数量和价格
       Integer countNum=0;
       Double hjSum=0.0;
       for (int i = 0; i < cartList.size(); i++) {
           countNum+=cartList.get(i).getTjshl();
           hjSum+=cartList.get(i).getHj();
       }
       map.put("cartList",cartList);
       map.put("countNum",countNum);
       map.put("hjSum",hjSum);
       return "miniCartInner";
   }
}
