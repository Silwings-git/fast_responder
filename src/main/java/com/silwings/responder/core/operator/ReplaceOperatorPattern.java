package com.silwings.responder.core.operator;

import java.util.regex.Pattern;

/**
 * @ClassName ReplaceOperatorPattern
 * @Description 替换操作符接口
 * @Author Silwings
 * @Date 2022/1/4 23:34
 * @Version V1.0
 **/
public interface ReplaceOperatorPattern {

    Pattern SEARCH_REPLACE_PATTERN =Pattern.compile("\\$\\{[\\S]+?}");

}
