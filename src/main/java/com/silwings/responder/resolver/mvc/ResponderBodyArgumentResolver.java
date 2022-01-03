package com.silwings.responder.resolver.mvc;

import com.silwings.responder.annotation.ResponderMapping;
import com.silwings.responder.core.chain.ResponderBody;
import com.silwings.responder.utils.ResponderBodyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * @ClassName ResponderBodyArgumentResolver
 * @Description 参数解析器
 * @Author Silwings
 * @Date 2021/8/6 23:39
 * @Version V1.0
 **/
@Slf4j
public class ResponderBodyArgumentResolver implements HandlerMethodArgumentResolver {

    public ResponderBodyArgumentResolver() {
        log.info("ResponderBodyArgumentResolver initialization.");
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return null != parameter && AnnotatedElementUtils.hasAnnotation(parameter.getMethod(), ResponderMapping.class);
    }


    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        // 获取HttpServletRequest对象,用于获取之前通过setAttribute()设置到请求信息中的请求体实例
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // 获取对应方法的入参类型,用来判断需要的参数是否是HttpServletRequest中存储的请求体实例
        final Class<?> parameterType = parameter.getParameterType();

        final ResponderBody responderBody = ResponderBodyUtils.getBody(nativeRequest);

        if (ResponderBody.class.equals(parameterType)) {
            return responderBody;
        }

        // 如果方法的入参并不是MyMvcBody,可以尝试使用方法上定义的参数名从MyMvcBody实例中获取值
        return this.getParameter(parameter.getParameterName(), responderBody);
    }

    /**
     * description: 根据参数名获取请求体中的对应值
     *
     * @param parameterName 参数名
     * @param responderBody 请求体转换出来的实例
     * @return java.lang.Object 参数名在请求体实例中的值
     */
    private Object getParameter(final String parameterName, final ResponderBody responderBody) {
        if (null == responderBody) {
            return null;
        }

        final PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(responderBody.getClass(), parameterName);
        if (null != propertyDescriptor) {
            try {
                return propertyDescriptor.getReadMethod().invoke(responderBody);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("ResponderBodyArgumentResolver.getParameter 执行异常.", e);
            }
        }

        return responderBody.getParam(parameterName);
    }

}
