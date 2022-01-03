package com.silwings.db.db_editor.bean.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 接口返回对象
 *
 * @author zejun.dong
 * @Date 2018年2月2日下午8:44:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PageParam {

    // Default page number.
    public static final int DEFAULT_PAGE_NUM = 1;
    // Default page size.
    public static final int DEFAULT_PAGE_SIZE = 5;

    private int pageSize;

    private int pageNum;

    public PageParam() {
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

}
