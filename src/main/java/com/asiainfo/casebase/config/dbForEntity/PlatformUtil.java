package com.asiainfo.casebase.config.dbForEntity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Desc 获取数据库类型
 **/
@Service
public class PlatformUtil {

    @Autowired
    private Environment env;


    /**
     * @Desc 根据数据库连接串 获取数据库类型
     **/
    public String getPlatform(String datasourcePrefix) {
        Binder binder = Binder.get(env);
        Map<String, String> dataSourceInfo = binder.bind(datasourcePrefix, Bindable.of(Map.class)).get();
        String jdbcUrl = dataSourceInfo.get("jdbcUrl");
        String platform = "";
        if(StringUtils.containsIgnoreCase(jdbcUrl,"oracle")) {
            platform = env.getProperty("hibernate.oracle.dialect");
        } else if (jdbcUrl.contains("mysql")) {
            platform = env.getProperty("hibernate.mysql.dialect");
        }
        return platform;
    }
}
