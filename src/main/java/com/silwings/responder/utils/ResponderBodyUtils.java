package com.silwings.responder.utils;


import com.silwings.responder.core.chain.ResponderBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ResponderBodyUtils
 * @Description ResponderBodyUtils
 * @Author Silwings
 * @Date 2021/1/1 17:21
 * @Version V1.0
 **/
public class ResponderBodyUtils {

    private static final String RESPONDER_BODY = "ResponderBody";

    public static void setBody(final HttpServletRequest request, final ResponderBody responderBody) {
        if (null != request) {
            request.setAttribute(RESPONDER_BODY, responderBody);
        }
    }

    public static ResponderBody getBody(final HttpServletRequest request) {
        return (ResponderBody) request.getAttribute(RESPONDER_BODY);
    }
}
