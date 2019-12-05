package com.asiainfo.casebase.config.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc PassToken注解
 *      1.该注解用于方法上，表明此方法是否跳过用户验证，
 *      2.值默认为true，表示无需登录即可访问
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
    boolean required() default true;
}
