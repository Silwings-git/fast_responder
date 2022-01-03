package com.silwings.responder.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName MyResponderHandler
 * @Description 标记这是一个自定义处理器
 * @Author Silwings
 * @Date 2021/8/6 18:47
 * @Version V1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface ResponderHandler {
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
