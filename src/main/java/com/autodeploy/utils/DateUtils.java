package com.autodeploy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 字符串日期 转 Date类型
     * @param timeString
     * @return
     */
    public static Date StringToDate(String timeString) {
        if (timeString == null) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(timeString);
        } catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /***
     * date to String
     **/
    public static String dateToString(Date date) {
        String str = "";
        try {
            str = sdf.format(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public static String dateToStringFormat(Date date, String format){
        String str = "";
        try {
            if(format == null) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            str = sdf.format(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }


}
