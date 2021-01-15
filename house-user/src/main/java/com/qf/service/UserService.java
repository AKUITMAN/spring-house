package com.qf.service;

import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    BaseResp registry(User user);

    BaseResp editStatus(Integer id);

    BaseResp login(User user);

    BaseResp getUser(String token);
}
