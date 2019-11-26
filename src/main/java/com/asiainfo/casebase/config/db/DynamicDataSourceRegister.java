package com.asiainfo.casebase.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 动态数据源注册
 * 实现 ImportBeanDefinitionRegistrar 实现数据源注册
 * 实现 EnvironmentAware 用于读取application.yml配置
 */
@Configuration
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    //默认数据源
    private DataSource defaultDataSource;

    //用户自定义数据源
    private Map<String, DataSource> slaveDataSources = new HashMap<>();

    private Binder binder;


    @Override
    public void setEnvironment(Environment environment) {
        binder = Binder.get(environment);
        initDefaultDataSource(environment);
        initslaveDataSources(environment);
    }

    // 读取主数据源
    private void initDefaultDataSource(Environment env) {

        Properties dataSourceInfo = binder.bind("spring.datasource.default", Bindable.of(Properties.class)).get();
        HikariConfig config = new HikariConfig(dataSourceInfo);
        HikariDataSource dataSource  = new HikariDataSource(config);
        defaultDataSource = dataSource;
    }
    //从数据源
    private void initslaveDataSources(Environment env) {
        String dbStr = env.getProperty("spring.datasource.names");
        String[] dbArr = dbStr.split(",");
        for (int i = 0; i < dbArr.length; i++) {
            Properties dataSourceInfo = binder.bind("spring.datasource." + dbArr[i], Bindable.of(Properties.class)).get();
            HikariConfig config = new HikariConfig(dataSourceInfo);
            HikariDataSource dataSource  = new HikariDataSource(config);

            slaveDataSources.put(dbArr[i], dataSource);

        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<Object, Object> targetDataSources = new HashMap();

        //添加默认数据源
        targetDataSources.put("default", this.defaultDataSource);
        DataSourceHolder.dataSourceIds.add("default");

        //添加其他数据源
        targetDataSources.putAll(slaveDataSources);
        for (String key : slaveDataSources.keySet()) {
            DataSourceHolder.dataSourceIds.add(key);
        }

        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);

        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        //注册 - BeanDefinitionRegistry
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);

        log.info("Dynamic DataSource Registry");
    }

}
