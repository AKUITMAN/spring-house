package com.qf.dao;


import com.qf.pojo.vo.Province;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProvinceMapper {
    List<Province> ProvincefindAll();
}
