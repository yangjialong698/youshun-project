package com.ennova.pubinfotask.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String format_normal = "yyyy-MM-dd";
    public static String format_long = "yyyyMMddhhmmss";
    public static String format_normal_time = "yyyy-MM-dd";

    public static String dateToString(Date date, String format){
        SimpleDateFormat formatter  = new SimpleDateFormat(format);
        return formatter.format(date);
    }

}
