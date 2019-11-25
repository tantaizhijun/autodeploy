//package com.asiainfo.casebase.config.db;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.boot.context.properties.bind.Bindable;
//import org.springframework.boot.context.properties.bind.Binder;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.env.Environment;
//import org.springframework.core.type.AnnotationMetadata;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * 动态数据源注册
// * 实现 ImportBeanDefinitionRegistrar 实现数据源注册
// * 实现 EnvironmentAware 用于读取application.yml配置
// */
//public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
//
//    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
//
//    //指定默认数据源(springboot2.0默认数据源是hikari如何想使用其他数据源可以自己配置)
//    private static final String DATASOURCE_TYPE_DEFAULT = "com.zaxxer.hikari.HikariDataSource";
//
//    //默认数据源
//    private DataSource defaultDataSource;
//
//    //用户自定义数据源
//    private Map<String, DataSource> slaveDataSources = new HashMap<>();
//
//    private Binder binder;
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        binder = Binder.get(environment);
//        initDefaultDataSource(environment);
//        initslaveDataSources(environment);
//    }
//
//    // 读取主数据源
//    private void initDefaultDataSource(Environment env) {
//
//        Properties dataSourceInfo = binder.bind("spring.datasource.default", Bindable.of(Properties.class)).get();
//        HikariConfig config = new HikariConfig(dataSourceInfo);
//        HikariDataSource dataSource  = new HikariDataSource(config);
//        defaultDataSource = dataSource;
//
////        Map<String, String> dsMap = new HashMap<>();
////        String dataSourcePrefix = "spring.datasource.master";
////        dsMap.put("driver", env.getProperty(dataSourcePrefix + ".driver-class-name"));
////        dsMap.put("url", env.getProperty(dataSourcePrefix + ".url"));
////        dsMap.put("username", env.getProperty(dataSourcePrefix + ".username"));
////        dsMap.put("password", env.getProperty(dataSourcePrefix + ".password"));
////        dsMap.put("type", env.getProperty(dataSourcePrefix + ".type"));
////        dsMap.put("key","master");
////        defaultDataSource = buildDataSource(dsMap);
//    }
//    //从数据源
//    private void initslaveDataSources(Environment env) {
//        List<Map> configs = binder.bind("spring.datasource.cluster", Bindable.listOf(Map.class)).get();
//        for (Map config : configs) {
//            // 多个数据源
//            Map<String, String> dsMap = new HashMap<>();
//            dsMap.put("driver", (String)config.get("driver-class-name"));
//            dsMap.put("url", (String)config.get("url"));
//            dsMap.put("username", (String)config.get("username"));
//            dsMap.put("password", (String)config.get("password"));
//            dsMap.put("key",(String)config.get("key"));
//            dsMap.put("type", (String)config.get("type"));
//            dsMap.put("dbName", (String)config.get("dbName"));
//
//            DataSource ds = buildDataSource(dsMap);
//            slaveDataSources.put( config.get("key").toString(), ds);
//        }
//    }
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
//        Map<Object, Object> targetDataSources = new HashMap();
//
//        //添加默认数据源
//        targetDataSources.put("master", this.defaultDataSource);
//        DataSourceHolder.dataSourceIds.add("master");
//
//        //添加其他数据源
//        targetDataSources.putAll(slaveDataSources);
//        for (String key : slaveDataSources.keySet()) {
//            DataSourceHolder.dataSourceIds.add(key);
//        }
//
//        //创建DynamicDataSource
//        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//        beanDefinition.setBeanClass(DynamicDataSource.class);
//        beanDefinition.setSynthetic(true);
//
//        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
//        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
//        mpv.addPropertyValue("targetDataSources", targetDataSources);
//        //注册 - BeanDefinitionRegistry
//        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
//
//        logger.info("Dynamic DataSource Registry");
//    }
//
//    public DataSource buildDataSource(Map<String, String> dataSourceMap) {
//        try {
//            Object type = dataSourceMap.get("type");
//            if (type == null) {
//                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
//            }
//            String driverClassName = dataSourceMap.get("driver");
//            String url = dataSourceMap.get("url");
//            String username = dataSourceMap.get("username");
//            String password = dataSourceMap.get("password");
//
//
//            Class<? extends DataSource> dataSourceType;
//            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
//            DataSourceBuilder factory = DataSourceBuilder.create()
//                    .driverClassName(driverClassName)
//                    .url(url)
//                    .username(username)
//                    .password(password)
//                    .type(dataSourceType);
//            return factory.build();
//
//        } catch (ClassNotFoundException e) {
//            logger.error("build DataSource error: ",e);
//        }
//        return null;
//    }
//
//}
