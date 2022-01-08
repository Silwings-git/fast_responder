package com.silwings.db.controller.bean.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

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
public class ResponderPageResult<T> {

    public static final String SUCCESS_CODE = "200200";
    public static final String FAIL_CODE = "200500";

    private String code;

    private PageData<T> data;

    private String msg;

    private ResponderPageResult(String code, PageData<T> data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> ResponderPageResult<T> ok() {
        return ResponderPageResult.ok(Collections.emptyList(), 0L);
    }

    public static <T> ResponderPageResult<T> ok(final List<T> data, final long total) {
        return new ResponderPageResult<>(SUCCESS_CODE, new PageData<>(data, total), "");
    }

    public static <T> ResponderPageResult<T> fail(final String msg) {
        return new ResponderPageResult<>(FAIL_CODE, null, msg);
    }

}
