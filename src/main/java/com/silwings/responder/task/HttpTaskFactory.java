package com.silwings.responder.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName HttpTaskFactory
 * @Description HttpTaskFactory
 * @Author Silwings
 * @Date 2022/1/6 0:35
 * @Version V1.0
 **/
@Slf4j
public class HttpTaskFactory {

    /**
     * description: 创建task任务
     * version: 1.0
     * date: 2022/1/6 0:57
     * author: Silwings
     *
     * @param taskInfo             任务配置
     * @param requestParamsAndBody 当前请求参数信息
     * @return com.silwings.responder.task.HttpTask 任务
     */
    public HttpTask create(final HttpTaskInfo taskInfo, final RequestParamsAndBody requestParamsAndBody) {

        final HttpTaskInfo newInfo = BeanCopyUtils.jsonCopyBean(taskInfo, HttpTaskInfo.class);

        final HttpTaskInfo.HttpTaskContent taskContent = newInfo.getContent();

        // 实际请求地址
        final String requestUrl = taskContent.getRequestUrl();
        final String realRequestUrl = ResponderReplaceOperator.replace(requestUrl, requestParamsAndBody).toString();

        // 实际请求参数
        final Map<String, String[]> params = taskContent.getParams();
        final HashMap<String, String[]> realParams = new HashMap<>();

        for (Map.Entry<String, String[]> paramEntry : params.entrySet()) {

            final String realParamName = ResponderReplaceOperator.replace(paramEntry.getKey(), requestParamsAndBody).toString();

            final String[] paramValueArray = paramEntry.getValue();
            final String[] realParamValueArray = new String[paramValueArray.length];
            Arrays.stream(paramValueArray)
                    .map(value -> ResponderReplaceOperator.replace(value, requestParamsAndBody))
                    .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                    .collect(Collectors.toList())
                    .toArray(realParamValueArray);

            realParams.put(realParamName, realParamValueArray);
        }

        // 实际请求体
        final String bodyStr = taskContent.getBody().toJSONString();
        final Object realBodyObj = ResponderReplaceOperator.replace(bodyStr, requestParamsAndBody);
        JSONObject realBody = new JSONObject();
        if (realBodyObj instanceof String) {

            if (JSON.isValidObject((String) realBodyObj)) {
                realBody = JSON.parseObject((String) realBodyObj);
            }else {
                log.error("HttpTask {} 的实际 Body 无法转换为JSON格式,已丢弃. 实际Body: {}", taskInfo.getName(), realBodyObj);
            }
        } else {

            realBody = JSON.parseObject(JSON.toJSONString(realBodyObj));
        }

        final HttpTask task = new HttpTask();
        task
                .setTaskName(taskInfo.getName())
                .setRequestUrl(realRequestUrl)
                .setRequestMethod(taskContent.getRequestMethod())
                .setHeaders(taskContent.getHeaders())
                .setParams(realParams)
                .setBody(realBody)
                .setDelayTime(taskInfo.getDelayTime())
                .setRunTime(System.currentTimeMillis() + taskInfo.getDelayTime());

        return task;
    }

}
