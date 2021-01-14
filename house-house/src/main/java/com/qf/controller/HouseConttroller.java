package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseConttroller {

    @Autowired
    HouseService houseService;

    @RequestMapping("/findById")
    public BaseResp findById(@RequestBody Map map){
        return houseService.findById(map);
    }
}
