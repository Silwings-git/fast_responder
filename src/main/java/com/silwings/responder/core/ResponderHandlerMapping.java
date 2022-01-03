package com.silwings.responder.core;

import com.silwings.responder.annotation.ResponderHandler;
import com.silwings.responder.annotation.ResponderMapping;
import com.silwings.responder.core.bean.RequestConfigInfo;
import com.silwings.responder.core.bean.RequestContext;
import com.silwings.responder.core.chain.ResponderBody;
import com.silwings.responder.core.chain.ResponderMappingInfo;
import com.silwings.responder.core.factory.RequestContextFactory;
import com.silwings.responder.interfaces.RequestConfigRepository;
import com.silwings.responder.utils.ResponderBodyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName MyMvcHandlerMapping
 * @Description 自定义处理器映射
 * @Author Silwings
 * @Date 2021/1/1 17:01
 * @Version V1.0
 **/
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class ResponderHandlerMapping extends AbstractHandlerMethodMapping<ResponderMappingInfo> {

    @Autowired
    private RequestContextFactory requestContextFactory;

    @Autowired
    private RequestConfigRepository requestConfigRepository;

    @Autowired
    private AntPathMatcher antPathMatcher;


    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {

        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        final String requestUrl = this.getUrlPathHelper().getLookupPathForRequest(request);

        final RequestConfigInfo requestConfig = this.getConfig(requestMethod, requestUrl);

        // 必须先确保数据库存在该url对应的配置才能读取流,否则会导致后续的处理器无法正常使用
        if (null != requestConfig) {

            final RequestContext requestContext = this.requestContextFactory.createRequestContext(requestUrl, requestConfig, request);

            final ResponderBody responderBody = new ResponderBody(requestContext);

            ResponderBodyUtils.setBody(request, responderBody);

        }

        // 前置判断以及请求体解析执行完成，交给父类的获取处理器方法逻辑执行查找匹配逻辑
        // 父类遍历全部事件映射信息，找到与事件请求体属性匹配的事件处理器
        return super.getHandlerInternal(request);
    }

    /**
     * description: 通过请求方式和请求地址获取配置信息
     * version: 1.0
     * date: 2022/1/3 19:33
     * author: Silwings
     *
     * @param requestMethod 请求方式
     * @param url           请求地址
     * @return com.silwings.responder.core.bean.RequestConfigInfo 自定义配置信息
     */
    private RequestConfigInfo getConfig(final RequestMethod requestMethod, final String url) {

        RequestConfigInfo requestConfigInfo = this.requestConfigRepository.findByKeyUrl(url);

        if (null == requestConfigInfo) {

            List<RequestConfigInfo> restFullConfigList = this.requestConfigRepository.queryRestConfigByMethod(requestMethod);

            for (RequestConfigInfo configInfo : restFullConfigList) {
                if (configInfo.matchUrl(url, this.antPathMatcher)) {
                    requestConfigInfo = configInfo;
                    break;
                }
            }

        }

        return requestConfigInfo;
    }

    /**
     * description:
     * 项目启动时会调用该方法判断是否是处理器类型,重写该方法以判断是否添加了
     * {@link ResponderHandler}注解,如果添加了才视为处理器bean,查找全部方法
     * version: 1.0
     * date: 2021/1/1 17:02
     * author: Silwings
     *
     * @param beanType
     * @return boolean true表示是处理器bean,框架会遍历该bean中的全部方法
     */
    @Override
    protected boolean isHandler(Class<?> beanType) {
        boolean isHandler = AnnotatedElementUtils.hasAnnotation(beanType, ResponderHandler.class);
        if (isHandler) {
            log.info("virtual processor initialization success: {}", beanType.getName());
        }
        return isHandler;
    }

    /**
     * description: 根据方法创建映射信息
     * version: 1.0
     * date: 2021/1/1 17:09
     * author: Silwings
     *
     * @param method
     * @param handlerType
     * @return com.silwings.mymvc.core.MyMvcInfo
     */
    @Override
    protected ResponderMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        final ResponderMapping mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, ResponderMapping.class);
        if (null == mergedAnnotation) {
            return null;
        }
//        return new ResponderMappingInfo(mergedAnnotation.type());
        return null;
    }

    /**
     * description: 通过映射信息根据请求获取映射匹配结果
     * version: 1.0
     * date: 2021/1/1 17:15
     * author: Silwings
     *
     * @param mapping
     * @param request
     * @return com.silwings.mymvc.core.MyMvcInfo
     */
    @Override
    protected ResponderMappingInfo getMatchingMapping(ResponderMappingInfo mapping, HttpServletRequest request) {
        return mapping.getMatchingCondition(ResponderBodyUtils.getBody(request));
    }

    /**
     * description: getMappingComparator
     * version: 1.0
     * date: 2021/1/1 17:24
     * author: Silwings
     *
     * @param request
     * @return java.util.Comparator<com.silwings.mymvc.core.MyMvcInfo>
     */
    @Override
    protected Comparator<ResponderMappingInfo> getMappingComparator(HttpServletRequest request) {
        return ((info1, info2) ->
                info1.compareTo(info1, ResponderBodyUtils.getBody(request)));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected Set<String> getMappingPathPatterns(ResponderMappingInfo mapping) {
        return new HashSet<>();
    }


    /**
     * {@code @Order}定义带注释的组件的排序顺序。
     * <p> {@ link #value}是可选的，代表{@link Ordered}接口中定义的订单值。较低的值具有较高的优先级。
     * 默认值为{@code Ordered.LOWEST_PRECEDENCE}，表示最低优先级（输给任何其他指定的订单值）。
     * <p> <b>注意：</ b>从Spring 4.0开始，Spring中的许多类型的组件都支持基于注释的排序，
     * 即使是考虑到目标组件的顺序值的集合注入（从它们的目标类或通过其{@code @Bean}方法）。
     * 尽管此类顺序值可能会影响注入点的优先级，但请注意，它们不会影响单例启动顺序，
     * 这是由依赖关系和{@code @DependsOn}声明（影响运行时确定的依赖图）确定的正交关注点。
     * <p>从Spring 4.1开始，在订购场景中，标准{@link javax.annotation.Priority}批注可以用作该批注的替代品。
     * 请注意，当必须选择单个元素时，{@ code @Priority}可能具有其他语义（请参见{@link AnnotationAwareOrderComparator＃getPriority}）。
     * <p>或者，也可以通过{@link Ordered}接口在每个实例的基础上确定顺序值，从而允许配置确定的实例值，而不是附加到特定类的硬编码值。
     * <p>请咨询{@link org.springframework.core.OrderComparator OrderComparator}的Javadoc，以获取有关非排序对象的排序语义的详细信息。
     */
}
