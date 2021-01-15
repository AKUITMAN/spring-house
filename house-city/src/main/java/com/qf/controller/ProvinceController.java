package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @RequestMapping("/findAll5")
    public BaseResp findAll(){
        return provinceService.findAll();
    }
}
