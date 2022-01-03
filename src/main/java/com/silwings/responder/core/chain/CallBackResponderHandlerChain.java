package com.silwings.responder.core.chain;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.callback.CallBackManager;
import com.silwings.responder.callback.CallBackTask;
import com.silwings.responder.core.bean.ResponderCallBack;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

/**
 * @ClassName CallBackResponderHandlerChain
 * @Description 回调处理器
 * @Author Silwings
 * @Date 2021/8/7 14:36
 * @Version V1.0
 **/
@Slf4j
public class CallBackResponderHandlerChain implements ResponderHandlerChain<ResponderBody> {

    private static final String UUID_STR = "UUID()";

    private CallBackManager callBackManager;

    public CallBackResponderHandlerChain(CallBackManager callBackManager) {
        this.callBackManager = callBackManager;
    }

    @Override
    public boolean supportsParameter(ResponderBody responderBody) {
//        if (null != responderBody && responderBody.hasException()) {
//            log.info("callback handler Terminated due to an exception !");
//        }
//        return null != responderBody && responderBody.hasCallback() && !responderBody.hasException();
        return true;
    }

    @Override
    public ResponderBody handle(ResponderBody responderBody) {

//        final ResponderCallBack callBack = responderBody.getCallBack();
//
//        final long handleTime = System.currentTimeMillis() + 10 * 1000L;
//
//        final Map<String, Object> parameters = responderBody.getParameters();
//
//        String bodyJson = JSON.toJSONString(callBack.getBody());
//        String param = callBack.getParam();
//
//         处理随机数
//        bodyJson = bodyJson.replace(UUID_STR, UUID.randomUUID().toString());
//        param = param.replace(UUID_STR, UUID.randomUUID().toString());
//
//         处理字符替换
//        for (Map.Entry<String, Object> paramEntry : parameters.entrySet()) {
//            final String paramKey = paramEntry.getKey();
//            final Object paramValue = paramEntry.getValue();
//
//            bodyJson = bodyJson.replace("\"${" + paramKey + "}\"", JSON.toJSONString(null == paramValue ? "" : paramValue));
//            param = param.replace("${" + paramKey + "}", JSON.toJSONString(null == paramValue ? "" : paramValue));
//        }
//
//        final CallBackTask callBackTask = new CallBackTask(handleTime, callBack.getCallbackUrl(), param, JSON.parseObject(bodyJson, Map.class), callBack.getMethod());
//        log.info("callback task loaded: {}", JSON.toJSONString(callBackTask));
//        callBackManager.add(callBackTask);
//
//        return responderBody;
        return null;
    }

}
