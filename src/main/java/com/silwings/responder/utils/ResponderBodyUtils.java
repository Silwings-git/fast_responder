package com.silwings.responder.utils;


import com.silwings.responder.core.chain.ResponderContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ResponderBodyUtils
 * @Description ResponderBodyUtils
 * @Author Silwings
 * @Date 2021/1/1 17:21
 * @Version V1.0
 **/
public class ResponderBodyUtils {

    private static final String RESPONDER_BODY = "ResponderContext";

    public static void setBody(final HttpServletRequest request, final ResponderContext responderContext) {
        if (null != request) {
            request.setAttribute(RESPONDER_BODY, responderContext);
        }
    }

    public static ResponderContext getBody(final HttpServletRequest request) {
        return (ResponderContext) request.getAttribute(RESPONDER_BODY);
    }
}
