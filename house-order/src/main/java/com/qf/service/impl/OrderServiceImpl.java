package com.qf.service.impl;

import com.qf.dao.OrderMapper;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.resp.HouseResp;
import com.qf.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public BaseResp findByUserId(Integer uid) {
        BaseResp baseResp = new BaseResp();
        List<HouseResp> list=orderMapper.findByUserId(uid);
        baseResp.setCode(200);
        baseResp.setMessage("查询成功");
        baseResp.setData(list);
        return baseResp;
    }
}
