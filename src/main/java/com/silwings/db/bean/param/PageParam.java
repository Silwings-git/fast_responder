package com.silwings.db.bean.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName PageParam
 * @Description 分页查询基本参数
 * @Author Silwings
 * @Date 2022/1/8 16:37
 * @Version V1.0
 **/
@Setter
@Getter
public class PageParam {

    // Default page number.
    public static final int DEFAULT_PAGE_NUM = 1;
    // Default page size.
    public static final int DEFAULT_PAGE_SIZE = 5;

    private int pageNum;

    private int pageSize;

    public PageParam() {
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

}
