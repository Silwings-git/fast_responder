package com.silwings.web.bean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @ClassName Utf8SseEmitter
 * @Description Utf8SseEmitter
 * @Author Silwings
 * @Date 2022/1/9 14:40
 * @Version V1.0
 **/
public class Utf8SseEmitter extends SseEmitter {

    public Utf8SseEmitter(Long timeout) {
        super(timeout);
    }

    @Override
    protected void extendResponse(final ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);

        // 解决中文字符乱码问题
        final HttpHeaders headers = outputMessage.getHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
    }
}
