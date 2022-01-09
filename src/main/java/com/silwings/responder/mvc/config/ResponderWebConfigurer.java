package com.silwings.responder.mvc.config;

import com.silwings.responder.core.RequestContextFactory;
import com.silwings.responder.core.ResponderFlowManager;
import com.silwings.responder.event.ResponderEventManager;
import com.silwings.responder.mvc.ResponderMappingArgumentResolver;
import com.silwings.responder.mvc.ResponderReturnValueHandler;
import com.silwings.responder.mvc.exception.ResponderExceptionConvertAspect;
import com.silwings.responder.task.HttpHandler;
import com.silwings.responder.task.HttpTaskFactory;
import com.silwings.responder.task.HttpTaskManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName MyWebConfigurer
 * @Description web配置
 * @Author Silwings
 * @Date 2021/1/3 12:22
 * @Version V1.0
 **/
@Configuration
@EnableConfigurationProperties({TaskSchedulerProperties.class, HttpTaskRestTemplateProperties.class})
public class ResponderWebConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ResponderMappingArgumentResolver());
    }

    @Override
    public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new ResponderReturnValueHandler());
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public RequestContextFactory requestContextFactory() {
        return new RequestContextFactory(antPathMatcher());
    }

    @Bean
    public HttpTaskFactory httpTaskFactory() {
        return new HttpTaskFactory();
    }

    @Bean
    public HttpTaskManager httpTaskManager() {
        return new HttpTaskManager();
    }

    @Bean
    public ResponderFlowManager responderBodyHandlerManager() {
        return new ResponderFlowManager(httpTaskFactory(), httpTaskManager());
    }

    @Bean
    public AsyncRestTemplate httpTaskRestTemplate(final HttpTaskRestTemplateProperties httpTaskRestTemplateProperties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(httpTaskRestTemplateProperties.getConnectTimeout());
        factory.setReadTimeout(httpTaskRestTemplateProperties.getReadTimeout());
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return new AsyncRestTemplate(factory);
    }

    @Bean
    public ResponderEventManager httpTaskEventManager() {
        return new ResponderEventManager();
    }

    @Bean
    public HttpHandler httpHandler(final AsyncRestTemplate httpTaskRestTemplate) {
        return new HttpHandler(httpTaskManager(), httpTaskRestTemplate, httpTaskEventManager());
    }

    @Bean("httpTaskScheduler")
    public TaskScheduler taskScheduler(final TaskSchedulerProperties taskSchedulerProperties) {
        final ThreadPoolTaskScheduler httpTaskScheduler = new ThreadPoolTaskScheduler();
        // 因为该服务通常用于调试,http任务不会很多,默认设置核心线程数为1
        httpTaskScheduler.setPoolSize(taskSchedulerProperties.getThreadPoolSize());
        return httpTaskScheduler;
    }

    @Bean
    public ResponderExceptionConvertAspect responderExceptionConvertAdvice() {
        return new ResponderExceptionConvertAspect();
    }

}
