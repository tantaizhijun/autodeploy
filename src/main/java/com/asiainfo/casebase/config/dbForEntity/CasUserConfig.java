//package com.asiainfo.casebase.config.dbForEntity;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.bind.Bindable;
//import org.springframework.boot.context.properties.bind.Binder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
///**
// * @Desc cas表数据库连接配置
// **/
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactoryCas",    //实体管理工厂引用名称
//        transactionManagerRef = "transactionManagerCas",        //事务管理工厂引用名称
//        basePackages = {"com.asiainfo.casebase.repository.casUser"})        //Repository所在位置
//public class CasUserConfig {
//
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private PlatformUtil platformUtil;
//
//    private String datasourcePrefix = "spring.datasource.cas";
//
//    private String entity = "com.asiainfo.casebase.entity.casUser";
//
//    @Bean(name = "casDataSource")
//    @Qualifier("casDataSource")
//    public DataSource casDataSource() {
//        Binder binder = Binder.get(env);
//
//        Properties dataSourceInfo = binder.bind(datasourcePrefix, Bindable.of(Properties.class)).get();
//        HikariConfig config = new HikariConfig(dataSourceInfo);
//        HikariDataSource dataSource  = new HikariDataSource(config);
//        return dataSource;
//    }
//
//    @Bean(name = "entityManagerFactoryCas")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryCas() {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setDatabasePlatform(platformUtil.getPlatform(datasourcePrefix));
//        vendorAdapter.setShowSql(true);
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan(entity);
//        factory.setDataSource(casDataSource());
//        return factory;
//    }
//
//    /**
//     * 配置事物管理器
//     */
//    @Bean(name = "transactionManagerCas")
//    public PlatformTransactionManager transactionManagerPrimary() {
//        return new JpaTransactionManager(entityManagerFactoryCas().getObject());
//    }
//}