package com.qf.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 54110 on 2020/12/24.
 */
@Component
public class JWTUtils {

    String issu = "qf-live";

    public String token(Map map){
        //加密算法
        Algorithm algorithmHS = Algorithm.HMAC256("qianfengjavaniubi");
        //计算过期时间
        long l = System.currentTimeMillis();

        l+=60*60*1000;

        Date date = new Date(l);
        //自定义头部
        Map headMap = new HashMap<>();
        headMap.put("alg","HS256");
        headMap.put("typ","jwt");
        //签发人


        String sign = JWT.create().withHeader(headMap).
                withSubject("token").withIssuer(issu).withClaim("body", map).withIssuedAt(new Date()).withExpiresAt(date).sign(algorithmHS);
        return sign;
    }


    public Map Verify(String token){
        Algorithm algorithm = Algorithm.HMAC256("qianfengjavaniubi");

        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issu).build();

        try{
            DecodedJWT verify = verifier.verify(token);
            Claim body = verify.getClaim("body");
            Map<String, Object> stringObjectMap = body.asMap();
            return stringObjectMap;
        }catch (Exception e){

            return null;
        }


    }
}

