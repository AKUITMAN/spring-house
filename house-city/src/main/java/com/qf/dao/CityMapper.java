package com.qf.dao;

import com.qf.pojo.vo.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CityMapper {
    List<City> findByPid(@Param("pid")Integer pid);
}
