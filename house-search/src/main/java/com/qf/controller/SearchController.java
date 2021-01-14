package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.service.QfSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 54110 on 2020/12/29.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    QfSearchService qfSearchService;

    @RequestMapping("/selectKey")
    public BaseResp selectKey(@RequestParam("key")String key, @RequestParam("page")Integer page, @RequestParam("size")Integer size, @RequestParam("pricesort")String pricesort){

        return qfSearchService.selectKey(key,page,size,pricesort);
    }

    @RequestMapping("/selectHouse")
    public BaseResp selectHouse(@RequestParam("key")String key,@RequestParam("tariff")Double tariff,@RequestParam("check")String check,@RequestParam("page")Integer page, @RequestParam("size")Integer size){
        BaseResp baseResp = qfSearchService.selectHouse(key, check,tariff, page, size);
        return baseResp;
    }
}
