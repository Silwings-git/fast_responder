package com.silwings.responder.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.RequestParamsAndBody;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.utils.BeanCopyUtils;

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
        for (String paramName : params.keySet()) {
            final String realParamName = ResponderReplaceOperator.replace(paramName, requestParamsAndBody).toString();

            final String[] paramValueArray = params.get(paramName);
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
        final Object realBodyStr = ResponderReplaceOperator.replace(bodyStr, requestParamsAndBody);
        final JSONObject realBody = JSON.parseObject(JSON.toJSONString(realBodyStr));

        final HttpTask task = new HttpTask();
        task
                .setTaskName(taskInfo.getName())
                .setRequestUrl(realRequestUrl)
                .setRequestMethod(taskContent.getRequestMethod())
                .setParams(realParams)
                .setBody(realBody)
                .setDelayTime(taskInfo.getDelayTime())
                .setRunTime(System.currentTimeMillis() + taskInfo.getDelayTime());

        return task;
    }

}
