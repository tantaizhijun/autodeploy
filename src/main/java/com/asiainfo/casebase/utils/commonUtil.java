package com.asiainfo.casebase.utils;

import java.util.UUID;

/**
 * @Desc 工具类
 **/
public class commonUtil {


    /**
     * @Desc 获取UUID
     **/
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }



}
