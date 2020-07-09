package com.autodeploy.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * dbUtils
 */

public class DbUtil {

    private static final Logger log = LoggerFactory.getLogger(DbUtil.class);

    public static List<Map<String,Object>> getStringListMap(List<Map<String, Object>> obj){
        List<Map<String, Object>> result;
        if (obj == null)
            return new ArrayList<>();
        else {
            result = new ArrayList<Map<String, Object>>(obj.size());
            for (Map<String, Object> map : obj) {
                Map<String, Object> newMap = null;
                newMap = getStringStringMap(map, newMap);
                result.add(newMap);
            }
        }
        return result;
    }

    private static Map<String, Object> getStringStringMap(Map<String, Object> obj, Map<String, Object> result) {
        if (obj != null) {
            result = new LinkedHashMap<String, Object>();
            Object value;
            Iterator iter = obj.keySet().iterator();
            String key;
            while (iter.hasNext()) {
                key = iter.next().toString();
                key = key.toLowerCase();
                value = obj.get(key);
                if (value == null) {
                    result.put(key, "");
                }else if (value instanceof Clob) {
                    Clob clob = (Clob) value;
                    try {
                        result.put(key, clob.getSubString(1, (int) clob.length()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (value instanceof Timestamp) {
                    Date date = new Date(((Timestamp) value).getTime());
                    String str = DateUtils.dateToStringFormat(date, "yyyyMMddHHmmss");
                    result.put(key, str);
                } else if(value instanceof  Integer){
                    result.put(key,value.toString());
                }else{
                    result.put(key, value);
                }
            }
        }
        return result;
    }




    /**
     * 替换sql语句中的常量，即占位符&
     *
     * @param sql
     * @param params
     */

    public static Map<String, Object> replaceSqlConstant(String sql, String[] params) throws Exception {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            String[] strs = {"&", "?"};
            List<String> paramsList = new ArrayList<String>();
            int fromIndex, index = 0;
            String sqlTemp = sql;
            while ((fromIndex = StringUtils.indexOfAny(sqlTemp, strs)) > -1) {
                if (sqlTemp.indexOf("&") == fromIndex) {
                    sql = sql.replaceFirst("&", params[index]);
                } else {
                    paramsList.add(params[index]);
                }

                sqlTemp = sqlTemp.substring(fromIndex + 1);
                index++;
            }

            result.put("sql", sql);

            String[] p = paramsList.toArray(new String[paramsList.size()]);
            result.put("params", p);
            return result;
        }catch (Exception e){
            log.error(sql.replaceAll("[\\t\\n\\r]", " ").substring(0,50));
            if(params!=null)
                for (String param:params){
                    log.error("预备替换变量：" + param);
                }
            log.error("替换变量出错：",e);
            throw new Exception("替换变量时出错"+e,e);
        }
    }

    static String VAR_SPLIT_STR= "$";

    /**
     * 将params中的变量名替换成变量值
     * params=$prov_code$,$interval$
     *
     * @param varMap prov_code=100,interval=05MI
     * @param param
     * @return params=100,05MI
     */

    public static String [] replaceVar(Map<String, String> varMap, String param) {
        String[] params = param == null ? null : param.split(",");

        if(params == null){
            return null;
        }

        for (String key : varMap.keySet()) {
            for(int i = 0;i<params.length;i++){
                params[i] = StringUtils.replace(params[i], VAR_SPLIT_STR + key + VAR_SPLIT_STR, varMap.get(key));
            }
        }
        return params;
    }

    public static String replaceParam(String sql, Map<String ,String> paramMap) {
        String regex = "\\{@(\\w+)\\}";
        Matcher m 	= Pattern.compile(regex).matcher(sql);
        while (m.find()) {  // {@name} 或 {@nameflg}
            String sqlStr = m.group();
            String field = m.group(1);  // name

            if(!field.endsWith("Flag")){ //{@name}的情况
                String name = paramMap.get(field);
                String flag = "{@"+field+"Flag}";
                String flagvalue ;
                if(name!=null && !"".equals(name)){//前台传入了参数
                    flagvalue = "1"; //条件
                    // 不自动添加单引号
                    // name = "'"+name+"'";
                }else{//前台没有传入参数
                    flagvalue = "0";
                    // 不自动添加单引号
                    //name = "'##'";
                    name = "##";
                }
                sql =  sql.replace(sqlStr,name);
                sql =  sql.replace(flag, flagvalue);

            }
        }

        return sql;
    }
}
