package com.silwings.responder.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Map对象操作工具类
 *
 * @author Andrew.Dong
 * @Date 2019-09-02
 */
public class MapUtils {

    private MapUtils() {
        // Unused
    }

    public static void convertKey(Map<String, Object> mapObject, String oldKey, String newKey) {

        if (null == mapObject || StringUtils.isBlank(oldKey)) {
            return;
        }

        if (!mapObject.containsKey(oldKey)) {
            return;
        }
        
        if (StringUtils.isBlank(newKey)) {
            mapObject.remove(oldKey);
        } else {
            mapObject.put(newKey, mapObject.remove(oldKey));
        }
    }
}
