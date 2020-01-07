package com.asiainfo.casebase.config.jwt;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Desc token 工具类
 *      生成token
 **/
@Slf4j
@Service
public class TokenService {


    /**
     * @Desc token的生成方法
     **/
    public String createToken(CasUser user) {
        String token="";
        try {
            token= JWT.create().withAudience(user.getId())
                    .sign(Algorithm.HMAC256(user.getPassword()));
        } catch (Exception e){
            log.error("tocken create error: {}",e);
            e.printStackTrace();
        }
        return token;
    }

}
