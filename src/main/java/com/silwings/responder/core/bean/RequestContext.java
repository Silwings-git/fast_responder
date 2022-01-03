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
    private Map<String,String> param;

    /**
     * rest参数
     */
    private Map<String, String> restParam;

    /**
     * 请求体
     */
    private JSONObject body;

}
