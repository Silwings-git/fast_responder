package com.silwings.web.enums;

/**
 * @ClassName EnableStatus
 * @Description 是否启用枚举
 * @Author Silwings
 * @Date 2022/1/8 15:17
 * @Version V1.0
 **/
public enum EnableStatus {
    DISABLED(0),
    ENABLE(1),
    ;

    private Integer code;

    EnableStatus(Integer code) {
        this.code = code;
    }

    public static EnableStatus valueOfCode(Integer enableStatus) {
        for (EnableStatus value : values()) {
            if (value.equalsCode(enableStatus)) {
                return value;
            }
        }
        return null;
    }

    public Integer number() {
        return this.code;
    }

    public boolean equalsCode(final Integer code) {
        return this.number().equals(code);
    }

}
