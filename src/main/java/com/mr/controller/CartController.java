package com.mr.controller;

import com.mr.model.TMallShoppingcar;
import com.mr.model.TMallUserAccount;
import com.mr.service.CartService;
import com.mr.util.MyJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义一个cookieName:cookieCartList
 *
 *
 * Created by niudd on 2018/11/7.
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取合计值
     * @param cart
     * @return
     */
    public static Double getHj(TMallShoppingcar cart){
        BigDecimal jg = new BigDecimal(cart.getSkuJg() + "");
        BigDecimal tjshl = new BigDecimal(cart.getTjshl() + "");
        double hj = jg.multiply(tjshl).doubleValue();
        return hj;
    }

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
    public String saveCart(TMallShoppingcar cart, HttpSession session,ModelMap map,
                           @CookieValue(value="cookieCartList",required = false) String cookieCartList,
                           HttpServletRequest request,HttpServletResponse response) {

        cart.setHj(getHj(cart));

        //cookie中是以json字符串的形式存放数据，所以要先把对象放入集合中
        //定义一个集合将对象放入集合中
        List<TMallShoppingcar> cartList = new ArrayList<TMallShoppingcar>();
        //判断是否登录
        TMallUserAccount user = (TMallUserAccount) session.getAttribute("user");
        if (user == null) {//用户不存在，没有登录
            if (StringUtils.isBlank(cookieCartList)) {//cookie中集合为空  则直接添加到cookie中
                //给购物车集合添加数据
                cartList.add(cart);
            } else {//有数据则进行判断更新
                //如何判断cookie中存在相同数据  先获取cookie中的集合
                cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
                boolean b1 = false;//默认设置为不存在
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getSkuId() == cart.getSkuId()) {//如果cookie中与需要添加相等则存在
                        b1 = true;
                    }
                }
                if (b1) {//判断数据库中是否存在相同数据
                    //循环遍历cookie中集合  cookie中存放的是字符串，所以要进行格式转换 并且在后边加泛型
                    cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
                    //用商品的skuId进行比较
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getSkuId() == cart.getSkuId()) {//如果存在 则更新数量
                            //更新购物车的数量
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl() + cart.getTjshl());

                           // BigDecimal jg = new BigDecimal(cartList.get(i).getSkuJg() + "");
                            //BigDecimal tjshl = new BigDecimal(cartList.get(i).getTjshl() + "");
                            cartList.get(i).setHj(getHj(cartList.get(i)));
                        } else {
                            cartList.add(cart);
                        }
                    }
                } else {//不存在则直接放入cookie中
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

        } else {//登陆
            //根据当前用户查询数据库中是否有数据
            //获取数据
            cartList = cartService.listCartByUserId(user.getId());
            if (cartList != null && cartList.size() > 0) {//有数据
                //循环遍历，判断存在不存在
                boolean b1 = false;
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getSkuId() == cart.getSkuId()) {
                        b1 = true;
                    }
                }
                if (b1) {//说明数据库中有数据  存在
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getSkuId() == cart.getSkuId()) {//如果已经存在
                            Map<String,Object> cartMap=new HashMap<String, Object>();
                            cartMap.put("skuId",cartList.get(i).getSkuId());
                            cartMap.put("userId",user.getId());
                            cartMap.put("tjshl",cartList.get(i).getTjshl() + cart.getTjshl());
                            cartList.get(i).setTjshl(cartList.get(i).getTjshl() + cart.getTjshl());
                            //BigDecimal hj = new BigDecimal(cartList.get(i).getSkuJg() + "");
                            //BigDecimal tjshl = new BigDecimal(cartList.get(i).getTjshl() + "");

                            cartMap.put("hj",getHj(cartList.get(i)));
                            cartService.updateCartBySkuIdAndUserId(cartMap);
                        }
                }
            } else {//添加数据
                // 将用户Id添加到cart对象中
                cart.setYhId(user.getId());
                cartService.saveCart(cart);
            }
        }else{//添加数据
            //将用户Id添加到cart对象中
            cart.setYhId(user.getId());
            cartService.saveCart(cart);
        }
        //更新 redis(根据用户Id清除redis中cart的list,当前用户)
        redisTemplate.delete("redisCartList"+user.getId());
        //用户登陆之后要将数据保存在redis中
        //确定当前用户的key
           // redisTemplate.opsForValue().set("redisCartList"+user.getId(),cartList);
    }
        map.put("cart",cart);
        return "cart-success";
    }

    /**
     *查询mini购物车
     * @return
     */
    @RequestMapping("findMiniCart")
    public String findMiniCart(HttpSession session, ModelMap map,
                               @CookieValue(value="cookieCartList",required = false) String cookieCartList){
        List<TMallShoppingcar> cartList=new ArrayList<TMallShoppingcar>();
        //判断是否登陆
        TMallUserAccount user =(TMallUserAccount)session.getAttribute("user");
        if(user==null){//说明没有登陆
            //获取cookie中的数据
            cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
        }else{//登陆  需要进行判断redis中是否有数据
            //用cartlist接收并返回
            cartList = (List<TMallShoppingcar>)redisTemplate.opsForValue().get("redisCartList" + user.getId());

            if(cartList== null|| cartList.size()==0){// 说明没有数据
                //通过用户Id从数据查询
                cartList=cartService.listCartByUserId(user.getId());
                //并且将查询到的数据放入redis中  下次查询直接从redis中获取
                redisTemplate.opsForValue().set("redisCartList"+user.getId(),cartList);
            }
        }
        //计算数量和价格
        Integer countNum=0;
        for (int i = 0; i < cartList.size(); i++) {
            countNum+=cartList.get(i).getTjshl();
        }
        map.put("cartList",cartList);
        map.put("countNum",countNum);
        map.put("hjSum",getSum(cartList));
        return "miniCartInner";
    }

    public static BigDecimal getSum( List<TMallShoppingcar> cartList){
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getShfxz().equals("1")) {//判断是否选中进行计算金额
                sum = sum.add(new BigDecimal(cartList.get(i).getHj() + ""));
            }
        }
        return sum;
    }

    /**
     * 跳转购物车页面  并且记载数据
     * @return
     */
    @RequestMapping("toCartListPage")
    public String toCartListPage(HttpSession session, ModelMap map,
                                 @CookieValue(value = "cookieCartList",required = false) String cookieCartList){

        List<TMallShoppingcar> cartList = new ArrayList<TMallShoppingcar>();
        //判断是否登录
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");

        if(user == null){//未登录
            cartList = MyJsonUtil.jsonToList(cookieCartList, TMallShoppingcar.class);
        }else{//登录

            //从redis中获取数据
            cartList = (List<TMallShoppingcar>)redisTemplate.opsForValue().get("redisCartListUser" + user.getId());

            if(cartList == null || cartList.size() == 0){//没有数据
                //查询数据库、通过用户
                cartList = cartService.listCartByUserId(user.getId());
                redisTemplate.opsForValue().set("redisCartListUser"+user.getId(),cartList);
            }
        }

        /*Integer countNum = 0;
        for (int i = 0; i < cartList.size(); i++) {
            countNum += cartList.get(i).getTjshl();
        }*/
        map.put("cartList",cartList);
        //map.put("countNum",countNum);
        map.put("hjSum",getSum(cartList));
        return "cartList";
    }

    /**
     * 根据skuId修改对象的选中状态，并且刷新合计
     * @param skuId
     * @param shfxz
     * @param map
     * @param cookieCartList
     * @return
     */
    @RequestMapping("changeShfxz")
    public String changeShfxz(HttpServletResponse response ,HttpServletRequest request,
                              int skuId , String shfxz , ModelMap map,HttpSession session,
                              @CookieValue(value = "cookieCartList",required = false) String cookieCartList){

        List<TMallShoppingcar> cartList = new ArrayList<TMallShoppingcar>();
        //判断是否登录
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");
        if(user != null){//登录
            //通过skuId 修改 cart
            //从reids中获取到数据
            cartList =  (List<TMallShoppingcar>)redisTemplate.opsForValue().get("redisCartListUser"+user.getId());
            //更新数据库
            for (int i = 0; i < cartList.size(); i++) {
                if(cartList.get(i).getSkuId() == skuId){
                    //修改数据库的状态
                    cartService.updateCartShfxzBySkuIdAndUserId(skuId,user.getId(),shfxz);
                    //修改
                    cartList.get(i).setShfxz(shfxz);
                }
            }
            //同步redis中
            redisTemplate.opsForValue().set("redisCartListUser"+user.getId(),cartList);

        }else{//未登录
            cartList = MyJsonUtil.jsonToList(cookieCartList,TMallShoppingcar.class);
            for (int i = 0; i < cartList.size(); i++) {
                //如果skuId一样的话，修改该对象的状态。
                if(cartList.get(i).getSkuId() == skuId){
                    cartList.get(i).setShfxz(shfxz);
                }
            }
            //更新cookie
            //MyCookieUtils.setCookie(request,response,"cookieCartList",
              //      MyJsonUtil.objectToJson(cartList),3*24*60*60,true);
            Cookie cookie = new Cookie("cookieCartList", MyJsonUtil.objectToJson(cartList));   //把对象放到cookie中
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
        }

        map.put("cartList",cartList);
        map.put("hjSum",getSum(cartList));
        return "cartListInner";
    }


}
