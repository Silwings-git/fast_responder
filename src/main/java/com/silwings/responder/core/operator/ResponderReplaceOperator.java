package com.silwings.responder.core.operator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.utils.ConvertUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;

/**
 * @ClassName ResponderReplaceOperator
 * @Description 操作符
 * @Author Silwings
 * @Date 2022/1/4 23:33
 * @Version V1.0
 **/
public enum ResponderReplaceOperator {

    /**
     * 查询替换.从请求参数中寻找并替换标准处的字符串
     */
    SEARCH_REPLACE("${}", arg -> ReplaceOperatorPattern.SEARCH_REPLACE_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::searchReplaceHandle),

    /**
     * 随机boolean值
     */
    RANDOM_BOOLEAN("-RDBoolean()-", arg -> ReplaceOperatorPattern.RANDOM_BOOLEAN_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::randomBoolean),

    /**
     * 随机int值,可指定范围
     */
    RANDOM_INT_BOUND("-RDInt()-", arg -> ReplaceOperatorPattern.RANDOM_INT_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::randomInt),

    /**
     * 随机long值,可指定范围
     */
    RANDOM_LONG("-RDLong()-", arg -> ReplaceOperatorPattern.RANDOM_LONG_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::randomLong),

    /**
     * 随机double值,可指定范围
     */
    RANDOM_DOUBLE("-RDDouble()-", arg -> ReplaceOperatorPattern.RANDOM_DOUBLE_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::randomDouble),

    /**
     * 随机uuid值,可指定长度
     */
    UUID_REPLACE("-UUID()-", arg -> ReplaceOperatorPattern.UUID_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::uuid),

    /**
     * 当前时间时间戳.可指定秒或毫秒
     */
    TIMESTAMP_NOW("-TSNow()-", arg -> ReplaceOperatorPattern.TIMESTAMP_NOW_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::timestamp),

    /**
     * 当前时间时间戳.可指定秒或毫秒
     */
    TIMESTAMP_FORMAT_NOW("-TSFNow()-", arg -> ReplaceOperatorPattern.TIMESTAMP_FORMAT_NOW_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::timestampFormat),

    /**
     * 从公共参数查询替换.从请求参数中寻找并替换标准处的字符串
     */
    PUBLIC_PARAM_SEARCH_REPLACE("$C{}", arg -> ReplaceOperatorPattern.PUBLIC_PARAM_SEARCH_REPLACE_PATTERN.matcher(arg).find(),
            ResponderReplaceOperator::searchCustomizeReplaceHandle),

    /**
     * 什么都不做.返回入参(该操作符意在筛选不到任何操作符时默认返回,这样不需要进行空判)
     */
    DO_NOTHING("", s -> true, (str, paramsAndBody) -> str),
    ;


    private final String operator;
    private final Predicate<String> applyFunction;
    private final BiFunction<String, RequestParamsAndBody, Object> handleFunction;

    ResponderReplaceOperator(final String operator, final Predicate<String> applyFunction, final BiFunction<String, RequestParamsAndBody, Object> handleFunction) {
        this.operator = operator;
        this.applyFunction = applyFunction;
        this.handleFunction = handleFunction;
    }

    /**
     * description: 将指定字符串适用操作符
     * version: 1.0
     * date: 2022/1/6 22:25
     * author: Silwings
     *
     * @param arg                  待适用操作符的参数
     * @param requestParamsAndBody 请求参数信息
     * @return java.lang.Object 替换后的对象.类型要么是String,要么是一个可转换为JSON格式的对象
     */
    public static Object replace(final String arg, final RequestParamsAndBody requestParamsAndBody) {

        if (StringUtils.isBlank(arg)) {
            return "";
        }

        Object result = arg;

        for (ResponderReplaceOperator value : values()) {
            if (value.applyFunction.test(arg)) {
                result = value.handleFunction.apply(ConvertUtils.toStringOrJsonString(result), requestParamsAndBody);
            }
        }

        return result;
    }

