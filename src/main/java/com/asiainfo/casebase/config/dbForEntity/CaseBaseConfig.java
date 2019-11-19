package com.asiainfo.casebase.config.dbForEntity;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Desc 本系统: 案例库数据源配置
 **/
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryCase",    //实体管理工厂引用名称
        transactionManagerRef = "transactionManagerCase",        //事务管理工厂引用名称
        basePackages = {"com.asiainfo.casebase.repository.casebase"})        //Repository所在位置
public class CaseBaseConfig {

    private String entityPackage = "com.asiainfo.casebase.entity.casebase";

    private String databasePrefix = "spring.datasource.default";

    @Autowired
    private Environment env;

    @Autowired
    private PlatformUtil platformUtil;

    @Bean(name = "defaultJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "defaultDataSource")
    @Qualifier("defaultDataSource")
    public DataSource defaultDataSource() {
        Binder binder = Binder.get(env);
        Properties dataSourceInfo = binder.bind(databasePrefix, Bindable.of(Properties.class)).get();
        HikariConfig config = new HikariConfig(dataSourceInfo);
        HikariDataSource dataSource  = new HikariDataSource(config);

        dataSource.setConnectionTestQuery("Select 1");
        dataSource.setIdleTimeout(60000);
        return dataSource;
    }

    @Bean(name = "entityManagerFactoryCase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryCase() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(platformUtil.getPlatform(databasePrefix));
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(entityPackage);
        factory.setDataSource(defaultDataSource());
        return factory;
    }

    /**
     * 配置事物管理器
     */
    @Bean(name = "transactionManagerCase")
    public PlatformTransactionManager transactionManagerPrimary() {
        return new JpaTransactionManager(entityManagerFactoryCase().getObject());
    }
}