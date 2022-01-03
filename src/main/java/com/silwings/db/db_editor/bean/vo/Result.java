package com.silwings.db.db_editor.bean.vo;

import lombok.Data;

/**
 * Result<T>
 *
 * @author Andrew.Dong
 * @Date 2019-11-07
 */
@Data
public class Result<T> {

    public static final String DEFAULT_FAILED_CODE = "9999";

    public static final String DEFAULT_SUCCESS_CODE = "0";

    private String code;

    private String message;

    private T data;

    /**
     * Constructor function.
     */
    public Result() {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = "";
        this.data = null;
    }

    /**
     * Constructor function. (For: successed result)
     * 
     * @param object -- Java object that can be converted to JSON format.
     */
    public Result(T object) {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = "";
        this.data = object;
    }

    /**
     * Constructor function. (For: failed result)
     * 
     * @param code -- Error code.
     * @param message -- Error description.
     */
    public Result(String code, String message) {

        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * Default succeed result without data.
     */
    public static <T> Result<T> ok() {

        return new Result<>(null);
    }

    /**
     * Default succeed result with data.
     */
    public static <T> Result<T> ok(T data) {

        return new Result<>(data);
    }

    /**
     * Default failed result with error description.
     */
    public static <T> Result<T> failed(String message) {

        return new Result<>(DEFAULT_FAILED_CODE, message);
    }

    /**
     * Default failed result with error code and description.
     */
    public static <T> Result<T> failed(String code, String message) {

        return new Result<>(code, message);
    }

    public boolean isSuccess() {

        return DEFAULT_SUCCESS_CODE.equals(code);
    }
}
