package com.silwings.responder.core.chain;


import com.silwings.responder.core.bean.HttpTask;
import com.silwings.responder.core.bean.RequestContext;
import com.silwings.responder.core.interfaces.abstracts.AbstractLogError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName ResponderBody
 * @Description 请求体封装
 * @Author Silwings
 * @Date 2021/8/6 18:40
 * @Version V1.0
 **/
@Slf4j
@Getter
public class ResponderBody extends AbstractLogError {

    private RequestContext requestContext;

    public ResponderBody(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<HttpTask> getTasks() {
        return this.requestContext.getRequestConfigInfo().getTasks();
    }

    public Object getParam(final String parameterName) {
        return this.requestContext.getRequestParamsAndBody().getParam(parameterName);
    }

}
