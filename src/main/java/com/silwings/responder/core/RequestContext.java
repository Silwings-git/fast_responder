package com.silwings.responder.core;

import com.silwings.responder.core.config.ResponderInfo;
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
class RequestContext {

    /**
     * 请求的全部参数信息
     */
    private RequestParamsAndBody requestParamsAndBody;

    /**
     * 请求配置信息
     */
    private ResponderInfo responderInfo;

    RequestContext(final RequestParamsAndBody requestParamsAndBody, final ResponderInfo responderInfo) {
        this.requestParamsAndBody = ConvertUtils.toObj(requestParamsAndBody, RequestParamsAndBody.EMPTY_OBJ);
        this.responderInfo = responderInfo;
    }

}
