package com.qf.service.impl;


import com.qf.dao.ProvinceMapper;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.Address;
import com.qf.pojo.vo.Province;
import com.qf.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    ProvinceMapper provinceMapper;

    @Override
    public BaseResp ProvincefindAll() {
        List<Province> provinces = provinceMapper.ProvincefindAll();
        BaseResp baseResp = new BaseResp();
        baseResp.setCode(200);
        baseResp.setMessage("查询成功");
        baseResp.setData(provinces);
        return  baseResp;
    }
}
