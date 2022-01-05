package com.silwings.responder.core.bean;

import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.utils.ConvertUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpTaskInfo
 * @Description HttpTaskInfo
 * @Author Silwings
 * @Date 2022/1/3 18:02
 * @Version V1.0
 **/
@Setter
@Getter
public class HttpTaskInfo implements ConditionAble {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 延迟时间(s)
     */
    private Long delayTime;

    /**
     * 任务执行条件
     */
    private List<String> conditions;

    /**
     * 任务内容
     */
    private HttpTaskContent content;

    @Override
    public Condition findCondition() {

        if (CollectionUtils.isEmpty(this.conditions)) {
            return Condition.TRUE_CONDITION;
        }

        return Condition.from(this.conditions);
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
        private Map<String,String[]> params;

        /**
         * 请求体
         */
        private JSONObject body;

        public Map<String, String[]> getParams() {
            return ConvertUtils.toObj(this.params, Collections.emptyMap());
        }

        public JSONObject getBody() {
            return ConvertUtils.toObj(this.body, new JSONObject());
        }

    }

}
