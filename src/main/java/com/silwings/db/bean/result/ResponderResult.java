package com.silwings.db.bean.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @ClassName ResponderResult
 * @Description 和前端交互用的返回值
 * @Author Silwings
 * @Date 2022/1/8 15:28
 * @Version V1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class ResponderResult<T> {

    public static final String SUCCESS_CODE = "200200";
    public static final String FAIL_CODE = "200500";

    private String code;

    private T data;

    private String msg;

    private ResponderResult(final String code, final T data, final String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> ResponderResult<T> ok() {
        return ResponderResult.ok(null);
    }

    public static <T> ResponderResult<T> ok(final T data) {
        return new ResponderResult<>(SUCCESS_CODE, data, "");
    }

    public static <T> ResponderResult<T> fail(final String msg) {
        return new ResponderResult<>(FAIL_CODE, null, msg);
    }

}
