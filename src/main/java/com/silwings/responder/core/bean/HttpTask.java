package com.silwings.responder.core.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName HttpTask
 * @Description HttpTask
 * @Author Silwings
 * @Date 2022/1/3 18:02
 * @Version V1.0
 **/
@Setter
@Getter
public class HttpTask implements ConditionAble {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 延迟时间(ms)
     */
    private Long delayTime;

    /**
     * 任务执行条件
     */
    private List<String> condition;

    /**
     * 任务内容
     */
    private HttpTaskContent content;

    @Override
    public List<Condition> findConditions() {

        if (CollectionUtils.isEmpty(this.condition)) {
            return Collections.emptyList();
        }

        return this.condition.stream().map(Condition::from).collect(Collectors.toList());
    }

    @Setter
    @Getter
    public class HttpTaskContent {

        /**
         * 请求方式
         */
        private RequestMethod requestMethod;

        /**
         * 请求地址
         */
        private String requestUrl;

        /**
         * 请求参数
         */
        private Map<String,String> params;

        /**
         * restFull参数
         */
        private Map<String,String> restParams;

        /**
         * 请求体
         */
        private JSONObject body;

    }

}
