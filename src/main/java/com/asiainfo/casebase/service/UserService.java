package com.asiainfo.casebase.service;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.repository.casUser.CasUserRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

/**
 * @Desc 用户相关业务
 **/
@Slf4j
@Service
public class UserService {

    @Autowired
    private CasUserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;


    /**
     * @Desc 根据id查询用户，从cas表
     **/
    public CasUser findUserById(String id) {
        if(null == id) {
            return null;
        }
        Optional<CasUser> casUserOptional = userRepository.findById(id);

        if(casUserOptional != null && casUserOptional.isPresent()) {
            return casUserOptional.get();
        }
        log.error("从cas表获取用户为空，id:" + id);
        return null;
    }

    /**
     * @Desc 获取当前用户登录的cas用户信息
     **/
    public CasUser findUserFromCas() {
        return findUserById(getUserIdFromCas());
    }

    /**
     * @Desc 从cas中获取用户ID
     **/
    public String getUserIdFromCas() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object attribute = request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if(attribute != null) {
            Assertion assertion = (Assertion)attribute;
            String id = assertion.getPrincipal().getName();
            if(StringUtils.isEmpty(id)) {
                log.error("从cas获取用户id为空" + id);
            }
            return id;
        }
        log.error("从cas获取用户id为空");
        return "user";
    }



    /**
     * @Description 获取权限
     **/
    public ResultData getAuthedInfo(String username){

        HashMap<Object, Object> map = new HashMap<>();

        map.put("system_name","tycj");
        map.put("user_id",username);
        JSONObject body = null;
        try {
            for (int i = 0; i < 3; i++) {
                ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(env.getProperty("authInfo"), map, JSONObject.class);
                if(responseEntity.getStatusCodeValue() == 200) {
                    body = responseEntity.getBody();
                    if("200".equals(String.valueOf(body.get("status")))) {
                        Object data = body.get("data");
                        return new ResultData(200,"查询成功",true,data);
                    } else {
                        log.error("第" + (i+1) + "次获取权限信息失败，原因：" + body.get("message"));
                    }
                } else {
                    log.error("第" + (i+1) + "次请求权限失败");
                }
            }
        }catch (Exception e){
            log.error("请求权限异常");
        }
        return new ResultData(-1,body == null ? null:String.valueOf(body.get("message")),false,body == null ? null : body.get("data"));
    }

}
