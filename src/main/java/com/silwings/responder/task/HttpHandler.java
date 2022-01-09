package com.silwings.responder.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.Map;

/**
 * @ClassName HttpHandler
 * @Description HTTP任务处理器
 * @Author Silwings
 * @Date 2022/1/7 20:47
 * @Version V1.0
 **/
@Slf4j
public class HttpHandler {

    private HttpTaskManager httpTaskManager;
    private AsyncRestTemplate httpTaskRestTemplate;

    public HttpHandler(final HttpTaskManager httpTaskManager, final AsyncRestTemplate httpTaskRestTemplate) {
        this.httpTaskManager = httpTaskManager;
        this.httpTaskRestTemplate = httpTaskRestTemplate;
    }

    @Async("httpTaskScheduler")
    @Scheduled(cron = "${httptask.handler.cron:* * * * * ?}")
    public void handle() {

        final HttpTask task = this.httpTaskManager.poll();

        if (null != task) {
            this.request(task);
        }

    }

    private void request(final HttpTask httpTask) {

        if (this.invalidTask(httpTask)) {
            log.error("Http Task 无效. task name: {}.", null == httpTask ? "{}" : JSON.toJSONString(httpTask));
            return;
        }

        log.info("HttpTask {} 开始执行.", httpTask.getTaskName());

        final String realUrl = this.spliceRealRequestUrl(httpTask.getRequestUrl(), httpTask.getParams());

        final HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, String> headerEntry : httpTask.getHeaders().entrySet()) {
            httpHeaders.set(headerEntry.getKey(), headerEntry.getValue());
        }

        final HttpEntity<JSONObject> httpEntity = new HttpEntity<>(httpTask.getBody(), httpHeaders);

        final long start = System.currentTimeMillis();

        final ListenableFuture<ResponseEntity<String>> requestResult = this.httpTaskRestTemplate.exchange(realUrl, httpTask.getHttpMethod(), httpEntity, String.class, httpTask.getParams());

        requestResult.addCallback(result -> log.info("HttpTask {} 执行 {} 请求成功. 耗时 {} ms. 参数信息: {}. 响应信息: {}", httpTask.getTaskName(), httpTask.getHttpMethod(), System.currentTimeMillis() - start, JSON.toJSONString(httpTask), result.toString())
                , ex -> log.error("HttpTask {} 执行 {} 请求失败. 参数信息: {}. 错误信息: {}", httpTask.getTaskName(), httpTask.getHttpMethod(), JSON.toJSONString(httpTask), ex.getMessage()));

    }

    /**
     * description: 简单校验http task是否有效
     * 1.task不为null
     * 2.requestUrl 不为 empty
     * version: 1.0
     * date: 2022/1/7 21:02
     * author: Silwings
     *
     * @param task http task
     * @return boolean true-有效. false-无效
     */
    private boolean invalidTask(final HttpTask task) {
        return null == task || StringUtils.isBlank(task.getRequestUrl());
    }


    /**
     * description: 拼接真实请求地址
     * version: 1.0
     * date: 2022/1/7 21:35
     * author: Silwings
     *
     * @param requestUrl 请求地址
     * @param params     请求参数
     * @return java.lang.String
     */
    private String spliceRealRequestUrl(final String requestUrl, final Map<String, String[]> params) {

        if (StringUtils.isBlank(requestUrl) || MapUtils.isEmpty(params)) {
            return requestUrl;
        }

        final StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String[]> paramEntry : params.entrySet()) {
            for (String value : paramEntry.getValue()) {
                builder.append(paramEntry.getKey()).append("=").append(value).append("&");
            }
        }

        return requestUrl + (builder.length() > 0 ? ("?" + builder.substring(0, builder.length() - 1)) : "");
    }

}