    /**
     * description: 查询并替换参数值
     * version: 1.0
     * date: 2022/1/6 10:14
     * author: Silwings
     *
     * @param arg           带有查找替换符的字符串
     * @param paramsAndBody 请求参数集
     * @return java.lang.Object 替换后的新值
     */
    private static Object searchReplaceHandle(final String arg, final RequestParamsAndBody paramsAndBody) {

        String input = arg;

        final HashMap<Object, Object> hashMap = new HashMap<>();

        // 如果input可以转换为一个json,按照对象的类型进行处理
        if (JSON.isValidObject(input)) {
            final Map<String, Object> map = JSON.parseObject(input, new TypeReference<Map<String, Object>>() {
            });
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                final Object realKey = searchReplaceHandle(key, paramsAndBody);

                final Object value = entry.getValue();
                final Object realValue = searchReplaceHandle(value.toString(), paramsAndBody);

                hashMap.put(realKey, realValue);
            }

            return hashMap;

        } else {
            Matcher matcher = ReplaceOperatorPattern.SEARCH_REPLACE_PATTERN.matcher(input);

            while (matcher.find()) {
                final String group = matcher.group();
                final Object param = paramsAndBody.getParam(group.substring(2, group.length() - 1));

                // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
                if (input.length() > group.length()) {
                    input = input.replace(group, getReplacement(param));
                } else {
                    return param;
                }

                matcher = ReplaceOperatorPattern.SEARCH_REPLACE_PATTERN.matcher(input);
            }

            return input;
        }
    }


    private static Object searchCustomizeReplaceHandle(final String arg, final RequestParamsAndBody paramsAndBody) {
        String input = arg;

        final HashMap<Object, Object> hashMap = new HashMap<>();

        // 如果input可以转换为一个json,按照对象的类型进行处理
        if (JSON.isValidObject(input)) {
            final Map<String, Object> map = JSON.parseObject(input, new TypeReference<Map<String, Object>>() {
            });
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                final Object realKey = searchCustomizeReplaceHandle(key, paramsAndBody);

                final Object value = entry.getValue();
                final Object realValue = searchCustomizeReplaceHandle(value.toString(), paramsAndBody);

                hashMap.put(realKey, realValue);
            }

            return hashMap;

        } else {
            Matcher matcher = ReplaceOperatorPattern.PUBLIC_PARAM_SEARCH_REPLACE_PATTERN.matcher(input);

            while (matcher.find()) {
                final String group = matcher.group();
                final Object param = paramsAndBody.getCustomizeParam(group.substring(3, group.length() - 1));

                // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
                if (input.length() > group.length()) {
                    input = input.replace(group, getReplacement(param));
                } else {
                    return param;
                }

                matcher = ReplaceOperatorPattern.PUBLIC_PARAM_SEARCH_REPLACE_PATTERN.matcher(input);
            }

            return input;
        }
    }


    private static String getReplacement(Object param) {
        if (null == param) {
            return "";
        }

        if (param instanceof String) {
            return (String) param;
        }

        if (param instanceof String[]) {
            int length = Array.getLength(param);

            if (0 == length) {
                return "";
            }

            final String[] paramArray = new String[length];
            for (int i = 0; i < paramArray.length; i++) {
                paramArray[i] = (String) Array.get(param, i);
            }

            return String.join(",", paramArray);
        }

        return JSON.toJSONString(param);
    }


    private static Object randomBoolean(final String arg, final RequestParamsAndBody paramsAndBody) {

        Matcher matcher = ReplaceOperatorPattern.RANDOM_BOOLEAN_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            final String replaceValue = Boolean.toString(ThreadLocalRandom.current().nextBoolean());

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.RANDOM_BOOLEAN_PATTERN.matcher(input);
        }

        return input;
    }

    private static Object randomInt(final String arg, final RequestParamsAndBody paramsAndBody) {

        Matcher matcher = ReplaceOperatorPattern.RANDOM_INT_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            String replaceValue;

            final String range = group.replace("-RDInt(", "").replace(")-", "");
            if (0 == range.length()) {

                replaceValue = Integer.toString(ThreadLocalRandom.current().nextInt());
            } else if (range.contains(",")) {

                final String[] split = range.split(",");
                replaceValue = Integer.toString(ThreadLocalRandom.current().nextInt(formatInt(split[0]), formatInt(split[1])));
            } else {

                replaceValue = Integer.toString(ThreadLocalRandom.current().nextInt(formatInt(range)));
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.RANDOM_INT_PATTERN.matcher(input);
        }

        return input;
    }


    private static Object randomLong(final String arg, final RequestParamsAndBody paramsAndBody) {

        Matcher matcher = ReplaceOperatorPattern.RANDOM_LONG_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            String replaceValue;

            final String range = group.replace("-RDLong(", "").replace(")-", "");
            if (0 == range.length()) {

                replaceValue = Long.toString(ThreadLocalRandom.current().nextLong());
            } else if (range.contains(",")) {

                final String[] split = range.split(",");
                replaceValue = Long.toString(ThreadLocalRandom.current().nextLong(formatLong(split[0]), formatLong(split[1])));
            } else {

                replaceValue = Long.toString(ThreadLocalRandom.current().nextLong(formatLong(range)));
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.RANDOM_LONG_PATTERN.matcher(input);
        }

        return input;

    }

    private static Object randomDouble(final String arg, final RequestParamsAndBody paramsAndBody) {
        Matcher matcher = ReplaceOperatorPattern.RANDOM_DOUBLE_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            String replaceValue;

            final String range = group.replace("-RDDouble(", "").replace(")-", "");
            if (0 == range.length()) {

                replaceValue = Double.toString(ThreadLocalRandom.current().nextDouble());
            } else if (range.contains(",")) {

                final String[] split = range.split(",");
                replaceValue = Double.toString(ThreadLocalRandom.current().nextDouble(formatDouble(split[0]), formatDouble(split[1])));
            } else {

                replaceValue = Double.toString(ThreadLocalRandom.current().nextDouble(formatDouble(range)));
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.RANDOM_DOUBLE_PATTERN.matcher(input);
        }

        return input;
    }

    private static double formatDouble(String str) {
        try {

            return Double.parseDouble(str);
        } catch (NumberFormatException e) {

            return Double.MAX_VALUE;
        }
    }


    private static long formatLong(String str) {
        try {

            return Long.parseLong(str);
        } catch (NumberFormatException e) {

            return Long.MAX_VALUE;
        }
    }

    private static int formatInt(final String str) {
        try {

            return Integer.parseInt(str);
        } catch (NumberFormatException e) {

            return Integer.MAX_VALUE;
        }
    }


    private static Object uuid(final String arg, final RequestParamsAndBody paramsAndBody) {

        Matcher matcher = ReplaceOperatorPattern.UUID_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            String replaceValue = UUID.randomUUID().toString();

            final String uuidLengthStr = group.replace("-UUID(", "").replace(")-", "");
            int uuidLength = 32;
            try {
                uuidLength = Integer.parseInt(uuidLengthStr);
            } catch (NumberFormatException e) {
                // no codes
            }

            if (uuidLength <= 32) {
                replaceValue = replaceValue.substring(0, uuidLength);
            } else {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < (uuidLength / 32) + 1; i++) {
                    builder.append(replaceValue);
                }
                replaceValue = builder.substring(0, uuidLength);
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.UUID_PATTERN.matcher(input);
        }

        return input;
    }


    private static Object timestamp(final String arg, final RequestParamsAndBody paramsAndBody) {
        Matcher matcher = ReplaceOperatorPattern.TIMESTAMP_NOW_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            final String timeUnit = group.replace("-TSNow(", "").replace(")-", "");

            long replaceValue = System.currentTimeMillis();
            if ("s".equals(timeUnit)) {
                replaceValue = System.currentTimeMillis() / 1000L;
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, Long.toString(replaceValue));
            } else {
                return Long.toString(replaceValue);
            }

            matcher = ReplaceOperatorPattern.TIMESTAMP_NOW_PATTERN.matcher(input);
        }

        return input;
    }


    private static Object timestampFormat(final String arg, final RequestParamsAndBody paramsAndBody) {
        Matcher matcher = ReplaceOperatorPattern.TIMESTAMP_FORMAT_NOW_PATTERN.matcher(arg);

        String input = arg;

        while (matcher.find()) {
            final String group = matcher.group();

            final String timePattern = group.replace("-TSFNow(", "").replace(")-", "");

            String replaceValue;

            if (StringUtils.isNotBlank(timePattern)) {
                try {
                    replaceValue = new SimpleDateFormat(timePattern).format(new Date());
                } catch (Exception e) {
                    // 当format失败时使用错误的 timePattern 替换
                    replaceValue = timePattern;
                }
            }else {
                replaceValue = String.valueOf(System.currentTimeMillis());
            }

            // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
            if (input.length() > group.length()) {
                input = input.replace(group, replaceValue);
            } else {
                return replaceValue;
            }

            matcher = ReplaceOperatorPattern.TIMESTAMP_FORMAT_NOW_PATTERN.matcher(input);
        }

        return input;
    }

    public static Map<String, String> replaceStringMap(final Map<String, String> paramMap, final RequestParamsAndBody requestParamsAndBody) {

        final Map<String, String> realHeader = new HashMap<>();

        if (MapUtils.isNotEmpty(paramMap)) {
            for (Map.Entry<String, String> headerKeyValue : paramMap.entrySet()) {
                final String realHeaderKey = ResponderReplaceOperator.replace(headerKeyValue.getKey(), requestParamsAndBody).toString();
                final String realHeaderValue = ResponderReplaceOperator.replace(headerKeyValue.getValue(), requestParamsAndBody).toString();
                realHeader.put(realHeaderKey, realHeaderValue);
            }
        }

        return realHeader;
    }
}
