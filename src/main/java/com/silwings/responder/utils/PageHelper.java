package com.silwings.responder.utils;

import com.github.pagehelper.PageRowBounds;
import com.silwings.db.db_editor.bean.param.PageParam;
import org.apache.ibatis.session.RowBounds;


/**
 *
 * @author Andrew.Dong
 * @Date 2020-12-20
 */
public class PageHelper {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_SIZE_MAX = 1000;

    private PageHelper() {
        // No codes
    }


    public static RowBounds buildRowBounds(PageParam pageParam) {

        return buildRowBounds(pageParam, false, DEFAULT_PAGE_SIZE_MAX);
    }

    public static RowBounds buildRowBounds(PageParam pageParam, int maxPageSize) {

        return buildRowBounds(pageParam, false, maxPageSize);
    }

    public static RowBounds buildRowBounds(PageParam pageParam, boolean count) {

        return buildRowBounds(pageParam, count, DEFAULT_PAGE_SIZE_MAX);
    }

    public static RowBounds buildRowBounds(PageParam pageParam, boolean count, int maxPageSize) {

        int pageSize = pageParam.getPageSize();
        int pageNum = pageParam.getPageNum();

        if (pageSize <= 0 || pageSize > maxPageSize) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }

        PageRowBounds rowBounds = new PageRowBounds((pageNum - 1) * pageSize, pageSize);
        rowBounds.setCount(count);

        return rowBounds;
    }
}
