package com.ennova.pubinfocommon.utils;

import java.util.Random;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-22
 */
public class RandomUtil {
    /**
     * 获取随机数字
     *
     * @param length 随机长度
     * @return
     */
    public static String randomNum(int length) {
        StringBuffer randomStr = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomStr.append(random.nextInt(10));
        }
        return randomStr.toString();
    }
}
