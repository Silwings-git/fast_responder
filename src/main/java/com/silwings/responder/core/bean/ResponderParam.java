package com.silwings.responder.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ResponderParam
 * @Description 请求参数
 * @Author Silwings
 * @Date 2021/8/7 11:23
 * @Version V1.0
 **/
public class ResponderParam<K,V> extends HashMap<K,V> {

    @Override
    public V put(K key, V value) {
        if (null != this.get(key)) {
            throw new IllegalStateException(key + "不可修改");
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Object key : m.keySet()) {
            if (null != this.get(key)) {
                throw new IllegalStateException(key + "不可修改");
            }
        }
        super.putAll(m);
    }
}
