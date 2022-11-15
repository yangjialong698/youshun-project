package com.ennova.pubinfocommon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class FileNameUtils {

    public static String FORMAT_LONG = "yyyyMMddhhmmss";

    /** 导出文件名 **/
    public String getfileName(){
        StringBuilder name = new StringBuilder(Objects.requireNonNull(dateToString(new Date(), FORMAT_LONG)));
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(10);
            name.append(num);
        }
        return name.toString();
    }

    public static String dateToString(Date date, String format){
        SimpleDateFormat formatter  = new SimpleDateFormat(format);
        return formatter.format(date);
    }

}
