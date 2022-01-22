package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName RequestContext
 * @Description 请求上下文
 * @Author Silwings
 * @Date 2022/1/3 19:08
 * @Version V1.0
 **/
public class ConvertUtils {

    private ConvertUtils() {
        // No codes.
    }

    public static String toString(final String source) {
        return ConvertUtils.toString(source,null);
    }

    public static String toString(final String source, final String defaultValue) {
        return StringUtils.isBlank(source) ? defaultValue : source;
    }

    public static <T> T getOrDefault(final T obj) {

        return ConvertUtils.getOrDefault(obj, null);
    }

    public static <T> T getOrDefault(final T obj, final T defaultValue) {
        return null == obj ? defaultValue : obj;
    }

    public static String toStringOrJsonString(final Object obj) {

        if (null == obj) {
            return null;
        }

        return obj instanceof String ? (String) obj : JSON.toJSONString(obj);
    }
}
