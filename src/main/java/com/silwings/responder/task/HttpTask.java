package com.silwings.responder.task;

import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HttpTask
 * @Description HttpTask
 * @Author Silwings
 * @Date 2022/1/5 22:09
 * @Version V1.0
 **/
@Setter(AccessLevel.PROTECTED)
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class HttpTask implements Delayed {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private RequestMethod requestMethod;

    /**
     * url参数
     */
    private Map<String, String[]> params;

    /**
     * 请求体
     */
    private JSONObject body;

    /**
     * 执行时间(ms)
     */
    private Long runTime;

    /**
     * 延迟时间(s)
     */
    private Long delayTime;

    @Override
    public long getDelay(final TimeUnit unit) {
        return unit.convert(this.runTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(final Delayed task) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),task.getDelay(TimeUnit.MILLISECONDS));
    }

}
