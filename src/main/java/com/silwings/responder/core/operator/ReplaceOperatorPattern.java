package com.silwings.responder.core.operator;

import java.util.regex.Pattern;

/**
 * @ClassName ReplaceOperatorPattern
 * @Description 替换操作符正则常量类
 * @Author Silwings
 * @Date 2022/1/4 23:34
 * @Version V1.0
 **/
class ReplaceOperatorPattern {

    static final Pattern SEARCH_REPLACE_PATTERN = Pattern.compile("\\$\\{[\\S]+?}");
    static final Pattern RANDOM_BOOLEAN_PATTERN = Pattern.compile("-RDBoolean\\(\\)-");
    static final Pattern RANDOM_INT_PATTERN = Pattern.compile("(((-RDInt\\()([1-9][0-9]{0,9})(\\)-)|(-RDInt\\()(-?[1-9][0-9]{0,9})(,)([1-9][0-9]{0,9})(\\)-))|(-RDInt\\(\\)-))");
    static final Pattern RANDOM_LONG_PATTERN = Pattern.compile("(((-RDLong\\()([1-9][0-9]{0,18})(\\)-)|(-RDLong\\()(-?[1-9][0-9]{0,18})(,)([1-9][0-9]{0,18})(\\)-))|(-RDLong\\(\\)-))");
    static final Pattern RANDOM_DOUBLE_PATTERN = Pattern.compile("(((-RDDouble\\()[1-9][0-9]*(\\.?[0-9]+)?(\\)-)|(-RDDouble\\()(-?[1-9][0-9]*(\\.?[0-9]+)?)(,)([1-9][0-9]*(\\.?[0-9]+)?)(\\)-))|(-RDDouble\\(\\)-))");
    static final Pattern UUID_PATTERN = Pattern.compile("((-UUID\\()([1-9][0-9]{0,18})(\\)-)|(-UUID\\(\\)-))");

}
