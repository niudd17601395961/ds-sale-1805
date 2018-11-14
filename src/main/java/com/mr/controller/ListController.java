package com.mr.controller;

import com.mr.model.OBJECTSku;
import com.mr.model.OBJECTTMallAttr;
import com.mr.model.TMallSku;
import com.mr.model.TMallSkuAttrValueVO;
import com.mr.service.AttrService;
import com.mr.service.SkuService;
import com.mr.util.MyHttpClientUtil;
import com.mr.util.MyJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaodd on 2018/11/6.
 */
@Controller
public class ListController {

    @Autowired
    private AttrService attrService;

    @Autowired
    private SkuService skuService;

    @RequestMapping("toListPage")
    public String toListPage(Integer flbh2 , ModelMap map){
        //通过class2查询属性
        List<OBJECTTMallAttr> attrList = attrService.findAttrByclass2(flbh2);

        //查询sku集合
        List<OBJECTSku> skuList = skuService.listSkuByFlbh2(flbh2);

        map.put("attrList",attrList);
        map.put("skuList",skuList);
        map.put("flbh2",flbh2);

        return "list";
    }

    /**
     * 通过属性、class2查询
     * @return
     */
    @RequestMapping("listSkuByAttrAndClass2")
    public String listSkuByAttrAndClass2(TMallSkuAttrValueVO attrValueVO, Integer flbh2,ModelMap map){

        List<OBJECTSku> skuList = skuService.listSkuByAttrAndClass2(attrValueVO, flbh2);

        map.put("skuList",skuList);
        return "list-sbox";
    }


    @RequestMapping("listSkuBySolr")
    public String listSkuBySolr(String key,ModelMap map){
        //需要调用solr接口
        Map<String,String> keyMap=new HashMap<String,String>();
        keyMap.put("key",key);
        String skuJson = MyHttpClientUtil.doGet("http://localhost:8181/querySkuBySolr.do", keyMap);
        List<TMallSku> skuList = MyJsonUtil.jsonToList(skuJson, TMallSku.class);
        map.put("skuList",skuList);
        return "list";
    }
}
