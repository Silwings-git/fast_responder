package com.silwings.responder.configuration;

import com.silwings.responder.annotation.ResponderMapping;
import com.silwings.responder.core.bean.Result;
import com.silwings.responder.core.chain.ResponderContext;
import org.apache.commons.lang3.StringUtils;
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
        // 仅支持标记了 @ResponderMapping 注解的事件方法，且返回值类型需要是VsMvcBody或其子类
        return returnType.hasMethodAnnotation(ResponderMapping.class) && ResponderContext.class.isAssignableFrom(returnType.getParameterType());
    }

    // 处理返回值的方法
    @Override
    public void handleReturnValue(final Object returnValue, final MethodParameter returnType, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest) throws Exception {
        // 标记请求已被处理
        mavContainer.setRequestHandled(true);
        final ResponderContext body = (ResponderContext) returnValue;
        final Result result = body.getResult();
        if (null == result || (null == result.getBody() && StringUtils.isBlank(result.getMsg()))) {
            return;
        }

        final HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        try (ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
             OutputStream outputStream = servletServerHttpResponse.getBody()) {

            if (null != result.getBody()) {
                response.addHeader("Content-Type", "application/json;charset=UTF-8");
                outputStream.write(result.getBody().toJSONString().getBytes());
            }else {
                outputStream.write(result.getMsg().getBytes());
            }
        }
    }
}
