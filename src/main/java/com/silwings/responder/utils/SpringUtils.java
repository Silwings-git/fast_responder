package com.silwings.responder.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringUtils
 * @Description spring工具
 * @Author Silwings
 * @Date 2022/1/9 16:02
 * @Version V1.0
 **/
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 使用对象给静态成员赋值,用于使当前类获取到spring上下文引用,从而使用getBean()方法
        SpringUtils.context = applicationContext;
    }

    public static <T> T getBean(final Class<T> clazz) {

        if (null == clazz || null == SpringUtils.context) {
            return null;
        }

        return SpringUtils.context.getBean(clazz);
    }

}
