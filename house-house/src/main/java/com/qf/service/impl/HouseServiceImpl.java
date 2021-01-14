package com.qf.service.impl;

import com.qf.dao.HouseMapper;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.resp.HouseResp;
import com.qf.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    HouseMapper houseMapper;

    @Override
    public BaseResp findById(Map map) {
        Integer id = (Integer) map.get("id");
        BaseResp baseResp = new BaseResp();
        if (id!=null){
            HouseResp byId = houseMapper.findById(id);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(byId);
            baseResp.setData(objects);
            baseResp.setMessage("查询成功");
            baseResp.setCode(200);
        }else {
            baseResp.setMessage("查询失败");
            baseResp.setCode(201);
        }
        return baseResp;
    }
}
