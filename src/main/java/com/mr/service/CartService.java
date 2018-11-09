package com.mr.service;

import com.mr.model.TMallShoppingcar;

import java.util.List;
import java.util.Map;

/**
 * Created by niudd on 2018/11/8.
 */
public interface CartService {
    List<TMallShoppingcar> listCartByUserId(Integer userId);

    void saveCart(TMallShoppingcar cart);

    void updateCartBySkuIdAndUserId(Map<String, Object> cartMap);

    void saveCartList(List<TMallShoppingcar> cartList, Integer userId);

    TMallShoppingcar findCartBySkuIdAndUserId(Integer skuId, Integer id);

    void updateCartShfxzBySkuIdAndUserId(int skuId, Integer id, String shfxz);
}
