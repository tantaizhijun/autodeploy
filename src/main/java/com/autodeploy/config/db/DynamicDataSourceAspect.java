package com.autodeploy.config.db;

import com.autodeploy.anno.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Aspect
@Component
@Slf4j
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DataSource ds) {
        MethodSignature methodSignature = (MethodSignature)point.getSignature();

        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = point.getArgs();
        List<String> list = Arrays.asList(parameterNames);
        String dsName = "";
        if(list.contains(ds.name())) {
            int index = list.indexOf(ds.name());
            if(args[index] != null) {
                dsName = args[index].toString();
            }
        }
        if (!DataSourceHolder.isContainsDataSource(dsName)) {
            log.error("数据源[{}]不存在，使用默认数据源 > {}", dsName, point.getSignature());
        } else {
            log.info("Use DataSource : {} > {}", dsName, point.getSignature());
            DataSourceHolder.setDataSource(dsName);
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
        DataSourceHolder.removeDataSource();
    }
}