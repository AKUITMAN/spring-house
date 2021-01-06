package com.qf.dao;

import com.qf.pojo.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 54110 on 2020/12/24.
 */
public interface UserRepository extends JpaRepository<User,Integer>{

    User findByEmail(String email);
}
