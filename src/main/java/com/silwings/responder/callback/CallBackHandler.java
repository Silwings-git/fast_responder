package com.silwings.responder.callback;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CallBackHandler
 * @Description 回调处理器
 * @Author Silwings
 * @Date 2021/8/7 18:14
 * @Version V1.0
 **/
@Slf4j
public class CallBackHandler implements Runnable {

    private CallBackManager callBackManager;
    private RestTemplate restTemplate;

    public CallBackHandler(CallBackManager callBackManager, RestTemplate restTemplate) {
        this.callBackManager = callBackManager;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        CallBackTask callBackTask = null;
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            try {
                log.info("wait task...");
                callBackTask = this.callBackManager.take();
                log.info("start task...");
                if (null == callBackTask) {
                    continue;
                }

                if ("post".equalsIgnoreCase(callBackTask.getMethod())) {
                    final String postResult = this.restTemplate.postForObject(callBackTask.getCallbackUrl(), callBackTask.getBody(), String.class);
                    log.info("回调成功.回调信息: {} ,返回值信息: {}", JSON.toJSONString(callBackTask), postResult);
                } else if ("get".equalsIgnoreCase(callBackTask.getMethod())) {
                    log.error("暂不支持GET请求");
                } else {
                    log.error("不是支持的请求方式");
                }

            } catch (Exception e) {
                log.error("CallBackHandler#run error : ", e);
                log.error("CallBackHandler#run error . param {}: ", JSON.toJSONString(callBackTask));
            }
        }
    }
}
