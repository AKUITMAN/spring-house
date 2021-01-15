package com.qf.service;

import com.qf.pojo.resp.BaseResp;

public interface OrderService {
    public BaseResp findByUserId(Integer uid);
}
