package com.silwings.responder.utils;

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

    public static <T> T toObj(final T obj, final T defaultValue) {
        return null == obj ? defaultValue : obj;
    }

}
