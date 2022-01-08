package com.silwings.responder.mvc.exception;

/**
 * @ClassName ResponderException
 * @Description ResponderException
 * @Author Silwings
 * @Date 2021/8/8 13:18
 * @Version V1.0
 **/
public class ResponderException extends RuntimeException {
    public ResponderException() {
    }

    public ResponderException(String message) {
        super(message);
    }

    public ResponderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponderException(Throwable cause) {
        super(cause);
    }

    public ResponderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
