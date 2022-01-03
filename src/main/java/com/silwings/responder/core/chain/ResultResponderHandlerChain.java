package com.silwings.responder.core.chain;

/**
 * @ClassName ResultResponderHandlerChain
 * @Description ResultResponderHandlerChain
 * @Author Silwings
 * @Date 2021/8/7 14:41
 * @Version V1.0
 **/
public class ResultResponderHandlerChain implements ResponderHandlerChain<ResponderBody> {
    @Override
    public boolean supportsParameter(ResponderBody responderBody) {
        return true;
    }

    @Override
    public ResponderBody handle(ResponderBody responderBody) {
        if (null == responderBody) {
            return new ResponderBody(null);
        }
//        if (responderBody.hasException()) {
//            responderBody.setHandlerResult(responderBody.getException().getMessage());
//        } else {
//            final String logicResult = responderBody.getLogicResult();
//            if (StringUtils.isNotBlank(logicResult)) {
//                responderBody.setHandlerResult(responderBody.getResult(logicResult));
//            } else {
//                responderBody.setHandlerResult(responderBody.getDefaultResult());
//            }
//        }

        return responderBody;
    }
}
