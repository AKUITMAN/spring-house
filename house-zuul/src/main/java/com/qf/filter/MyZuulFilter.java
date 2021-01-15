package com.qf.filter;

import com.qf.pojo.resp.BaseResp;
import com.qf.utils.CookieUtils;
import com.qf.utils.JWTUtils;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qf.pojo.resp.BaseResp;
import com.qf.utils.CookieUtils;
import com.qf.utils.JWTUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 54110 on 2020/12/24.
 */
@Component
public class MyZuulFilter extends ZuulFilter {
    //拦截器何时进行拦截

    private static final List URL_LIST= new ArrayList<>();
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
    //拦截器的顺序
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }
    //对于哪些请求进行拦截
    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        /*if (split[2].equals("editStatus")){
            return false;
        }*/
        //放行editStatus
        for (String str:split
             ) {
            if (str.equals("editStatus")){
                return false;
            }
        }
        if (URL_LIST.contains(requestURI)){
            //放行login请求
            return false;
        }
        return true;
    }
    //拦截后执行的方法
    @Override
    public Object run() throws ZuulException {
       //进行用户的验证 是否登录
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        //声明返回值
        BaseResp baseResp = new BaseResp();
        //从request中获取token
        Cookie[] cookies = request.getCookies();
        if (cookies==null||cookies.length==0){
            response.setContentType("application/json;charset=utf-8");
            baseResp.setMessage("未登录");
            baseResp.setCode(20001);
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.print(JSONObject.toJSON(baseResp));
            currentContext.setSendZuulResponse(false);
            return baseResp;

        }
        CookieUtils cookieUtils = new CookieUtils();
        String token = cookieUtils.getToken(cookies);
        //使用jwt进行解密
        JWTUtils jwtUtils = new JWTUtils();
        Map verify = jwtUtils.Verify(token);

        if (verify==null||verify.get("username")==null){
            response.setContentType("application/json;charset=utf-8");
            baseResp.setMessage("登录失效，重新登录");
            baseResp.setCode(20002);

            baseResp.setCode(20001);
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.print(JSONObject.toJSON(baseResp));
            currentContext.setSendZuulResponse(false);
            return baseResp;

        }
        return null;
    }

    //无参构造
    public MyZuulFilter(){
        URL_LIST.add("/house-user/user/login");
        URL_LIST.add("/house-user/tbuser/login");
        URL_LIST.add("/house-user/user/registry");
        URL_LIST.add("/house-user/user/editStatus");
        URL_LIST.add("/house-user/user/getUser");
        URL_LIST.add("/house-city/city/findAll");
        URL_LIST.add("/house-city/city/findByPid");
//        URL_LIST.add("/qfshop-user/user/login");
//        URL_LIST.add("/qfshop-goods/goods/findAll");
        URL_LIST.add("/house-search/search/selectKey");
        URL_LIST.add("/house-address/address/addressfindAll");
        URL_LIST.add("/house-search/search/selectHouse");
        URL_LIST.add("/house-house/house/findById");
        URL_LIST.add("/house-province/province/provincefindAll");
        URL_LIST.add("/house-order/order/findByUserId");
    }
}
