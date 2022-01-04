package com.silwings.responder.core.interfaces.abstracts;

import com.silwings.responder.commons.exception.ExceptionThreadLocal;
import com.silwings.responder.commons.exception.ResponderException;
import com.silwings.responder.core.interfaces.LogError;

/**
 * @ClassName AbstractLogError
 * @Description AbstractLogError
 * @Author Silwings
 * @Date 2022/1/4 23:20
 * @Version V1.0
 **/
public abstract class AbstractLogError implements LogError {

    @Override
    public void err(String errMsg) {
        ExceptionThreadLocal.set(new ResponderException(errMsg));
    }

}
