package com.silwings.responder.core.operator;

import java.util.regex.Pattern;

/**
 * @ClassName ReplaceOperatorPattern
 * @Description 替换操作符正则常量类
 * @Author Silwings
 * @Date 2022/1/4 23:34
 * @Version V1.0
 **/
public class ReplaceOperatorPattern {

    static final Pattern SEARCH_REPLACE_PATTERN = Pattern.compile("\\$\\{[\\S]+?}");
    static final Pattern RANDOM_INT_PATTERN = Pattern.compile("-RDInt-");
    static final Pattern RANDOM_BOOLEAN_PATTERN = Pattern.compile("-RDBoolean-");
    static final Pattern RANDOM_DOUBLE_PATTERN = Pattern.compile("-RDDouble-");
    static final Pattern RANDOM_LONG_PATTERN = Pattern.compile("-RDLong-");

}
