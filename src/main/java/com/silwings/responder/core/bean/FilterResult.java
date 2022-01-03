package com.silwings.responder.core.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FilterResult
 * @Description TODO_Silwings
 * @Author Silwings
 * @Date 2022/1/3 18:43
 * @Version V1.0
 **/
@Setter
@Getter
public class FilterResult implements ConditionAble{

    /**
     * 返回值名称
     */
    private String resultName;

    /**
     * 条件
     */
    private List<String> conditions;

    @Override
    public List<Condition> findConditions() {
        if (CollectionUtils.isEmpty(this.conditions)){
            return Collections.emptyList();
        }

        return this.conditions.stream().map(Condition::from).collect(Collectors.toList());
    }

}
