package com.silwings.responder.commons.bean;


import com.silwings.responder.utils.BeanCopyUtils;

import java.lang.reflect.Type;

/**
 * @ClassName BaseBean
 * @Description BaseBean
 * @Author Silwings
 * @Date 2021/8/7 11:45
 * @Version V1.0
 **/
public class BaseBean {

    public static <T> T copy(T t) {
        if (null == t) {
            return null;
        }
        return BeanCopyUtils.jsonCopyBean(t, (Type) t.getClass());
    }

}
