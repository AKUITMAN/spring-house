package com.qf.service.impl;

import com.qf.dao.UserMapper;
import com.qf.dao.UserRepository;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.User;
import com.qf.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRepository userRepository;

    //注册
    @Override
    public BaseResp registry(User user) {
        //将用户信息存储到数据库中，将用户的id以及email地址获取，发送给rabbitmq
        BaseResp baseResp = new BaseResp();
        if (user.getEmail()==null){
            baseResp.setCode(201);
            baseResp.setMessage("邮箱不能为空");
            return baseResp;
        }
        User user1=userRepository.findByEmail(user.getEmail());
        if (user1!=null){
            baseResp.setCode(202);
            baseResp.setMessage("该邮箱已被注册");
            return baseResp;
        }
        user.setStatus(0);
        User user2=userRepository.saveAndFlush(user);
        //发送邮件，激活账号,获取存储后用户的id和邮箱地址
        Map map= new HashMap<>();
        map.put("id",user2.getId());
        map.put("email",user2.getEmail());
        rabbitTemplate.convertAndSend("","send-mail",map);
        baseResp.setCode(200);
        baseResp.setMessage("注册成功，请注意查收邮件，激活账号");
        return baseResp;
    }

    @Override
    public BaseResp editStatus(Integer id) {
        BaseResp baseResp = new BaseResp();
        //通过id查询
        Optional<User> byId=userRepository.findById(id);
        if (!byId.isPresent()){
            baseResp.setCode(201);
            baseResp.setMessage("没有该用户");
            return baseResp;
        }
        User user = byId.get();
        if (user.getStatus()!=null&&user.getStatus()==1){
            baseResp.setCode(202);
            baseResp.setMessage("用户已激活，不能重复激活");
            return baseResp;
        }
        user.setStatus(1);
        userRepository.saveAndFlush(user);
        baseResp.setCode(200);
        baseResp.setMessage("激活成功");
        return baseResp;
    }
}
