package com.asiainfo.casebase.config;

import com.alibaba.fastjson.JSONObject;

import com.asiainfo.casebase.responseEntity.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @Description Aop切面类
 */
@Aspect
@Slf4j
@Component
public class HttpAspect {

    @Autowired
    HttpServletRequest request;

    private String methodName;
    private long startTime;
    /**
     * 切入点
     */
    @Pointcut("execution(* com.asiainfo.casebase.web.*.*(..))")
    public void controllerPointCut() {
    }

    @Pointcut("execution(* com.asiainfo.casebase.repository.*.*(..))")
    public void daoPointCut() {
    }

    @Pointcut("daoPointCut() || controllerPointCut()")
    public void mergePointCut() {
    }


    @Before("controllerPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        startTime = System.currentTimeMillis();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime1 = dateformat.format(System.currentTimeMillis());
        log.info("执行" + methodName + " ,startTime：" + startTime1);
    }

    @After("controllerPointCut()")
    public void doAfter() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime1 = dateformat.format(System.currentTimeMillis());
        long endTime = System.currentTimeMillis();
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + ", endTime: " + endTime1 + " 耗时为：" + E_time + "ms");
    }

    @Around("controllerPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {

            String url = request.getRequestURL().toString();
            String method = request.getMethod();
            String uri = request.getRequestURI();
            log.info("请求开始，url:{}, method:{},uri:{}",url,method,uri);
            result = pjp.proceed();
            log.info("请求结束");
        } catch (Exception e) {
            result =  JSONObject.toJSONString(new ResultData(500, "服务器出错", false));
            log.error(e.getMessage());
        }
        return result;
    }

}