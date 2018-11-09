package com.mr.mapper;

import com.mr.model.TMallShoppingcar;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by niudd on 2018/11/8.
 */
public interface CartMapper {
    List<TMallShoppingcar> listCartByUserId(@Param("userId") Integer userId);

    void saveCart(TMallShoppingcar cart);

    void updateCartBySkuIdAndUserId(Map<String, Object> cartMap);


    void saveCartList(Map<String, Object> map);

    TMallShoppingcar findCartBySkuIdAndUserId(@Param("skuId") Integer skuId,@Param("userId") Integer userId);

    void updateCartShfxzBySkuIdAndUserId(Map<String, Object> map);
}
