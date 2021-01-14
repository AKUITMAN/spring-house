package com.qf.controller;


import com.qf.pojo.resp.BaseResp;
import com.qf.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @RequestMapping("/addressfindAll")
    public BaseResp addressfindAll(){
        return addressService.AddressfindAll();
    }
}
