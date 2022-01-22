package com.silwings.responder.task;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;

/**
 * @ClassName HttpTaskManager
 * @Description HttpTaskManager
 * @Author Silwings
 * @Date 2022/1/5 22:06
 * @Version V1.0
 **/
@Slf4j
public class HttpTaskManager {

    private final DelayQueue<HttpTask> queue = new DelayQueue<>();

    public void add(final HttpTask httpTask) {
        log.info("Http Task {} 被添加到执行队列. Task: {}", httpTask.getTaskName(), JSON.toJSONString(httpTask));
        this.queue.add(httpTask);
    }

    HttpTask poll() {
        return this.queue.poll();
    }

}
