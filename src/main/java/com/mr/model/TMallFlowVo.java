package com.mr.model;

import java.util.List;

/**
 * Created by niudd on 2018/11/12.
 */
public class TMallFlowVo extends TMallFlow{
    //物流表里对应多个商品详情
    private List<TMallOrderInfo> infoList;

    public List<TMallOrderInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<TMallOrderInfo> infoList) {
        this.infoList = infoList;
    }
}
