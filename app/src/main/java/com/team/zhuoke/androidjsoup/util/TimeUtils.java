package com.team.zhuoke.androidjsoup.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ztw on 2016/11/14.
 */

public class TimeUtils {

    /**
     * 把日期格式化为时期时间格式
     */
    private static ThreadLocal<DateFormat> THREADLOCAL_DATETIME_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String timeStamp2DateStr(Long millSec)
    {
        return THREADLOCAL_DATETIME_FORMAT.get().format(millSec);
    }
}
