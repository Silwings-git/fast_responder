package com.silwings.responder.core.result;

import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.codition.ConditionAble;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @ClassName ResultCondition
 * @Description 过滤返回值
 * @Author Silwings
 * @Date 2022/1/3 18:43
 * @Version V1.0
 **/
@Setter
@Getter
public class ResultCondition implements ConditionAble {

    /**
     * 返回值名称
     */
    private String resultName;

    /**
     * 条件
     */
    private List<String> conditions;

    @Override
    public Condition findCondition() {
        if (CollectionUtils.isEmpty(this.conditions)){
            return Condition.TRUE_CONDITION;
        }

        return Condition.from(conditions);
    }

}
