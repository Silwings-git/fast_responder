package com.silwings.responder.core.bean;

import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.interfaces.abstracts.AbstractLogError;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @ClassName RequestParamsAndBody
 * @Description 请求参数和请求体
 * @Author Silwings
 * @Date 2022/1/4 22:58
 * @Version V1.0
 **/
@Slf4j
@Setter
@Getter
public class RequestParamsAndBody extends AbstractLogError {

    public static final RequestParamsAndBody EMPTY_OBJ = new RequestParamsAndBody(Collections.emptyMap(), Collections.emptyMap(), new JSONObject());

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

    public RequestParamsAndBody(final Map<String, String[]> params, final Map<String, String> restParams, final JSONObject body) {
        this.params = params;
        this.restParams = restParams;
        this.body = body;
    }

    /**
     * description: 从全部请求参数中获取参数信息
     * 该接口会依次尝试从url参数,rest参数,请求体中获取paramName对应的值.
     * version: 1.0
     * date: 2022/1/4 21:51
     * author: Silwings
     *
     * @param paramName 参数名
     * @return java.lang.Object 参数值
     */
    public Object getParam(final String paramName) {

        if (StringUtils.isBlank(paramName)) {
            return null;
        }

        if (this.getParams().containsKey(paramName)) {
            return this.getParams().get(paramName);
        }

        if (this.getRestParams().containsKey(paramName)) {
            return this.getRestParams().get(paramName);
        }

        // 尝试从请求体中获取参数
        if (!paramName.contains(".")) {
            return this.getBody().get(paramName);
        }

        Object result = null;
        final String[] paramArray = paramName.split(".");
        if (paramArray.length == 1) {
            result = this.getBody().get(paramArray[0]);
        }

        JSONObject jsonObject = null;
        try {
            for (int i = 0; i <= paramArray.length - 1; i++) {
                // 当获取到最后一个元素时,使用Object接收,防止出现格式问题
                if (i == paramArray.length - 1) {
                    result = jsonObject.get(paramArray[i]);
                } else if (null != jsonObject) {
                    // 数组格式不应该出现在中间属性上,所以这里直接赋值给类型.
                    jsonObject = jsonObject.getJSONObject(paramArray[i]);
                } else {
                    jsonObject = this.getBody().getJSONObject(paramArray[i]);
                }
            }
        } catch (Exception e) {
            log.error("尝试从请求体获取信息失败", e);
            this.err("尝试从请求体获取信息失败: " + e.getMessage());
        }

        return result;
    }


}
