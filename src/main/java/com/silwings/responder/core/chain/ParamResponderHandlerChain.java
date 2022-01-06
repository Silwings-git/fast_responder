package com.silwings.responder.core.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ParamResponderHandlerChain
 * @Description 参数处理器
 * @Author Silwings
 * @Date 2021/8/7 14:30
 * @Version V1.0
 **/
@Slf4j
public class ParamResponderHandlerChain implements ResponderHandlerChain<ResponderContext> {

    @Override
    public boolean supportsParameter(ResponderContext responderContext) {
        return true;
    }

    @Override
    public ResponderContext handle(ResponderContext responderContext) {

        log.info("====================================分=====割=====线====================================");

        return responderContext;
    }
}
