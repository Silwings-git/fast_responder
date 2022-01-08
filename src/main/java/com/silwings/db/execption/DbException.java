package com.silwings.db.execption;

/**
 * @ClassName ResponderException
 * @Description ResponderException
 * @Author Silwings
 * @Date 2021/8/8 13:18
 * @Version V1.0
 **/
public class DbException extends RuntimeException {
    public DbException() {
    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbException(Throwable cause) {
        super(cause);
    }

    public DbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
