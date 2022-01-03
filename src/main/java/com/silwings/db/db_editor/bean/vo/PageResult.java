package com.silwings.db.db_editor.bean.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * PageResult<T>
 *
 * @author Andrew.Dong
 * @Date 2019-11-07
 */
@Data
public class PageResult<T> {

    public static final String DEFAULT_FAILED_CODE = "9999";

    public static final String DEFAULT_SUCCESS_CODE = "0";

    private String code;
    private String message;

    private PageData<T> data;

    /**
     * Constructor function.
     */
    public PageResult() {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = "";
        this.data = new PageData<>(Collections.emptyList(), 0L);
    }

    /**
     * Constructor function. (For: successed result)
     * 
     * @param list -- Java object that can be converted to JSON format.
     */
    public PageResult(List<T> list, Long total) {

        this.code = DEFAULT_SUCCESS_CODE;
        this.message = "";
        this.data = new PageData<>(list, total);
    }

    /**
     * Constructor function. (For: failed result)
     * 
     * @param code -- Error code.
     * @param message -- Error description.
     */
    public PageResult(String code, String message) {

        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * Default succeed result without data.
     */
    public static <T> PageResult<T> ok() {

        return new PageResult<>(null, 0L);
    }

    /**
     * Default succeed result with data.
     */
    public static <T> PageResult<T> ok(List<T> list, Long total) {

        return new PageResult<>(list, total);
    }

    /**
     * Default failed result with error description.
     */
    public static <T> PageResult<T> failed(String message) {

        return new PageResult<>(DEFAULT_FAILED_CODE, message);
    }

    /**
     * Default failed result with error code and description.
     */
    public static <T> PageResult<T> failed(String code, String message) {

        return new PageResult<>(code, message);
    }

    public boolean isSuccess() {

        return DEFAULT_SUCCESS_CODE.equals(code);
    }
}
