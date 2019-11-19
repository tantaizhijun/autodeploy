package com.asiainfo.casebase.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryMysql",  //实体管理工厂引用名称
        transactionManagerRef="transactionManagerMysql",      //事务管理工厂引用名称
        basePackages= {"com.asiainfo.casebase.repository"}) //设置Repository所在位置
public class JpaDataSourceConfig {

    @Autowired
    private Environment env;

    private String entity = "com.asiainfo.casebase.entity";


    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("dynamicDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource () {

        DynamicDataSource dds = new DynamicDataSource();
        Map<Object, Object> map = new HashMap<>();

        String dataSourceStr = env.getProperty("spring.datasource.names");
        String [] dataSourceNameList = dataSourceStr.split(",");

        Binder binder = Binder.get(env);

        for(String dbName:dataSourceNameList){
            Properties dataSourceInfo = binder.bind("spring.datasource."+dbName, Bindable.of(Properties.class)).get();
            HikariConfig config = new HikariConfig(dataSourceInfo);
            HikariDataSource dataSource  = new HikariDataSource(config);
            dataSource.setLeakDetectionThreshold(60 * 1000);
            dataSource.setMaxLifetime(60000);
            dataSource.setIdleTimeout(60000);
            dataSource.setMaximumPoolSize(15);
            dataSource.setMinimumIdle(10);
            dataSource.setConnectionTimeout(60000);

            map.put(dbName,dataSource);
        }
        dds.setTargetDataSources(map);
        DataSourceHolder.setDataSource("default");//设定默认数据源
        return dds;
    }

    @Bean(name = "entityManagerFactoryMysql")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary () {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        //需要进行测试，确定Oracle1的动态数据源是否可用
        vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.mysql.dialect"));

        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(entity);
        factory.setDataSource(dynamicDataSource ());
        return factory;
    }
    /**
     * 配置事物管理器
     */
    @Bean(name = "transactionManagerMysql")
    public PlatformTransactionManager transactionManagerPrimary() {
        return new JpaTransactionManager(entityManagerFactoryPrimary().getObject());
    }
}