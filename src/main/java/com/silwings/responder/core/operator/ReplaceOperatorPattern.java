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

    public static final Pattern SEARCH_REPLACE_PATTERN = Pattern.compile("\\$\\{[\\S]+?}");

}
