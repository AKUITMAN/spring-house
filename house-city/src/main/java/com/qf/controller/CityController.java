package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("city")
public class CityController {
    @Autowired
    private CityService cityService;
    @RequestMapping("/findByPid")
    public BaseResp findByPid(@RequestParam("pid")Integer pid){
        return cityService.findByPid(pid);
    }
}
