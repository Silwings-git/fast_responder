package com.silwings.web.config;

import com.silwings.web.execption.DbExceptionConvertAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName DbWebConfigurer
 * @Description web配置
 * @Author Silwings
 * @Date 2021/1/8 21:45
 * @Version V1.0
 **/
@Configuration
public class DbWebConfigurer implements WebMvcConfigurer {

    @Bean
    public DbExceptionConvertAspect dbExceptionConvertAdvice() {
        return new DbExceptionConvertAspect();
    }

}
