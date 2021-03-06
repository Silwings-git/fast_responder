package com.silwings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName VrApplication
 * @Description 启动类
 * @Author Silwings
 * @Date 2021/8/6 18:34
 * @Version V1.0
 **/
@EnableScheduling
@SpringBootApplication
public class FastResponderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastResponderApplication.class, args);
    }
}
