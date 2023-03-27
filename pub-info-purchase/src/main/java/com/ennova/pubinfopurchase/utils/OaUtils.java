package com.ennova.pubinfopurchase.utils;

import cn.hutool.core.date.DateUtil;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/20
 */
public class OaUtils {

    public static String getSerialNumber(String serialNumber) {
        String dayStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        String[] dayArr = dayStr.split("-");

        String year = dayArr[0];
        String month = dayArr[1];
        String day = dayArr[2];
        if (serialNumber.equals("")) {
            String newNumber = year + month + day + "0001";
            return newNumber;
        }

        String year2 = serialNumber.substring(0, 4);
        String month2 = serialNumber.substring(4, 6);
        String day2 = serialNumber.substring(6, 8);

        DecimalFormat decimalFormat = new DecimalFormat("0000");

        // 如果日期一致，相加
        if (year.equals(year2) && month.equals(month2) && day.equals(day2)) {
            return year + month + day + decimalFormat.format(Integer.parseInt(serialNumber.substring(8, 12)) + 1);
        }

        // 日期不一致，获取一个新的日期
        String newNumber = year + month + day + "0001";
        return newNumber;
    }

}
