package com.silwings.responder.core.chain;


import com.silwings.responder.core.bean.RequestContext;

/**
 * @ClassName ResponderBody
 * @Description 请求体封装
 * @Author Silwings
 * @Date 2021/8/6 18:40
 * @Version V1.0
 **/
public class ResponderBody {

    private RequestContext requestContext;

    public ResponderBody(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}
