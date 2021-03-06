package com.silwings.responder.core;


import com.silwings.responder.core.result.Result;
import com.silwings.responder.task.HttpTaskInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName ResponderContext
 * @Description 一次请求流程中的上下文
 * @Author Silwings
 * @Date 2021/8/6 18:40
 * @Version V1.0
 **/
@Slf4j
@Getter
public class ResponderContext {

    private final RequestContext requestContext;

    private Result result;

    /**
     * 开始执行时间,对象被构建时算起
     */
    private final long startExecutionTime = System.currentTimeMillis();

    ResponderContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    List<HttpTaskInfo> getTasks() {
        return this.requestContext.getResponderInfo().getTasks();
    }

    public Object getParam(final String parameterName) {
        return this.requestContext.getRequestParamsAndBody().getParam(parameterName);
    }

    RequestParamsAndBody getRequestParamsAndBody() {
        return this.requestContext.getRequestParamsAndBody();
    }

    long getDelayReturnTime() {
        return this.requestContext.getResponderInfo().getDelayTime();
    }

    List<Result> getResults() {
        return this.requestContext.getResponderInfo().getResults();
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public long getExecutionTime() {
        return this.startExecutionTime - System.currentTimeMillis();
    }

    public String getResponderName() {
        return this.requestContext.getResponderInfo().getName();
    }
}
