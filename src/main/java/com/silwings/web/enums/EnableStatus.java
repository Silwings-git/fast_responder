package com.silwings.web.enums;

/**
 * @ClassName EnableStatus
 * @Description 是否启用枚举
 * @Author Silwings
 * @Date 2022/1/8 15:17
 * @Version V1.0
 **/
public enum EnableStatus {
    DISABLED(0, "禁用"),
    ENABLE(1, "启用"),
    ;

    private Integer code;
    private String desc;

    EnableStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EnableStatus valueOfCode(final Integer enableStatus) {
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

    public String desc() {
        return this.desc;
    }
}
