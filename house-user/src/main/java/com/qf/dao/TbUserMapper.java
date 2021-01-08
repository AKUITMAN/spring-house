package com.qf.dao;

import com.qf.pojo.vo.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TbUserMapper {
    TbUser findByUserName(@Param("username") String username);
}
