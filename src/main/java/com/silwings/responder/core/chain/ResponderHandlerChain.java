package com.silwings.responder.core.chain;

/**
 * @ClassName ResponderHandlerChain
 * @Description ResponderHandlerChain
 * @Author Silwings
 * @Date 2021/8/7 14:28
 * @Version V1.0
 **/
public interface ResponderHandlerChain<T> {

    boolean supportsParameter(T t);

    T handle(T t);

}
