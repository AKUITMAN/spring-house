package com.qf.service.impl;

import com.qf.dao.CityMapper;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.City;
import com.qf.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityMapper cityMapper;

    @Override
    public BaseResp findByPid(Integer pid) {
        BaseResp baseResp = new BaseResp();
        List<City> byPid = cityMapper.findByPid(pid);
        baseResp.setCode(200);
        baseResp.setData(byPid);
        baseResp.setMessage("查询成功");

        return baseResp;
    }
}
