package com.mr.service;

import com.mr.model.TMallAddress;
import com.mr.model.TMallOrderVO;

import java.util.List;

/**
 * Created by niudd on 2018/11/12.
 */
public interface OrderService {
    List<TMallAddress> selectAddressListByYhId(Integer userId);

    void saveOrder(TMallOrderVO orderVO);
}
