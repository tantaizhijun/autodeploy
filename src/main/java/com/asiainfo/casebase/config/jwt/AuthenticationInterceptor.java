package com.asiainfo.casebase.config.jwt;


import com.asiainfo.casebase.config.cas.CasUtil;
import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Desc 拦截器
 *      获取token并验证token
 **/
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);


    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        try {
            // 如果不是映射到方法直接通过
            if(!(object instanceof HandlerMethod)) {
                return true;
            }

            //获取token
            String token = request.getHeader("token");
            if(token == null) {
                Cookie[] cookies = request.getCookies();
                if(cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        Cookie cookie = cookies[i];
                        log.info("cookie:{}",cookie.getName() + ":" + cookie.getValue());
                        if("token".equals(cookie.getName())) {
                            token = cookie.getValue();
                        }
                    }
                }
            }

            HandlerMethod handlerMethod = (HandlerMethod) object;
            Method method = handlerMethod.getMethod();

            //检查是否有passtoken注释，有则跳过认证
            if (method.isAnnotationPresent(PassToken.class)) {
                PassToken passToken = method.getAnnotation(PassToken.class);
                if (passToken.required()) {
                    return true;
                }
            }

            if (token == null) {
                throw new RuntimeException("没有携带认证token，请重新登录");
            }

            String userId;
            try {
                userId = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                log.error("token decode error: " ,j);
                throw new RuntimeException("401");
            }

            CasUser casUser = userService.findUserById(userId);
            if(casUser == null) {
                throw new RuntimeException("用户不存在，请重新登录");
            }
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(casUser.getPassword())).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                log.error("401 token 认证不通过");
                throw new RuntimeException("401：认证不通过");
            }
            return true;
        }catch (Exception e){
            log.error("jwt认证异常，异常信息：" + e.toString());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
