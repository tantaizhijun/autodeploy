package com.asiainfo.casebase.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Date 2019-06-18 11:58
 * @Version 1.0
 **/
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {


    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //设置允许跨域的路径
        registry.addMapping("/**")
                .allowedOrigins(env.getProperty("Access-Control-Allow-Origin"))
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
