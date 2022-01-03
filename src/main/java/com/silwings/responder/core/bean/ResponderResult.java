package com.silwings.responder.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ResponderResult
 * @Description 自定义响应结果
 * @Author Silwings
 * @Date 2021/8/7 11:24
 * @Version V1.0
 **/
public class ResponderResult<K,V> extends HashMap<K,V> {

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

    public V getDefaultResult() {
        return this.get("default");
    }
}
