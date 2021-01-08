package com.qf.dao;

import com.qf.pojo.req.TbPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TbPermissionMapper {
    List<TbPermission> findPermissionByUserName(@Param("username") String username);
}
