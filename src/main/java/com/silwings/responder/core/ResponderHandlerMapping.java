package com.silwings.responder.core;

import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.interfaces.RequestConfigRepository;
import com.silwings.responder.mvc.ResponderHandler;
import com.silwings.responder.mvc.ResponderMapping;
import com.silwings.responder.mvc.ResponderMappingInfo;
import com.silwings.responder.utils.ResponderContextUtils;
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
    protected HandlerMethod getHandlerInternal(final HttpServletRequest request) throws Exception {

        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        final String requestUrl = this.getUrlPathHelper().getLookupPathForRequest(request);

        final RequestConfigInfo requestConfig = this.getConfig(requestMethod, requestUrl);

        // 必须先确保数据库存在该url对应的配置才能读取流,否则会导致后续的处理器无法正常使用
        if (null != requestConfig) {

            final RequestContext requestContext = this.requestContextFactory.createRequestContext(requestUrl, requestConfig, request);

            final ResponderContext responderContext = new ResponderContext(requestContext);

            ResponderContextUtils.setBody(request, responderContext);

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
     * @return com.silwings.responder.core.config.RequestConfigInfo 自定义配置信息
     */
    private RequestConfigInfo getConfig(final RequestMethod requestMethod, final String url) {

        RequestConfigInfo adaptedConfig = null;

        // 同一个url可能对应不同请求方式
        final List<RequestConfigInfo> configByUrl = this.requestConfigRepository.queryByKeyUrl(url);
        for (RequestConfigInfo configInfo : configByUrl) {
            if (requestMethod.equals(configInfo.getRequestMethod())) {
                adaptedConfig = configInfo;
            }
        }

        if (null == adaptedConfig) {

            final List<RequestConfigInfo> restFullConfigList = this.requestConfigRepository.queryRestConfigByMethod(requestMethod);

            for (RequestConfigInfo configInfo : restFullConfigList) {
                if (configInfo.matchUrl(url, this.antPathMatcher)) {
                    adaptedConfig = configInfo;
                    break;
                }
            }
        }

        return adaptedConfig;
    }

    /**
     * description:
     * 项目启动时会调用该方法判断是否是处理器类型,重写该方法以判断是否添加了
     * {@link ResponderHandler}注解,如果添加了才视为处理器bean,查找全部方法
     * version: 1.0
     * date: 2021/1/1 17:02
     * author: Silwings
     *
     * @param beanType 当前检测到的类
     * @return boolean true表示是处理器bean,框架会遍历该bean中的全部方法
     */
    @Override
    protected boolean isHandler(final Class<?> beanType) {
        boolean isHandler = AnnotatedElementUtils.hasAnnotation(beanType, ResponderHandler.class);
        if (isHandler) {
            log.info("Responder processor initialization success: {}", beanType.getName());
        }
        return isHandler;
    }

    /**
     * description: 根据方法创建映射信息
     * version: 1.0
     * date: 2021/1/1 17:09
     * author: Silwings
     *
     * @param method      带有{@link org.springframework.stereotype.Controller}注解的类的方法
     * @param handlerType
     * @return ResponderMappingInfo method的映射信息
     */
    @Override
    protected ResponderMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {
        final ResponderMapping mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, ResponderMapping.class);
        if (null == mergedAnnotation) {
            return null;
        }

        // 这里只需要返回一个空对象即可,用于给spring mvc创建映射信息.在实际请求到来时会通过getMatchingCondition创建对应请求的ResponderMappingInfo
        return new ResponderMappingInfo();
    }

    /**
     * description: 通过映射信息根据请求获取映射匹配结果
     * version: 1.0
     * date: 2021/1/1 17:15
     * author: Silwings
     *
     * @param mapping 项目初始化时构建的带{@link org.springframework.stereotype.Controller}注解的类的处理器方法的映射信息
     * @param request 请求
     * @return ResponderMappingInfo 根据request创建出的映射信息,用于实际处理业务
     */
    @Override
    protected ResponderMappingInfo getMatchingMapping(final ResponderMappingInfo mapping, final HttpServletRequest request) {
        return mapping.getMatchingCondition(ResponderContextUtils.getBody(request));
    }

    @Override
    protected Comparator<ResponderMappingInfo> getMappingComparator(final HttpServletRequest request) {
        return ((info1, info2) ->
                info1.compareTo(info1, ResponderContextUtils.getBody(request)));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected Set<String> getMappingPathPatterns(ResponderMappingInfo mapping) {
        return new HashSet<>();
    }

}
