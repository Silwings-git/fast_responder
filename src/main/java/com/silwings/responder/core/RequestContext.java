package com.silwings.responder.core;

import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.utils.ConvertUtils;
import lombok.Getter;
import lombok.Setter;

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
     * 请求的全部参数信息
     */
    private RequestParamsAndBody requestParamsAndBody;

    /**
     * 请求配置信息
     */
    private RequestConfigInfo requestConfigInfo;

    public RequestContext(final RequestParamsAndBody requestParamsAndBody, final RequestConfigInfo requestConfigInfo) {
        this.requestParamsAndBody = ConvertUtils.toObj(requestParamsAndBody, RequestParamsAndBody.EMPTY_OBJ);
        this.requestConfigInfo = requestConfigInfo;
    }

}
