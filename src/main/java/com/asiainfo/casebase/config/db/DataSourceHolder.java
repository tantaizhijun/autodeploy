package com.asiainfo.casebase.config.db;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc:数据源上下文
 */
@Slf4j
public class DataSourceHolder {

    /**
     * 存储已经注册的数据源的key
     */
    public static final List<String> dataSourceIds = new ArrayList<>();

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static String getDataSource () {
        return HOLDER.get() == null ? "default" : HOLDER.get();
    }

    public static void setDataSource (String dataSourceRouterKey) {
        log.info("切换至{}数据源", dataSourceRouterKey);
        HOLDER.set(dataSourceRouterKey);
    }

    /**
     * 移除数据源
     */
    public static void removeDataSource () {
        HOLDER.remove();
    }

    /**
     * 判断指定DataSrouce当前是否存在
     *
     * @param dataSourceId
     * @return
     */
    public static boolean isContainsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }

}
