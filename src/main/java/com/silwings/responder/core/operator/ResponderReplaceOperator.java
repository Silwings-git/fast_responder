package com.silwings.responder.core.operator;

import com.silwings.responder.core.bean.RequestParamsAndBody;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @ClassName ResponderReplaceOperator
 * @Description ResponderReplaceOperator
 * @Author Silwings
 * @Date 2022/1/4 23:33
 * @Version V1.0
 **/
public enum ResponderReplaceOperator implements ReplaceOperator {

    SEARCH_REPLACE("${}", str -> StringUtils.isNotBlank(str) && str.startsWith("${") && str.endsWith("}"), (str, paramsAndBody) -> paramsAndBody.getParam(str.substring(2, str.length() - 1))),

    ;

    private String operator;
    private Predicate<String> applyFunction;
    private BiFunction<String, RequestParamsAndBody, Object> handleFunction;

    ResponderReplaceOperator(final String operator, final Predicate<String> applyFunction, final BiFunction<String, RequestParamsAndBody, Object> handleFunction) {
        this.operator = operator;
        this.applyFunction = applyFunction;
        this.handleFunction = handleFunction;
    }

    @Override
    public Object replace(final String str, final RequestParamsAndBody requestParamsAndBody) {
        return this.handleFunction.apply(str, requestParamsAndBody);
    }

    public static ResponderReplaceOperator valueOfOperator(final String strOrOperator) {
        for (ResponderReplaceOperator value : values()) {
            if (value.applyFunction.test(strOrOperator)) {
                return value;
            }
        }
        return null;
    }

}
