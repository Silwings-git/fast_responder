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
import java.util.ArrayList;
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
public class LogsController implements ResponderEventListener {

    @Value("${web.httptask.querylogs.max-connect-number:10}")
    private Integer maxConnectNumber;

    @Value("${web.httptask.querylogs.sse-timeout:0}")
    private Long sseTimeout;

    private final List<SseEmitter> allLogSseEmitterList = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> httpTaskLogSseEmitterList = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/all")
    public SseEmitter queryLogs() {
        if (this.allLogSseEmitterList.size() >= this.maxConnectNumber) {
            this.closeSseEmitter(this.allLogSseEmitterList.get(0), this.allLogSseEmitterList);
        }

        final SseEmitter sseEmitter = new Utf8SseEmitter(this.sseTimeout);
        this.allLogSseEmitterList.add(sseEmitter);

        this.sendWelcomeStatement(sseEmitter);

        return sseEmitter;
    }

    @GetMapping(value = "/httpTask")
    public SseEmitter queryHttpTaskLogs() {
        if (this.httpTaskLogSseEmitterList.size() >= this.maxConnectNumber) {
            this.closeSseEmitter(this.httpTaskLogSseEmitterList.get(0), this.httpTaskLogSseEmitterList);
        }

        final SseEmitter sseEmitter = new Utf8SseEmitter(this.sseTimeout);
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
    public void doEvent(final ResponderEventPack eventPack) {

        this.doAllLogsEvent(eventPack);

        this.doHttpTaskLogsEvent(eventPack);
    }

    private void doAllLogsEvent(final ResponderEventPack eventPack) {

        final List<SseEmitter> waitCloseEmitterList = new ArrayList<>();

        for (SseEmitter nextEmitter : this.allLogSseEmitterList) {
            try {
                nextEmitter.send(eventPack.getMsg(), MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                waitCloseEmitterList.add(nextEmitter);
            }
        }

        waitCloseEmitterList.forEach(sseEmitter -> this.closeSseEmitter(sseEmitter, this.allLogSseEmitterList));
    }

    private void doHttpTaskLogsEvent(final ResponderEventPack eventPack) {
        final List<SseEmitter> waitCloseEmitterList = new ArrayList<>();

        // http task的执行由 httpTaskScheduler 触发,结果由 SimpleAsyncTaskExecutor 处理
        if (eventPack.getMsg().contains("httpTaskScheduler")
                || eventPack.getMsg().contains("SimpleAsyncTaskExecutor")) {

            for (SseEmitter sseEmitter : this.httpTaskLogSseEmitterList) {
                try {
                    sseEmitter.send(eventPack.getMsg(), MediaType.APPLICATION_JSON);
                } catch (Exception e) {
                    waitCloseEmitterList.add(sseEmitter);
                }
            }
        }

        waitCloseEmitterList.forEach(sseEmitter -> this.closeSseEmitter(sseEmitter, this.httpTaskLogSseEmitterList));
    }

    private void sendWelcomeStatement(final SseEmitter sseEmitter) {

        if (null == sseEmitter) {
            return;
        }

        try {
            sseEmitter.send("欢迎使用 Fast_Responder 快捷应答服务.");
        } catch (IOException e) {
            log.info("欢迎语推送失败");
        }
    }

}
