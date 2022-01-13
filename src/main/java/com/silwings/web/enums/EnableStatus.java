package com.silwings.web.enums;

/**
 * @ClassName EnableStatus
 * @Description 是否启用枚举
 * @Author Silwings
 * @Date 2022/1/8 15:17
 * @Version V1.0
 **/
public enum EnableStatus {
    DISABLED(false, "禁用"),
    ENABLE(true, "启用"),
    ;

    private Boolean value;
    private String desc;

    EnableStatus(Boolean value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EnableStatus valueOfCode(final Boolean code) {
        if (null == code) {
            return null;
        }
        return code ? ENABLE : DISABLED;
    }

    public String desc() {
        return this.desc;
    }

    public Boolean value() {
        return this.value;
    }
}
