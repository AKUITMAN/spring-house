package com.qf.dao;

import com.qf.pojo.vo.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    List<Address> AddressfindAll();
}
