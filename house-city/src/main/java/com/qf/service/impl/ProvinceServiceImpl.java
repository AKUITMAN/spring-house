package com.qf.service.impl;

import com.qf.dao.ProvinceRepository;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.Province;
import com.qf.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;

    @Override
    public BaseResp findAll() {
        BaseResp baseResp = new BaseResp();
        List<Province> all = provinceRepository.findAll();
        baseResp.setCode(200);
        baseResp.setData(all);
        return baseResp;
    }
}
