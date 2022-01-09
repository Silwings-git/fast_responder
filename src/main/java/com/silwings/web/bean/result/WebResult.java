package com.silwings.web.bean.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @ClassName WebResult
 * @Description 和前端交互用的返回值
 * @Author Silwings
 * @Date 2022/1/8 15:28
 * @Version V1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class WebResult<T> {

    public static final String SUCCESS_CODE = "200200";
    public static final String FAIL_CODE = "200500";

    private String code;

    private T data;

    private String msg;

    private WebResult(final String code, final T data, final String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> WebResult<T> ok() {
        return WebResult.ok(null);
    }

    public static <T> WebResult<T> ok(final T data) {
        return new WebResult<>(SUCCESS_CODE, data, "");
    }

    public static <T> WebResult<T> fail(final String msg) {
        return new WebResult<>(FAIL_CODE, null, msg);
    }

}
