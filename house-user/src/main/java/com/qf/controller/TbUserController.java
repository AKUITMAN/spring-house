package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.TbUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/tbuser")
public class TbUserController {

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public BaseResp login(@RequestBody TbUser tbUser){
        //验证登录，从subjectUtils中获得主体
        Subject subject = SecurityUtils.getSubject();
        //构建令牌对象
        UsernamePasswordToken token = new UsernamePasswordToken(tbUser.getUserName(), tbUser.getPassword());
        try {
            subject.login(token);
            //可以在此处进行其他业务，比如放至session
        }catch (Exception e){
            System.out.println("登陆失败");
        }
        BaseResp baseResult=new BaseResp();
        if (subject.isAuthenticated()){
            baseResult.setCode(200);
            baseResult.setMessage("登陆成功");
            return baseResult;
        }else {
            baseResult.setCode(201);
            baseResult.setMessage("登陆失败");
            return baseResult;
        }
    }
}
