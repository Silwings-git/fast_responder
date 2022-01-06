package com.silwings.responder.mvc;

import com.silwings.responder.core.ResponderBodyHandlerManager;
import com.silwings.responder.core.ResponderContext;
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

    private ResponderBodyHandlerManager responderBodyHandlerManager;

    public ResponderHandlerController(ResponderBodyHandlerManager responderBodyHandlerManager) {
        this.responderBodyHandlerManager = responderBodyHandlerManager;
    }

    @ResponderMapping
    public ResponderContext handle(final ResponderContext responderContext) {
        return this.responderBodyHandlerManager.handle(responderContext);
    }

}
