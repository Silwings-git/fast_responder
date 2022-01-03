package com.silwings.responder.core.factory;

import com.silwings.responder.core.bean.RequestConfigInfo;
import com.silwings.responder.core.bean.RequestContext;

import java.util.Map;

/**
 * @ClassName RequestContextFactory
 * @Description 请求上下文工厂
 * @Author Silwings
 * @Date 2022/1/3 19:12
 * @Version V1.0
 **/
public class RequestContextFactory {

    public static RequestContext of(final String url, final RequestConfigInfo requestConfigInfo, Map<String, ? super Object> requestBody) {

        return new RequestContext();

    }

}
