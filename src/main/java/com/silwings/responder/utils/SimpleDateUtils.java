package com.silwings.responder.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName SimpleDateUtils
 * @Description SimpleDateUtils
 * @Author Silwings
 * @Date 2021/8/8 17:55
 * @Version V1.0
 **/
public class SimpleDateUtils {

    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

}
