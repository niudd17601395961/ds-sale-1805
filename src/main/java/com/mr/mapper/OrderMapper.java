package com.mr.mapper;

import com.mr.model.TMallAddress;
import com.mr.model.TMallOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by niudd on 2018/11/12.
 */
public interface OrderMapper {
    List<TMallAddress> selectAddressListByYhId(@Param("userId") Integer userId);

    void saveOrder(TMallOrderVO orderVO);

    void saveFlow(Map<String, Object> flowMap);

    void saveInfo(Map<String, Object> infoMap);

    void deleteCartsByCartIds(@Param("cartIds") List<Integer> cartIds);
}
