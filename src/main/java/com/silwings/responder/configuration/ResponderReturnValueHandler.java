package com.silwings.responder.configuration;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.annotation.ResponderMapping;
import com.silwings.responder.core.chain.ResponderBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class ResponderReturnValueHandler implements HandlerMethodReturnValueHandler {

    // 是否支持该返回值
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 仅支持标记了@ReqMapping注解的事件方法，且返回值类型需要是VsMvcBody或其子类
        return returnType.hasMethodAnnotation(ResponderMapping.class) && ResponderBody.class.isAssignableFrom(returnType.getParameterType());
    }

    // 处理返回值的方法
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 标记请求已被处理
        mavContainer.setRequestHandled(true);
        final ResponderBody body = (ResponderBody) returnValue;
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");

        try (ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
             OutputStream outputStream = servletServerHttpResponse.getBody()) {
//            final Object handleResult = body.getHandleResult();
//            outputStream.write(handleResult instanceof String ? handleResult.toString().getBytes() : JSON.toJSONString(handleResult).getBytes());
        }
    }
}
