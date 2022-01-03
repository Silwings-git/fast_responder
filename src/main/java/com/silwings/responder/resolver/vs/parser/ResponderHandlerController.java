package com.silwings.responder.resolver.vs.parser;

import com.silwings.responder.annotation.ResponderHandler;
import com.silwings.responder.annotation.ResponderMapping;
import com.silwings.responder.core.chain.ResponderBody;
import com.silwings.responder.core.chain.ResponderBodyHandlerManager;
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
    public ResponderBody handle(final ResponderBody responderBody) {
        // TODO_Silwings: 2022/1/3 handler的处理逻辑
        // 1.task和result的handler不需要借助容器,直接使用硬编码调用即可
        return this.responderBodyHandlerManager.handle(responderBody);
    }

    public String test() {

        return true ? true ? true ? true ? true ? true ? true ? "s" : "s" : "s" : "s" : "s" : "s" : "s" : "s";

    }

}
