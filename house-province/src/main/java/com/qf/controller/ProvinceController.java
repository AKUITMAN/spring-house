package com.qf.controller;


import com.qf.pojo.resp.BaseResp;
import com.qf.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Autowired
    ProvinceService provinceService;

    @RequestMapping("/provincefindAll")
    public BaseResp addressfindAll(){
        return provinceService.ProvincefindAll();
    }
}
