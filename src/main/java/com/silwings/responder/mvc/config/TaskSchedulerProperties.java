package com.silwings.responder.mvc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName TaskSchedulerProperties
 * @Description TaskSchedulerProperties
 * @Author Silwings
 * @Date 2022/1/9 11:24
 * @Version V1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "httptask.scheduler")
@PropertySource({"classpath:application.yml"})
public class TaskSchedulerProperties {

    private int threadPoolSize = 1;

}
