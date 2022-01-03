package com.silwings.responder.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


/**
 * 变量转换Utils
 */
public class ConvertUtils {

    private ConvertUtils() {
        // No codes.
    }

    /**
     * toInteger
     *
     * @param obj 待转换对象
     * @return Integer
     * @author zejun.dong
     */
    public static Integer toInteger(Object obj) {

        return toInteger(obj, null);
    }

    /**
     * toInteger
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return Integer
     * @author zejun.dong
     */
    public static Integer toInteger(Object obj, Integer defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return defaultValue;
        }

        return Integer.valueOf(String.valueOf(obj));
    }

    /**
     * toLong
     *
     * @param obj 待转换对象
     * @return Long
     * @author zejun.dong
     */
    public static Long toLong(Object obj) {

        return toLong(obj, null);
    }

    /**
     * toLong
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return Long
     * @author zejun.dong
     */
    public static Long toLong(Object obj, Long defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return defaultValue;
        }

        return Long.valueOf(String.valueOf(obj));
    }

    /**
     * toDouble
     *
     * @param obj 待转换对象
     * @return Double
     * @author zejun.dong
     */
    public static Double toDouble(Object obj) {

        return toDouble(obj, null);
    }

    /**
     * toDouble
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return Double
     * @author zejun.dong
     */
    public static Double toDouble(Object obj, Double defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return defaultValue;
        }

        return Double.valueOf(String.valueOf(obj));
    }

    /**
     * toBigDecimal
     *
     * @param obj 待转换对象
     * @return BigDecimal
     * @author zejun.dong
     */
    public static BigDecimal toBigDecimal(Object obj) {

        return toBigDecimal(obj, null);
    }

    /**
     * toBigDecimal
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return BigDecimal
     * @author zejun.dong
     */
    public static BigDecimal toBigDecimal(Object obj, BigDecimal defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return defaultValue;
        }

        return new BigDecimal(String.valueOf(obj));
    }

    /**
     * toString
     *
     * @param obj 待转换对象
     * @return String
     * @author zejun.dong
     */
    public static String toString(Object obj) {

        return toString(obj, null);
    }

    /**
     * toString
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return String
     * @author zejun.dong
     */
    public static String toString(Object obj, String defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof String) {
            if (StringUtils.isBlank((String) obj)) {
                return defaultValue;
            }
            return (String) obj;
        }

        return String.valueOf(obj);
    }

    /**
     * toBoolean
     *
     * @param obj 待转换对象
     * @return Boolean
     */
    public static Boolean toBoolean(Object obj) {

        return toBoolean(obj, null);
    }

    /**
     * toBoolean
     *
     * @param obj          待转换对象
     * @param defaultValue 默认值
     * @return Boolean
     */
    public static Boolean toBoolean(Object obj, Boolean defaultValue) {

        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            return defaultValue;
        }

        return Boolean.valueOf(String.valueOf(obj));
    }

}
