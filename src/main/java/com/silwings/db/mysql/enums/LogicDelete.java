package com.silwings.db.mysql.enums;

/**
 * @ClassName LogicDelete
 * @Description 是否删除枚举
 * @Author Silwings
 * @Date 2022/1/8 15:19
 * @Version V1.0
 **/
public enum LogicDelete {

    NORMAL(0),
    DELETED(1),
    ;

    private Integer code;

    LogicDelete(Integer code) {
        this.code = code;
    }

    public Integer number() {
        return this.code;
    }
}
