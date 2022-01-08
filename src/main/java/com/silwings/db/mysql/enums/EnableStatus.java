package com.silwings.db.mysql.enums;

/**
 * @ClassName EnableStatus
 * @Description 是否启用枚举
 * @Author Silwings
 * @Date 2022/1/8 15:17
 * @Version V1.0
 **/
public enum EnableStatus {
    DISABLED("1"),
    ENABLE("2"),
    ;

    private String code;

    EnableStatus(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

}
