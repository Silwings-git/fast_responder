package com.silwings.responder.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ResponderMapping
 * @Description 处理方法映射
 * @Author Silwings
 * @Date 2021/8/6 18:53
 * @Version V1.0
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponderMapping {
}
