package com.silwings.responder.core.operator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;

/**
 * @ClassName ResponderReplaceOperator
 * @Description ResponderReplaceOperator
 * @Author Silwings
 * @Date 2022/1/4 23:33
 * @Version V1.0
 **/
public enum ResponderReplaceOperator implements ReplaceOperatorPattern {


    SEARCH_REPLACE("${}", arg -> SEARCH_REPLACE_PATTERN.matcher(arg).find(), ResponderReplaceOperator::searchReplaceHandle),
    DO_NOTHING("", s -> false, (str, paramsAndBody) -> str),
    ;

    private String operator;
    private Predicate<String> applyFunction;
    private BiFunction<String, RequestParamsAndBody, Object> handleFunction;

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

        if (StringUtils.isBlank(arg)) {
            return arg;
        }

        String input = arg;

        final HashMap<Object, Object> hashMap = new HashMap<>();

        // 如果input可以转换为一个json,按照对象的类型进行处理
        if (JSON.isValidObject(input)) {
            final Map<String, Object> map = JSON.parseObject(input, new TypeReference<Map<String, Object>>() {
            });
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                final Object realKey = ResponderReplaceOperator.searchReplaceHandle(key, paramsAndBody);

                final Object value = entry.getValue();
                final Object realValue = ResponderReplaceOperator.searchReplaceHandle(value.toString(), paramsAndBody);

                hashMap.put(realKey, realValue);
            }

            return hashMap;

        } else {
            Matcher matcher = SEARCH_REPLACE_PATTERN.matcher(input);

            while (matcher.find()) {
                final String group = matcher.group();
                final Object param = paramsAndBody.getParam(group.substring(2, group.length() - 1));

                // 如果原始字符串长度比获取到的group长,需要部分替换.否则就是完整替换,完整替换不用执行替换,直接使用新值返回
                if (input.length() > group.length()) {
                    input = input.replace(group, getReplacement(param));
                } else {
                    return param;
                }

                matcher = SEARCH_REPLACE_PATTERN.matcher(input);
            }

            return input;
        }

    }

    private static String getReplacement(Object param) {
        if (null == param) {
            return "";
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

}
