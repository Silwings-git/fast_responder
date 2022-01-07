package com.silwings.responder.core;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.core.result.Result;
import com.silwings.responder.core.result.ResultCondition;
import com.silwings.responder.task.HttpTaskFactory;
import com.silwings.responder.task.HttpTaskInfo;
import com.silwings.responder.task.HttpTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
     * @param responderContext
     * @return com.silwings.responder.core.ResponderContext
     */
    public ResponderContext handle(final ResponderContext responderContext) {

        this.performTask(responderContext.getTasks(), responderContext.getRequestParamsAndBody());

        Result result = this.filterResult(responderContext.getResultConditions(), responderContext.getRequestParamsAndBody(), responderContext.getResults());

        result = this.replaceOperatorResult(result, responderContext.getRequestParamsAndBody());

        responderContext.setResult(result);

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
            return;
        }

        tasks.stream()
                // 筛选符合运行条件的任务配置
                .filter(e -> e.findCondition().meet(requestParamsAndBody))
                // 创建任务
                .map(taskInfo -> this.httpTaskFactory.create(taskInfo, requestParamsAndBody))
                // 加入执行队列等待执行
                .forEach(this.httpTaskManager::add);

    }


    /**
     * description: 过滤返回值集,筛选出合适的返回值.如果按照条件全部匹配失败将查询default返回值
     * version: 1.0
     * date: 2022/1/6 22:37
     * author: Silwings
     *
     * @param resultConditions     返回值条件集
     * @param requestParamsAndBody 请求参数
     * @param results              可选返回值
     * @return com.silwings.responder.core.result.Result 符合筛选条件或default的返回值,如果未设置default,可能返回null
     */
    private Result filterResult(final List<ResultCondition> resultConditions, final RequestParamsAndBody requestParamsAndBody, final List<Result> results) {

        if (CollectionUtils.isEmpty(resultConditions)) {
            return this.getDefaultResult(results);
        }

        for (ResultCondition resultCondition : resultConditions) {

            final Condition condition = resultCondition.findCondition();

            if (condition.meet(requestParamsAndBody)) {

                final Result resultByName = this.getResultByName(resultCondition.getResultName(), results);

                if (null != resultByName) {
                    return resultByName;
                }

            }

        }

        return this.getDefaultResult(results);
    }

    /**
     * description: 获取默认返回值.(名称为default)
     * version: 1.0
     * date: 2022/1/6 21:52
     * author: Silwings
     *
     * @param results 可选返回值集
     * @return com.silwings.responder.core.result.Result 默认返回值.如果没有返回null
     */
    public Result getDefaultResult(final List<Result> results) {
        return this.getResultByName("default", results);
    }

    /**
     * description: 根据返回对象名获取返回对象实例
     * version: 1.0
     * date: 2022/1/6 21:55
     * author: Silwings
     *
     * @param resultName 返回对象名称
     * @param results    可选返回值集
     * @return com.silwings.responder.core.result.Result 和resultName匹配的返回值对象.如果找不到返回null
     */
    public Result getResultByName(final String resultName, final List<Result> results) {
        if (StringUtils.isBlank(resultName) || CollectionUtils.isEmpty(results)) {
            return null;
        }

        for (Result result : results) {
            if (resultName.equals(result.getResultName())) {
                return result;
            }
        }

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
     * @return com.silwings.responder.core.result.Result 新的返回值实例
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

        return realResult;
    }

}