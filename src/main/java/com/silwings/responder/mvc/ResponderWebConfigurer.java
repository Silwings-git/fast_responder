package com.silwings.responder.mvc;

import com.silwings.responder.core.RequestContextFactory;
import com.silwings.responder.core.ResponderFlowManager;
import com.silwings.responder.interfaces.RequestConfigRepository;
import com.silwings.responder.task.HttpTaskFactory;
import com.silwings.responder.task.HttpTaskManager;
import com.silwings.responder.task.callback.CallBackHandler;
import com.silwings.responder.task.callback.CallBackManager;
import com.silwings.test.TestRequestConfigRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
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
public class ResponderWebConfigurer implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ResponderMappingArgumentResolver());
    }

    @Bean
    public CallBackManager callBackManager() {
        final CallBackManager callBackManager = new CallBackManager();
        // 启动后台回调线程
        final Thread thread = new Thread(new CallBackHandler(callBackManager, new RestTemplate()));
        thread.setDaemon(true);
        thread.setName("callback-task");
        thread.start();

        return callBackManager;
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
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
    public RequestConfigRepository requestConfigRepository() {
        return new TestRequestConfigRepository();
    }

    @Bean
    public HttpTaskManager httpTaskManager() {
        return new HttpTaskManager();
    }

    @Bean
    public ResponderFlowManager responderBodyHandlerManager() {
        return new ResponderFlowManager(httpTaskFactory(), httpTaskManager());
    }


}
