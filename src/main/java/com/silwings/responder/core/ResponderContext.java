package com.silwings.responder.core;


import com.silwings.responder.core.result.Result;
import com.silwings.responder.core.result.ResultCondition;
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

    private RequestContext requestContext;

    private Result result;

    public ResponderContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<HttpTaskInfo> getTasks() {
        return this.requestContext.getRequestConfigInfo().getTasks();
    }

    public Object getParam(final String parameterName) {
        return this.requestContext.getRequestParamsAndBody().getParam(parameterName);
    }

    public RequestParamsAndBody getRequestParamsAndBody() {
        return this.requestContext.getRequestParamsAndBody();
    }

    public List<ResultCondition> getResultConditions() {
        return this.requestContext.getRequestConfigInfo().getResultConditions();
    }

    public List<Result> getResults() {
        return this.requestContext.getRequestConfigInfo().getResults();
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
