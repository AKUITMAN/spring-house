package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping("/findByUserId")
    public BaseResp findByUserId(@RequestParam("uid")Integer uid){
        return orderService.findByUserId(uid);
    }
}
