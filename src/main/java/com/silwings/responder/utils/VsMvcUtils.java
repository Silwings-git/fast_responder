package com.silwings.responder.utils;


import com.silwings.responder.core.chain.ResponderBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName VsMvcUtils
 * @Description VsMvcUtils
 * @Author 崔益翔
 * @Date 2021/1/1 17:21
 * @Version V1.0
 **/
public class VsMvcUtils {

    private static final String VS_MVC_BODY = "ResponderBody";

    public static void setMyMvcBody(final HttpServletRequest request, final ResponderBody responderBody) {
        if (null != request) {
            request.setAttribute(VS_MVC_BODY, responderBody);
        }
    }

    public static ResponderBody getMyMvcInfo(final HttpServletRequest request) {
        return (ResponderBody) request.getAttribute(VS_MVC_BODY);
    }
}
