package com.silwings.responder.mvc;

import com.silwings.responder.core.ResponderContext;
import com.silwings.responder.core.ResponderFlowManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName VsMvcHandler
 * @Description VsMvcHandler
 * @Author Silwings
 * @Date 2021/8/7 12:42
 * @Version V1.0
 **/
@Slf4j
@ResponderHandler
public class ResponderHandlerController {

    private ResponderFlowManager responderFlowManager;

    public ResponderHandlerController(final ResponderFlowManager responderFlowManager) {
        this.responderFlowManager = responderFlowManager;
    }

    @ResponderMapping
    public ResponderContext handle(final ResponderContext responderContext) throws Exception {
        return this.responderFlowManager.handle(responderContext);
    }

}
