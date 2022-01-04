package com.silwings.responder.core.operator;

import com.silwings.responder.core.bean.RequestParamsAndBody;

/**
 * @ClassName ReplaceOperator
 * @Description 替换操作符接口
 * @Author Silwings
 * @Date 2022/1/4 23:34
 * @Version V1.0
 **/
public interface ReplaceOperator {

    Object replace(String str, RequestParamsAndBody requestParamsAndBody);

}
