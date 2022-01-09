package com.silwings.responder.mvc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName HttpTaskRestTemplateProperties
 * @Description http任务的http客户端配置
 * @Author Silwings
 * @Date 2022/1/9 11:29
 * @Version V1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "httptask.resttemplate")
public class HttpTaskRestTemplateProperties {

    /**
     * 连接时间
     */
    private int connectTimeout = 20_000;

    /**
     * 读取时间
     */
    private int readTimeout = 20_000;

}
