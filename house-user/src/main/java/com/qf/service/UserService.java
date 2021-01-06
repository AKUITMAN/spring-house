package com.qf.service;

import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.User;

public interface UserService {
    BaseResp registry(User user);

    BaseResp editStatus(Integer id);
}
