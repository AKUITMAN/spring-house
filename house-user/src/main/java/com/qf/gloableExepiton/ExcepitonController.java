package com.qf.gloableExepiton;

import com.qf.pojo.resp.BaseResp;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExcepitonController {
    @ExceptionHandler(UnauthorizedException.class)
    public BaseResp AuthonewrizationEx(){
        BaseResp baseResult = new BaseResp();
        baseResult.setCode(300);
        baseResult.setMessage("无权限访问");
        return baseResult;
    }
    @ExceptionHandler(UnauthenticatedException.class)
    public BaseResp unauthenticatedException(){
        BaseResp baseResult = new BaseResp();
        baseResult.setCode(400);
        baseResult.setMessage("未登录");
        return baseResult;
    }
}
