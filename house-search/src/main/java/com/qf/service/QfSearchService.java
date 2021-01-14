package com.qf.service;

import com.qf.pojo.resp.BaseResp;

/**
 * Created by 54110 on 2020/12/29.
 */
public interface QfSearchService {
    BaseResp selectKey(String key, Integer page, Integer size, String pricesort);

    BaseResp selectHouse(String key,String check,Double tariff,Integer page, Integer size);
}
