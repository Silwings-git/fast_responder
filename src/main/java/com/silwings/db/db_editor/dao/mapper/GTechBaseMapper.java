package com.silwings.db.db_editor.dao.mapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.RowBoundsMapper;

/**
 * GTechBaseMapper
 *
 * @author Andrew.Dong
 * @Date 2019-09-02
 */
public interface GTechBaseMapper<T> extends BaseMapper<T>, MySqlMapper<T>, ConditionMapper<T>, RowBoundsMapper<T>, IdsMapper<T> {
}
