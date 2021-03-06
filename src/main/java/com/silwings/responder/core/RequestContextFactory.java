package com.silwings.responder.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.silwings.responder.core.config.ResponderInfo;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @ClassName RequestContextFactory
 * @Description 请求上下文工厂
 * @Author Silwings
 * @Date 2022/1/3 19:12
 * @Version V1.0
 **/
public class RequestContextFactory {

    private static final String QUESTION_MARK = "\\?";

    private final AntPathMatcher antPathMatcher;
    private final FastJsonHttpMessageConverter jsonbHttpMessageConverter;

    public RequestContextFactory(final AntPathMatcher antPathMatcher) {
        this.antPathMatcher = antPathMatcher;
        this.jsonbHttpMessageConverter = new FastJsonHttpMessageConverter();
    }

    RequestContext createRequestContext(final String url, final ResponderInfo responderInfo, final HttpServletRequest request) throws IOException {

        final String[] urlArray = url.split(QUESTION_MARK);

        // url参数
        final Map<String, String[]> params = request.getParameterMap();

        // path参数
        Map<String, String> pathParams = Collections.emptyMap();
        if (this.antPathMatcher.match(responderInfo.getKeyUrl(), urlArray[0])) {
            // 必须使用经过?分割后的url数据,否者可能将?后的参数错误解析到path参数中
            pathParams = this.antPathMatcher.extractUriTemplateVariables(responderInfo.getKeyUrl(), urlArray[0]);
        }

        // 请求体
        final JSONObject requestBody = (JSONObject) this.jsonbHttpMessageConverter.read(JSONObject.class, new ServletServerHttpRequest(request));

        final RequestParamsAndBody paramsAndBody = new RequestParamsAndBody(params, pathParams, requestBody);

        // 公共参数
        JSONObject customizeParam = responderInfo.getCustomizeParam();
        if (null != customizeParam) {
            final Object replace = ResponderReplaceOperator.replace(customizeParam.toString(), paramsAndBody);
            // replace要么是一个可以转换为json格式的java对象
            if (replace instanceof String ) {
                if (JSON.isValidObject((String) replace)) {
                    customizeParam = JSON.parseObject((String) replace);
                }
            } else {
                customizeParam = JSON.parseObject(JSON.toJSONString(replace));
            }
        }

        paramsAndBody.setCustomizeParam(customizeParam);

        return new RequestContext(paramsAndBody, responderInfo);
    }

}
