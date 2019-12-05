package com.asiainfo.casebase.web;


import com.asiainfo.casebase.config.jwt.PassToken;
import com.asiainfo.casebase.config.jwt.TokenService;
import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.SqlService;
import com.asiainfo.casebase.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Api("CommonController相关API")
@RestController
public class CommonController {

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private SqlService sqlService;

    @Autowired
    private TokenService tokenService;


    /***
     * @Description 从cas中获取用户
     **/
    @ApiOperation(value="从cas中获取用户",notes="从cas中获取用户",httpMethod = "GET")
    @RequestMapping(value="/getLoginUser",method= RequestMethod.GET)
    public Object getLoginUserName(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        Object attribute = request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if(attribute != null) {
            Assertion assertion = (Assertion)attribute;
            String id = assertion.getPrincipal().getName();

            Cookie[] cookies = request.getCookies();


            CasUser casUser = userService.findUserFromCas();
//            try {
//                ResultData data = userService.getAuthedInfo(id);
//                casUser.setAuth(data.getData());
//            }catch (Exception e){
//                log.error("获取权限信息失败");
//            }
            return new ResultData(200,"登录成功",true,casUser);
        } else {
            return new ResultData(302,"登录失败",false,null);
        }
    }

    /**
     * 单点登录
     **/
    @PassToken
    @RequestMapping(value="/login",method= RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        Object attribute = session.getAttribute("_const_cas_assertion_");
        String id = null;
        if(null != attribute) {
            Assertion assertion = (Assertion)attribute;
            id = assertion.getPrincipal().getName();
            CasUser casUser = userService.findUserById(id);
            if(casUser == null) {
                log.error("登录失败,用户不存在!");
                //return new ResultData(401,"登录失败,用户不存在!",false);
            } else {
                String token = tokenService.getToken(casUser);
                log.info("token",token);
                Cookie cookie = new Cookie("token",token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        //        //重定向
        response.sendRedirect(env.getProperty("success"));

        //return new ResultData(401,"登录失败",false);

    }


    /**
     * @Description 单点登出
     **/
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResultData loginOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            session.removeAttribute("_const_cas_assertion_");
            session.invalidate();

            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            response.addCookie(cookie);

            String logoutUrl = env.getProperty("cas.server-url-prefix") + "/logout?service=" + env.getProperty("success");
            return new ResultData(200,"退出成功",true,logoutUrl);

        }catch (Exception e){
            log.error("退出异常：" + e.toString());
            return new ResultData(-1,"退出失败",false);
        }
    }


    @ApiOperation(value="统一查询接口",notes="接口参数为String的查询接口",httpMethod = "GET")
    @RequestMapping(value="jsonData",method= RequestMethod.GET)
    public Object jsonData(@RequestParam(value = "params", required = false, defaultValue = "") String params){
        return sqlService.executeSql(params);
    }

    @ApiOperation(value="统一查询接口",notes="接口参数为Map的查询接口",httpMethod = "POST")
    @RequestMapping(value="jsonDataMap",method= RequestMethod.POST)
    public ResultData jsonDataMap(@RequestBody Map<String, String> map){
        try {
            ResultData resultData = sqlService.executeSql(map);
            if(resultData.isSuccess()) {
                return new ResultData(200,"查询成功",true,resultData.getData());
            }
            return new ResultData(-1,resultData.getMsg(),false);
        }catch (Exception e){
            log.error("查询异常",e);
            return new ResultData(-1,"查询失败",false);
        }

    }
}
