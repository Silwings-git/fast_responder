package com.silwings.responder.core.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName RequestContext
 * @Description 请求上下文
 * @Author Silwings
 * @Date 2022/1/3 19:08
 * @Version V1.0
 **/
@Setter
@Getter
public class RequestContext {

    /**
     * url参数
     */
    private Map<String, String[]> params;

    /**
     * rest参数
     */
    private Map<String, String> restParams;

    /**
     * 请求体
     */
    private JSONObject body;

    public RequestContext(final Map<String, String[]> params, final Map<String, String> restParams, final JSONObject body) {
        this.params = params;
        this.restParams = restParams;
        this.body = body;
    }

    public Object getParamByName(final String paramName) {

        if (this.params.containsKey(paramName)) {
            return this.params.get(paramName);
        }

        if (this.restParams.containsKey(paramName)) {
            return this.restParams.get(paramName);
        }

        return null;
    }
}
