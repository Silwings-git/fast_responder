package com.silwings.responder.callback;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CallBackManager
 * @Description 回调管理
 * @Author Silwings
 * @Date 2021/8/7 17:58
 * @Version V1.0
 **/
@Slf4j
public class CallBackManager {

    private PriorityBlockingQueue<CallBackTask> priorityBlockingQueue;

    public CallBackManager() {
        this.priorityBlockingQueue = new PriorityBlockingQueue<>(10, (t1, t2) -> {
            final long timeInterval = t1.getHandleTime() - t2.getHandleTime();
            if (0 == timeInterval) {
                return 0;
            }
            return timeInterval < 0 ? -1 : 1;
        });
    }

    public void add(CallBackTask callBackTask) {
        if (this.priorityBlockingQueue.size() > 100) {
            log.error("队列size达到上限,已经丢弃当前任务: {} ", JSON.toJSONString(callBackTask));
            return;
        }
        if (null == callBackTask || null == callBackTask.getHandleTime()) {
            log.error("callBackTask 数据补全,已丢弃: {} ", JSON.toJSONString(callBackTask));
            return;
        }

        this.priorityBlockingQueue.add(callBackTask);
    }

    public CallBackTask take() throws InterruptedException {
        final CallBackTask take = this.priorityBlockingQueue.take();
        final long l = take.getHandleTime() - System.currentTimeMillis();

        // 未到达执行时间的先暂停
        if (l > 0) {
            TimeUnit.MILLISECONDS.sleep(l);
        }
        return take;
    }


}
