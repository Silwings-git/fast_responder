package com.silwings.responder.core.codition;

import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import lombok.AccessLevel;
import lombok.Getter;
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
                return false;
            }
        }

        return true;
    }

    @Getter(AccessLevel.PRIVATE)
    private static class Expression {

        private static final Expression INVALID_EXPRESSION = new Expression();
        private static final Expression TRUE = new Expression("1", "1", ConditionSymbol.EQUAL);

        private final boolean valid;

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

        public Expression(final String param, final String value, final ConditionSymbol conditionSymbol) {
            this.valid = true;
            this.param = param;
            this.value = value;
            // 默认真实值为形式值
            this.realParam = param;
            this.realValue = value;

            this.conditionSymbol = conditionSymbol;
        }

        public static Expression of(final String conditionStr) {

            final String trimCondition = conditionStr.trim();

            final String[] conditionArray = trimCondition.split(" ");

            if (3 != conditionArray.length) {
                return Expression.INVALID_EXPRESSION;
            }

            final ConditionSymbol conditionSymbol = ConditionSymbol.valueOfSymbol(conditionArray[1]);
            if (null == conditionSymbol) {
                return Expression.INVALID_EXPRESSION;
            }

            return new Expression(conditionArray[0], conditionArray[2], conditionSymbol);
        }

        public void updateRealParam(final Object realParam) {
            this.realParam = realParam;
        }

        public void updateRealValue(final Object realValue) {
            this.realValue = realValue;
        }

        public boolean calculate() {

            return this.valid && this.conditionSymbol.apply(this.realParam.toString(), this.realValue.toString());
        }
    }

    private enum ConditionSymbol {

        EQUAL("==", String::equals),
        NO_EQUAL("!=", (k, v) -> !k.equals(v)),
        GREATER_THAN(">", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) > 0),
        GREATER_OR_EQUAL(">=", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) >= 0),
        LESS_THEN("<", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) < 0),
        LESS_OR_EQUAL("<=", (k, v) -> ConditionSymbol.allIsNumeric(k, v) && Double.valueOf(k).compareTo(Double.valueOf(v)) <= 0),
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

}