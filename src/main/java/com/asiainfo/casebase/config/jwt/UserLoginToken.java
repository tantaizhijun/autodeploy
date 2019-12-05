package com.asiainfo.casebase.config.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc UserLoginToken注解
 *      1.该注解用于方法上，表明该方法是否需要登录才能进行操作，
 *      2.false为无需登录（即不进行验证用户），值默认为true
 *
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
    boolean required() default true;
}
