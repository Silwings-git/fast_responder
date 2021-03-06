package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName BeanCopyUtils
 * @Description BeanCopyUtils
 * @Author Silwings
 * @Date 2022/1/5 22:06
 * @Version V1.0
 **/
public class BeanCopyUtils {

    /**
     * description: java result 实例复制
     * version: 1.0
     * date: 2022/1/5 22:57
     * author: Silwings
     *
     * @param source 源数据
     * @param clazz  目标类型
     * @return T 一个新的type类型实例
     */
    public static <T> T jsonCopyBean(final Object source, final Class<T> clazz) {

        if (null == source) {
            return null;
        }

        if (source instanceof String) {
            return JSON.parseObject((String) source, clazz);
        }

        return JSON.parseObject(JSON.toJSONString(source), clazz);
    }

    public static <T> List<T> jsonCopyList(final List<?> source, final Class<T> clazz) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        return JSON.parseArray(JSON.toJSONString(source), clazz);
    }

}
