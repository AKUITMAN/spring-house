package com.qf.service.impl;

import com.qf.dao.UserMapper;
import com.qf.dao.UserRepository;
import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.User;
import com.qf.service.UserService;
import com.qf.utils.CookieUtils;
import com.qf.utils.JWTUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
        if (user.getUserName()==null){
            baseResp.setCode(204);
            baseResp.setMessage("用户名不能为空");
            return baseResp;
        }
        User byUserName = userRepository.findByUserName(user.getUserName());
        if (byUserName!=null){
            baseResp.setCode(203);
            baseResp.setMessage("用户名重复");
            return baseResp;
        }
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

    @Override
    public BaseResp login(User user) {
        //声明返回值
        BaseResp baseResp = new BaseResp();
        //获取到用户名
        String email = user.getEmail();
        User byEmail = userRepository.findByEmail(email);
        //判断
        if (byEmail==null){
            baseResp.setCode(201);
            baseResp.setMessage("未找到该用户");
            return baseResp;
        }
        if (!user.getPassword().equals(byEmail.getPassword())){
            baseResp.setCode(202);
            baseResp.setMessage("密码错误");
            return baseResp;
        }
        //使用JWT进行加密
        JWTUtils jwtUtils = new JWTUtils();
        //将信息放置在JWT的负载部分
        Map map=new HashMap<>();
        map.put("email",byEmail.getEmail());
        map.put("id",byEmail.getId());
        map.put("userName",byEmail.getUserName());
        map.put("image",byEmail.getImage());
        String token = jwtUtils.token(map);
        baseResp.setCode(200);
        baseResp.setMessage("登陆成功");
        baseResp.setData(token);
        return baseResp;
    }

    @Override
    public BaseResp getUser(HttpServletRequest request) {
        CookieUtils cookieUtils = new CookieUtils();
        Cookie[] cookies = request.getCookies();
        String token = cookieUtils.getToken(cookies);
        //从JWT获取用户信息
        JWTUtils jwtUtils = new JWTUtils();
        Map verify = jwtUtils.Verify(token);
        BaseResp baseResp = new BaseResp();
        baseResp.setCode(200);
        baseResp.setData(verify);
        return baseResp;
    }
}
