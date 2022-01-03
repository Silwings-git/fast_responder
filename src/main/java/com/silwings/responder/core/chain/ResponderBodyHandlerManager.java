package com.silwings.responder.core.chain;

/**
 * @ClassName ResponderBodyHandlerManager
 * @Description ResponderBodyHandlerManager
 * @Author Silwings
 * @Date 2021/8/7 14:54
 * @Version V1.0
 **/
public class ResponderBodyHandlerManager<T extends ResponderHandlerChain<ResponderBody>> {

    private Chain<T> head;

    private Chain<T> last = null;

    public ResponderBodyHandlerManager<T> addHandler(T t) {
        final Chain<T> tChain = new Chain<>(t);

        if (null == head) {
            this.head = tChain;
        } else {
            this.last.setNext(tChain);
        }
        this.last = tChain;

        return this;
    }

    public ResponderBody handle(ResponderBody e) {
        Chain<T> handlerChain = this.head;
        ResponderBody result = e;

        while (null != handlerChain) {
            if (handlerChain.current.supportsParameter(result)) {
                result = handlerChain.current.handle(result);
            }
            handlerChain = handlerChain.next;
        }

        return result;
    }

    public class Chain<T> {

        private T current;

        private Chain<T> next;

        protected Chain(T current) {
            this.current = current;
        }

        protected void setNext(Chain<T> next) {
            this.next = next;
        }
    }

}
