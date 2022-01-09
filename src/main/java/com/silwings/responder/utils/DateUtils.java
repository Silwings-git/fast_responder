package com.silwings.responder.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName DateUtils
 * @Description DateUtils
 * @Author Silwings
 * @Date 2021/8/8 17:55
 * @Version V1.0
 **/
public class DateUtils {

    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD_HHMMSS_SSSZ = "yyyy-MM-dd HH:mm:ss.SSSZ";

    public static String format(final Date date, final String pattern) {

        if (date == null || StringUtils.isBlank(pattern)) {
            return null;
        }

        return getDateFormat(pattern).format(date);
    }

    private static DateFormat getDateFormat(final String pattern) {

        final DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat;
    }

}
