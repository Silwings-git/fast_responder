package com.silwings.web.controller;

import com.silwings.responder.event.ResponderEventPack;
import com.silwings.responder.event.ResponderEventType;
import com.silwings.responder.interfaces.ResponderEventListener;
import com.silwings.web.bean.Utf8SseEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName HttpTaskLogController
 * @Description http 任务日志
 * @Author Silwings
 * @Date 2022/1/9 12:41
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/responder/httpTask")
public class HttpTaskController implements ResponderEventListener {

    @Value("${web.httptask.querylogs.max-connect-number:10}")
    private Integer maxConnectNumber;

    private final CopyOnWriteArrayList<SseEmitter> sseEmitterArrayList = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/logs")
    public SseEmitter queryLogs() {
        if (this.sseEmitterArrayList.size() >= this.maxConnectNumber) {
            this.closeSseEmitter(this.sseEmitterArrayList.get(0));
        }

        final SseEmitter sseEmitter = new Utf8SseEmitter(0L);
        this.sseEmitterArrayList.add(sseEmitter);

        return sseEmitter;
    }

    private void closeSseEmitter(final SseEmitter sseEmitter) {
        if (null == sseEmitter) {
            return;
        }

        try {
            this.sseEmitterArrayList.remove(sseEmitter);
            sseEmitter.onCompletion(() -> log.info("一个 SseEmitter 断开."));
            sseEmitter.complete();
        } catch (Exception e) {
            log.error("HttpTaskController#closeSseEmitter error.", e);
            // no codes
        }
    }

    @Override
    public ResponderEventType getEventType() {
        return ResponderEventType.HTTP_TASK_LOG;
    }

    @Override
    public void doEvent(final ResponderEventPack<?> eventPack) {

        for (SseEmitter nextEmitter : this.sseEmitterArrayList) {
            try {
                nextEmitter.send(eventPack, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                // 读写分离集合,可以在遍历过程中remove
                this.closeSseEmitter(nextEmitter);
            }
        }
    }

}
