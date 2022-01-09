package com.silwings.web.controller;

import com.silwings.responder.event.ResponderEventPack;
import com.silwings.responder.event.ResponderEventType;
import com.silwings.responder.interfaces.ResponderEventListener;
import com.silwings.web.bean.Utf8SseEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName HttpTaskLogController
 * @Description http 任务日志
 * @Author Silwings
 * @Date 2022/1/9 12:41
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/responder/logs")
public class HttpTaskController implements ResponderEventListener<String> {

    @Value("${web.httptask.querylogs.max-connect-number:10}")
    private Integer maxConnectNumber;

    private final CopyOnWriteArrayList<SseEmitter> allLogSseEmitterList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<SseEmitter> httpTaskLogSseEmitterList = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/all")
    public SseEmitter queryLogs() {
        if (this.allLogSseEmitterList.size() >= this.maxConnectNumber) {
            this.closeSseEmitter(this.allLogSseEmitterList.get(0), this.allLogSseEmitterList);
        }

        final SseEmitter sseEmitter = new Utf8SseEmitter(0L);
        this.allLogSseEmitterList.add(sseEmitter);

        this.sendWelcomeStatement(sseEmitter);

        return sseEmitter;
    }

    @GetMapping(value = "/httpTask")
    public SseEmitter queryHttpTaskLogs() {
        if (this.httpTaskLogSseEmitterList.size() >= this.maxConnectNumber) {
            this.closeSseEmitter(this.httpTaskLogSseEmitterList.get(0), this.httpTaskLogSseEmitterList);
        }

        final SseEmitter sseEmitter = new Utf8SseEmitter(0L);
        this.httpTaskLogSseEmitterList.add(sseEmitter);

        this.sendWelcomeStatement(sseEmitter);

        return sseEmitter;
    }

    private void closeSseEmitter(final SseEmitter sseEmitter, final List<SseEmitter> sseEmitterList) {
        if (null == sseEmitter || null == sseEmitterList) {
            return;
        }

        try {
            sseEmitterList.remove(sseEmitter);
            sseEmitter.onCompletion(() -> log.info("一个 SseEmitter 断开."));
            sseEmitter.complete();
        } catch (Exception e) {
            log.error("HttpTaskController#closeSseEmitter error.", e);
        }
    }

    @Override
    public ResponderEventType getEventType() {
        return ResponderEventType.PROJECT_LOG;
    }

    @Override
    public void doEvent(final ResponderEventPack<String> eventPack) {

        for (SseEmitter nextEmitter : this.allLogSseEmitterList) {
            try {
                nextEmitter.send(eventPack, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                // 读写分离集合,可以在遍历过程中remove
                this.closeSseEmitter(nextEmitter, allLogSseEmitterList);
            }
        }

        // http task的执行由 httpTaskScheduler 触发,结果由 SimpleAsyncTaskExecutor 处理
        if (eventPack.getData().getData().contains("httpTaskScheduler")
                || eventPack.getData().getData().contains("SimpleAsyncTaskExecutor")) {

            for (SseEmitter sseEmitter : this.httpTaskLogSseEmitterList) {
                try {
                    sseEmitter.send(eventPack, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    // 读写分离集合,可以在遍历过程中remove
                    this.closeSseEmitter(sseEmitter, httpTaskLogSseEmitterList);
                }
            }
        }
    }


    private void sendWelcomeStatement(final SseEmitter sseEmitter) {

        if (null == sseEmitter) {
            return;
        }

        try {
            sseEmitter.send(new ResponderEventPack<>(ResponderEventType.PROJECT_LOG, ResponderEventPack.simpleEventData("欢迎使用 Fast_Responder 快捷应答服务.")));
        } catch (IOException e) {
            log.info("欢迎语推送失败");
        }
    }

}
