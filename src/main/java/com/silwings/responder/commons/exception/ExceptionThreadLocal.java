package com.silwings.responder.commons.exception;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ExceptionThreadLocal
 * @Description ExceptionThreadLocal
 * @Author Silwings
 * @Date 2022/1/4 23:09
 * @Version V1.0
 **/
public class ExceptionThreadLocal {

    private static final ThreadLocal<List<ResponderException>> EXCEPTION_TL = ThreadLocal.withInitial(CopyOnWriteArrayList::new);

    public static void set(final ResponderException ex) {
        if (null != ex) {
            EXCEPTION_TL.get().add(ex);
        }
    }

    public static List<ResponderException> get() {
        return EXCEPTION_TL.get();
    }

    public static void remove() {
        ExceptionThreadLocal.get().clear();
    }

}
