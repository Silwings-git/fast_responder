package com.silwings.responder.utils;

import com.silwings.db.controller.bean.param.PageParam;
import org.apache.ibatis.session.RowBounds;

/**
 * @ClassName RowBoundsUtils
 * @Description 分页参数工具
 * @Author Silwings
 * @Date 2022/1/8 16:52
 * @Version V1.0
 **/
public class RowBoundsUtils {

    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int MAX_SIZE = 1000;

    public static RowBounds build(final PageParam pageParam) {

        if (null == pageParam) {
            return new RowBounds(0, 5);
        }

        int pageSize = pageParam.getPageSize();
        int pageNum = pageParam.getPageNum();

        if (pageSize <= 0 || pageSize > MAX_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }

        return new RowBounds((pageNum - 1) * pageSize, pageSize);
    }
}
