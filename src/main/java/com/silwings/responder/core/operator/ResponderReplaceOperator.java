package com.silwings.responder.core.operator;

import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.bean.RequestParamsAndBody;
import com.silwings.responder.utils.ResponderStringUtils;
import org.apache.commons.lang3.StringUtils;

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


    SEARCH_REPLACE("${}", arg -> SEARCH_REPLACE_PATTERN.matcher(arg).find(), (arg, paramsAndBody) -> {

        String input = arg;
        Matcher matcher = SEARCH_REPLACE_PATTERN.matcher(input);

        while (matcher.find()) {
            final String group = matcher.group();
            final Object param = paramsAndBody.getParam(group.substring(2, group.length() - 1));

            input = input.replace(group, null == param ? "" : param.toString());

            matcher = SEARCH_REPLACE_PATTERN.matcher(input);
        }

        return input;
    }),
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

    public static Object replace(final String arg, final RequestParamsAndBody requestParamsAndBody) {

        if (StringUtils.isBlank(arg)) {
            return "";
        }

        Object result = arg;

        for (ResponderReplaceOperator value : values()) {
            if (value.applyFunction.test(ResponderStringUtils.toString(result))) {
                result = value.handleFunction.apply(ResponderStringUtils.toString(result), requestParamsAndBody);
            }
        }

        return result;
    }

//    public static ResponderReplaceOperator valueOfOperator(final String strOrOperator) {
//        for (ResponderReplaceOperator value : values()) {
//            if (value.applyFunction.test(strOrOperator)) {
//                return value;
//            }
//        }
//        return ResponderReplaceOperator.DO_NOTHING;
//    }

    public static void main(String[] args) {

        System.out.println(JSONObject.isValidObject("{}"));

    }

}
