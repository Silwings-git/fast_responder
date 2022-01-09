package com.silwings.responder.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @ClassName ResponderExceptionConvertAspect
 * @Description 统一异常转换
 * @Author Silwings
 * @Date 2022/1/8 21:27
 * @Version V1.0
 **/
@Slf4j
@Aspect
public class ResponderExceptionConvertAspect {

    @Pointcut("execution(* com.silwings.responder.mvc.ResponderHandlerController..*.*(..))")
    public void pointCut() {
        // no codes
    }

    @Around("pointCut()")
    public Object convert(final ProceedingJoinPoint jp) {
        try {
            return jp.proceed();
        } catch (Throwable throwable) {
            throw new ResponderException(throwable);
        }
    }

}
