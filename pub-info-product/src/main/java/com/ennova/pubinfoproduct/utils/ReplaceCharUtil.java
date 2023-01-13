package com.ennova.pubinfoproduct.utils;

import java.text.DecimalFormat;

public class ReplaceCharUtil {

    static String replaceChar(String str) {
        if(str == null){
            return "";
        }

        for (int i = 10; i < 14; i++) {
            str = str.replaceAll(String.valueOf((char) i), "").replace(" ", ""); // 去掉换行符,回车符,制表符,空格
        }
        return str;
    }

    public static String double2Str(Double d) {
        if(d == null){
            return "0";
        }
        DecimalFormat df   = new DecimalFormat("######0.00"); // 保留两位小数
        return df.format(d);
    }


}
