package com.silwings.responder.core.result;

import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.codition.ContainCondition;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckResult
 * @Description 响应
 * @Author Silwings
 * @Date 2022/1/3 18:55
 * @Version V1.0
 **/
@Setter
@Getter
public class Result implements ContainCondition {

    /**
     * 返回值名称
     */
    private String resultName;

    /**
     * 响应体
     */
    private JSONObject body;

    /**
     * 返回信息(仅当body不存在时尝试取该字段)
     */
    private String msg;

    /**
     * 条件
     */
    private List<String> conditions;

    /**
     * 响应头
     */
    private Map<String,String> headers;

    @Override
    public Condition findCondition() {
        if (CollectionUtils.isEmpty(this.conditions)){
            return Condition.TRUE_CONDITION;
        }

        return Condition.from(conditions);
    }

}
