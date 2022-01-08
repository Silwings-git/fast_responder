package com.silwings.db.mysql.dao.mapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @ClassName ResponderBaseMapper
 * @Description 基础mapper
 * @Author Silwings
 * @Date 2022/1/8 15:05
 * @Version V1.0
 **/
public interface ResponderBaseMapper<T> extends BaseMapper<T>, MySqlMapper<T>, ConditionMapper<T>, IdsMapper<T> {
}
