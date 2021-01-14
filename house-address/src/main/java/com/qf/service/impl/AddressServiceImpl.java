package com.qf.service.impl;

import com.qf.dao.AddressMapper;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.Address;
import com.qf.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressMapper addressMapper;

    @Override
    public BaseResp AddressfindAll() {
        List<Address> addresses = addressMapper.AddressfindAll();
        BaseResp baseResp = new BaseResp();
        baseResp.setCode(200);
        baseResp.setMessage("查询成功");
        baseResp.setData(addresses);
        return  baseResp;
    }
}
