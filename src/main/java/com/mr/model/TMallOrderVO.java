package com.mr.model;

import java.util.List;

/**
 * Created by niudd on 2018/11/12.
 */
public class TMallOrderVO extends TMallOrder{
    //一个订单表对应多个物流表
    private List<TMallFlowVo> flowList;

    public List<TMallFlowVo> getFlowList() {
        return flowList;
    }

    public void setFlowList(List<TMallFlowVo> flowList) {
        this.flowList = flowList;
    }
}
