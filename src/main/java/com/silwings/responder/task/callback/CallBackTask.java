package com.silwings.responder.task.callback;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CallBackTask
 * @Description 回调任务封装
 * @Author Silwings
 * @Date 2021/8/7 18:01
 * @Version V1.0
 **/
@Getter
public class CallBackTask {

    /**
     * 执行时间
     */
    private Long handleTime;

    /**
     * 回调地址
     */
    private String callbackUrl;

    /**
     * 回调参数
     */
    private String param;

    /**
     * 回调请求体
     */
    private Map<? super Object, ? super Object> body;

    /**
     * 回调请求方式
     */
    private String method;

    public CallBackTask(Long handleTime, String callbackUrl, String param, Map<? super Object, ? super Object> body, String method) {
        this.handleTime = null == handleTime ? System.currentTimeMillis() + 2 * 60 * 1000L : handleTime;
        this.callbackUrl = null == callbackUrl ? "" : callbackUrl;
        this.param = null == param ? "" : param;
        this.body = null == body ? new HashMap<>() : body;
        this.method = StringUtils.isBlank(method) ? "post" : method;
    }

}
