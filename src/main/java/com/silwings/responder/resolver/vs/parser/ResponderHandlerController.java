package com.silwings.responder.resolver.vs.parser;

import com.alibaba.fastjson.JSON;
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

        System.out.println("=====================================");

        System.out.println(JSON.toJSONString(responderBody));

        System.out.println("=====================================");

        return responderBody;
//        return this.responderBodyHandlerManager.handle(responderBody);
    }

}
