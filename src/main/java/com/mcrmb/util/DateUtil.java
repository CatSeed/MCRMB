package com.mcrmb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这个类提供了操作日期的实用方法。
 */

public class DateUtil {

    /**
     * 将时间戳转换为日期
     *
     * @param timestamp 时间戳
     * @return 日期字符串
     */
    public static String convertTimestampToDate(String timestamp) {
        long seconds = new Long(timestamp) * 1000L;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(seconds));
    }

    /**
     * 返回当前时间戳，以秒为单位。此方法将系统当前时间转换为Unix时间戳格式，并返回字符串类型的结果。
     *
     * @return 当前时间戳的字符串表示形式
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }
}
