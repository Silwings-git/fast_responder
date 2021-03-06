package com.silwings.responder.core;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.core.result.Result;
import com.silwings.responder.task.HttpTaskFactory;
import com.silwings.responder.task.HttpTaskInfo;
import com.silwings.responder.task.HttpTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ResponderFlowManager
 * @Description 应答器流程管理器
 * @Author Silwings
 * @Date 2021/8/7 14:54
 * @Version V1.0
 **/
@Slf4j
public class ResponderFlowManager {

    private HttpTaskFactory httpTaskFactory;
    private HttpTaskManager httpTaskManager;

    public ResponderFlowManager(final HttpTaskFactory httpTaskFactory, final HttpTaskManager httpTaskManager) {
        this.httpTaskFactory = httpTaskFactory;
        this.httpTaskManager = httpTaskManager;
    }

    /**
     * description: 根据请求配置信息处理请求.
     * version: 1.0
     * date: 2022/1/6 22:40
     * author: Silwings
     *
     * @param responderContext 请求上下文
     * @return com.silwings.responder.core.ResponderContext 请求上下文
     */
    public ResponderContext handle(final ResponderContext responderContext) throws InterruptedException {

        this.performTask(responderContext.getTasks(), responderContext.getRequestParamsAndBody());

        Result result = this.filterResult(responderContext.getResults(), responderContext.getRequestParamsAndBody());

        result = this.replaceOperatorResult(result, responderContext.getRequestParamsAndBody());

        responderContext.setResult(result);

        // 最快响应延迟时间，sleep允许参数小于0，小于0时不执行
        TimeUnit.MILLISECONDS.sleep(responderContext.getDelayReturnTime() - responderContext.getExecutionTime());

        return responderContext;
    }

    /**
     * description: 判断任务条件并创建符合运行条件的Http任务
     * version: 1.0
     * date: 2022/1/6 22:36
     * author: Silwings
     *
     * @param tasks                任务集合
     * @param requestParamsAndBody 请求参数集
     */
    private void performTask(final List<HttpTaskInfo> tasks, final RequestParamsAndBody requestParamsAndBody) {

        if (CollectionUtils.isEmpty(tasks)) {
            log.info("当前应答器不包含Http Task.");
            return;
        }

        tasks.stream()
                .filter(Objects::nonNull)
                // 筛选符合运行条件的任务配置
                .filter(e -> e.findCondition().meet(requestParamsAndBody))
                // 创建任务
                .map(taskInfo -> this.httpTaskFactory.create(taskInfo, requestParamsAndBody))
                // 加入执行队列等待执行
                .forEach(this.httpTaskManager::add);

    }


    /**
     * description: 过滤返回值集,筛选出合适的返回值.如果按照条件全部匹配失败将返回null
     * version: 1.0
     * date: 2022/1/6 22:37
     * author: Silwings
     *
     * @param results              可选返回值集
     * @param requestParamsAndBody 请求参数
     * @return com.silwings.responder.core.result.CheckResult 符合筛选条件的result,如果找不到返回null
     */
    private Result filterResult(final List<Result> results, final RequestParamsAndBody requestParamsAndBody) {

        if (CollectionUtils.isEmpty(results)) {
            log.info("当前应答器不包含Result信息");
            return null;
        }

        for (Result result : results) {
            final Condition condition = result.findCondition();

            if (condition.meet(requestParamsAndBody)) {
                log.info("匹配到合适的Result: {}", result.getResultName());
                return result;
            }
        }

        log.info("未匹配到合适的 Result .");
        return null;
    }

    /**
     * description: 给返回值应用操作符
     * version: 1.0
     * date: 2022/1/6 22:38
     * author: Silwings
     *
     * @param result               返回值
     * @param requestParamsAndBody 请求参数集
     * @return com.silwings.responder.core.result.CheckResult 新的返回值实例
     */
    private Result replaceOperatorResult(final Result result, final RequestParamsAndBody requestParamsAndBody) {

        if (null == result) {
            return null;
        }

        final Result realResult = new Result();

        if (null != result.getBody()) {
            final Object replaceBody = ResponderReplaceOperator.replace(result.getBody().toJSONString(), requestParamsAndBody);

            // replaceBody要么是String,要么是一个可以转换为json格式的java对象
            if (replaceBody instanceof String) {
                if (JSON.isValidObject((String) replaceBody)) {
                    realResult.setBody(JSON.parseObject((String) replaceBody));
                } else {
                    // body中必须是对象,如果无法转换为对象,将结果放到msg中
                    realResult.setMsg((String) replaceBody);
                }
            } else {
                realResult.setBody(JSON.parseObject(JSON.toJSONString(replaceBody)));
            }
        }

        if (StringUtils.isBlank(realResult.getMsg()) && StringUtils.isNotBlank(result.getMsg())) {

            // msg内容必然是一个string
            realResult.setMsg((String) ResponderReplaceOperator.replace(result.getMsg(), requestParamsAndBody));
        }

        final Map<String, String> realHeader = ResponderReplaceOperator.replaceStringMap(result.getHeaders(), requestParamsAndBody);
        realResult.setHeaders(realHeader);

        log.info("应答器返回值初始化完成,Result信息: {}", JSON.toJSONString(realResult));

        return realResult;
    }

}