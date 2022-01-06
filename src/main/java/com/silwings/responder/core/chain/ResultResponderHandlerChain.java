package com.silwings.responder.core.chain;

/**
 * @ClassName ResultResponderHandlerChain
 * @Description ResultResponderHandlerChain
 * @Author Silwings
 * @Date 2021/8/7 14:41
 * @Version V1.0
 **/
public class ResultResponderHandlerChain implements ResponderHandlerChain<ResponderContext> {
    @Override
    public boolean supportsParameter(ResponderContext responderContext) {
        return true;
    }

    @Override
    public ResponderContext handle(ResponderContext responderContext) {
        if (null == responderContext) {
            return new ResponderContext(null);
        }
//        if (responderContext.hasException()) {
//            responderContext.setHandlerResult(responderContext.getException().getMessage());
//        } else {
//            final String logicResult = responderContext.getLogicResult();
//            if (ResponderStringUtils.isNotBlank(logicResult)) {
//                responderContext.setHandlerResult(responderContext.getResult(logicResult));
//            } else {
//                responderContext.setHandlerResult(responderContext.getDefaultResult());
//            }
//        }

        return responderContext;
    }
}
