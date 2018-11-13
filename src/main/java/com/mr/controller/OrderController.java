package com.mr.controller;

import com.mr.model.*;
import com.mr.service.OrderService;
import com.mr.util.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by niudd on 2018/11/12.
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("toCheckPage")
    public String toCheckPage(HttpSession session, ModelMap map){
        TMallUserAccount user = (TMallUserAccount)session.getAttribute("user");
        if(user==null){
            return "redirect:toLoginPage.do?loginSuccessUrl=toCheckPage";
        }else{
            //通过当前用户去查询地址集合
           List<TMallAddress> addressList =orderService.selectAddressListByYhId(user.getId());
            //获取到购物车中被选中的对象
            //从redis中获取当前用户的购物车数据
            List<TMallShoppingcar> cartList =(List<TMallShoppingcar>) redisTemplate.opsForValue().get("redisCartList" + user.getId());
            List<TMallShoppingcar> checkOrderList=new ArrayList<TMallShoppingcar>();
            for (int i = 0; i < cartList.size(); i++) {//遍历集合获得被选中的对象
                TMallShoppingcar cart = cartList.get(i);
                //如果被选中添加到集合中
                if(cart.getShfxz().equals("1")){
                    checkOrderList.add(cart);
                }
            }
            map.put("checkOrderList",checkOrderList);
            map.put("addressList",addressList);
            map.put("sum",CartController.getSum(cartList));
            return "checkOrder";
        }
    }

    @RequestMapping("saveOrder")
    public String saveOrder(TMallAddress address,HttpSession session){
        TMallUserAccount user =(TMallUserAccount)session.getAttribute("user");
        //从redis中获取当前用户的购物车数据
        List<TMallShoppingcar> cartList =
                (List<TMallShoppingcar>)redisTemplate.opsForValue().get("redisCartListUser"+user.getId());
        //实体类  一个订单对应多个物流信息  物流信息对应多个订单详情
        TMallOrderVO orderVO = new TMallOrderVO();
        orderVO.setJdh(1);
        orderVO.setZje(CartController.getSum(cartList).doubleValue());
        orderVO.setYhId(user.getId());
        orderVO.setDzhId(address.getId());
        orderVO.setDzhMch(address.getDzMch());
        orderVO.setShhr(address.getShjr());


        //物流信息
        List<TMallFlowVo> flowList=new ArrayList<TMallFlowVo>();
        Set<String> flowSet=new HashSet<String>();
        //根据不同的地址进行拆单
        for (int i = 0; i < cartList.size(); i++) {
            String kcdz = cartList.get(i).getKcdz();
            flowSet.add(kcdz);
        }
        //循环set
        Iterator<String> flowIterator = flowSet.iterator();
        while(flowIterator.hasNext()){
            String nextKcdz = flowIterator.next();//库存地址
            TMallFlowVo flowVO=new TMallFlowVo();
            flowVO.setPsfsh("顺丰物流");
            flowVO.setPsshj(MyDateUtil.getMyDateD(new Date(), 1));
            flowVO.setPsmsh("配送描述：风里雨里，东门等你！");
            flowVO.setYhId(user.getId());
            //订单详情集合
            List<TMallOrderInfo> infoList=new ArrayList<TMallOrderInfo>();
            for (int i = 0; i < cartList.size(); i++) {
                TMallOrderInfo info=new TMallOrderInfo();
                TMallShoppingcar car = cartList.get(i);
                if(cartList.get(i).getKcdz().equals(nextKcdz)){
                    info.setSkuJg(car.getSkuJg());
                    info.setSkuShl(car.getTjshl());
                    info.setSkuKcdz(car.getKcdz());
                    info.setGwchId(car.getId());
                    info.setSkuId(car.getSkuId());
                    info.setSkuMch(car.getSkuMch());
                    info.setShpTp(car.getShpTp());
                    infoList.add(info);
                }
            }
            //将商品详情添加到物流集合中
            flowVO.setInfoList(infoList) ;
            //将每一个符合地址的数据放在物流集合里
            flowList.add(flowVO);
        }
        orderVO.setFlowList(flowList);
        orderService.saveOrder(orderVO);
        //更新redis
        redisTemplate.delete("redisCartListUser"+user.getId());
        return "success";
    }
}
