package com.silwings.responder.core.codition;

import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.utils.ConvertUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @ClassName Condition
 * @Description 条件
 * @Author Silwings
 * @Date 2022/1/3 18:12
 * @Version V1.0
 **/
@Slf4j
public class Condition {

    public static final Condition TRUE_CONDITION = new Condition(Expression.TRUE);

    private List<Expression> expressions;

    private Condition(final Expression expression) {
        Objects.requireNonNull(expression);
        this.expressions = Collections.singletonList(expression);
    }

    private Condition(final List<String> conditionStrList) {

        if (CollectionUtils.isEmpty(conditionStrList)) {
            this.expressions = Collections.singletonList(Expression.TRUE);
        } else {
            this.expressions = conditionStrList.stream().map(Expression::of).collect(Collectors.toList());
        }
    }

    public static Condition from(final List<String> conditionStrList) {
        return new Condition(conditionStrList);
    }

    public boolean meet(final RequestParamsAndBody requestParamsAndBody) {

        for (Expression expression : this.expressions) {

            expression.updateRealParam(ResponderReplaceOperator.replace(expression.getParam(), requestParamsAndBody));
            expression.updateRealValue(ResponderReplaceOperator.replace(expression.getValue(), requestParamsAndBody));

            if (!expression.calculate()) {
                log.info("条件表达式: {} 不满足条件. 表达式状态: {} .实际比对信息: {}", expression.originalExpression, expression.valid ? "正常" : "异常", expression.realExpression());
                return false;
            }
        }

        return true;
    }

    @Getter(AccessLevel.PRIVATE)
    public static class Expression {

        private static final Expression INVALID_EXPRESSION = new Expression();
        private static final Expression TRUE = new Expression("1", "1", ConditionSymbol.EQUAL);

        private final boolean valid;

        /**
         * 原始表达式
         */
        private String originalExpression;

        /**
         * 形式参数
         */
        private final String param;

        /**
         * 形式参数值
         */
        private final String value;

        /**
         * 真实参与计算的参数
         */
        private Object realParam;

        /**
         * 真实参与计算的参数值
         */
        private Object realValue;

        private final ConditionSymbol conditionSymbol;

        private Expression() {
            this.valid = false;
            this.param = null;
            this.value = null;
            this.conditionSymbol = null;
        }

        private Expression(final String param, final String value, final ConditionSymbol conditionSymbol) {
            this.valid = true;
            this.param = param;
            this.value = value;
            // 默认真实值为形式值
            this.realParam = param;
            this.realValue = value;

            this.conditionSymbol = conditionSymbol;
        }

        static Expression of(final String conditionStr) {

            final String trimCondition = conditionStr.trim();

            final String[] conditionArray = trimCondition.split(" ");

            if (conditionArray.length < 2) {
                return Expression.INVALID_EXPRESSION;
            }

            final ConditionSymbol conditionSymbol = ConditionSymbol.valueOfSymbol(conditionArray[1]);
            if (null == conditionSymbol) {
                return Expression.INVALID_EXPRESSION;
            }

            final Expression expression = new Expression(conditionArray[0], conditionArray.length > 2 ? conditionArray[2] : "", conditionSymbol);
            expression.originalExpression = conditionStr;

            return expression;
        }

        void updateRealParam(final Object realParam) {
            this.realParam = realParam;
        }

        void updateRealValue(final Object realValue) {
            this.realValue = realValue;
        }

        boolean calculate() {
            if (null == this.realParam || null == this.conditionSymbol || null == this.realValue) {
                return false;
            }

            return this.valid && this.conditionSymbol.apply(ConvertUtils.toStringOrJsonString(this.realParam),
                    ConvertUtils.toStringOrJsonString(this.realValue));
        }

        String realExpression() {
            if (null == this.realParam || null == this.conditionSymbol || null == this.realValue) {
                return "";
            }
            return ConvertUtils.toStringOrJsonString(this.realParam) + " " + this.conditionSymbol.symbol + " " + ConvertUtils.toStringOrJsonString(this.realValue);
        }

        public boolean isValid() {
            return this.valid;
        }
    }

    private enum ConditionSymbol {

        EQUAL("==", String::equals),
        NO_EQUAL("!=", (k, v) -> !k.equals(v)),
        GREATER_THAN(">", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) > 0),
        GREATER_OR_EQUAL(">=", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) >= 0),
        LESS_THEN("<", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) < 0),
        LESS_OR_EQUAL("<=", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) <= 0),
        IS_NULL("=IsNull=", (k, v) -> null == k),
        IS_NOT_NULL("=IsNotNull=", (k, v) -> null != k),
        IS_BLANK("=IsBlank=", (k, v) -> null == k || StringUtils.isBlank(k)),
        IS_NOT_BLANK("=IsNotBlank=", (k, v) -> StringUtils.isNotBlank(k)),
        ;

        private String symbol;
        private BiPredicate<String, String> function;

        ConditionSymbol(final String symbol, final BiPredicate<String, String> function) {
            this.symbol = symbol;
            this.function = function;
        }

        public boolean apply(final String param, final String value) {
            return this.function.test(param, value);
        }

        public static ConditionSymbol valueOfSymbol(final String sl) {

            for (ConditionSymbol value : values()) {
                if (value.equalsSymbol(sl)) {
                    return value;
                }
            }

            return null;
        }

        public boolean equalsSymbol(final String sl) {
            return this.symbol.equals(sl);
        }

        private static boolean allIsNumeric(final String k, final String v) {
            return StringUtils.isNumeric(k) && StringUtils.isNumeric(v);
        }

    }

    public List<Expression> getExpressions() {
        return CollectionUtils.isEmpty(this.expressions) ? Collections.emptyList() : this.expressions;
    }
}
