package com.qf.dao;

import com.qf.pojo.resp.HouseResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {
    HouseResp findById(@Param("id") Integer id);
}
