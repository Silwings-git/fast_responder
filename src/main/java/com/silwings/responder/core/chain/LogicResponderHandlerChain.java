package com.silwings.responder.core.chain;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.bean.ResponderLogic;
import com.silwings.responder.utils.JsonFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName LogicResponderHandlerChain
 * @Description LogicResponderHandlerChain
 * @Author Silwings
 * @Date 2021/8/7 14:33
 * @Version V1.0
 **/
@Slf4j
public class LogicResponderHandlerChain implements ResponderHandlerChain<ResponderBody> {
    @Override
    public boolean supportsParameter(ResponderBody responderBody) {
//        return null != responderBody && responderBody.hasLogic();
        return true;
    }

    @Override
    public ResponderBody handle(ResponderBody responderBody) {

//        final Map<String, Object> parameters = responderBody.getParameters();
//
//        final List<ResponderLogic.Logic> logicList = responderBody.findLogic();
//        for (ResponderLogic.Logic logic : logicList) {
//            String resultKey = logic.getResultKey();
//
//            Boolean eqR = null;
//
//            final Map<String, String> equalsCondition = logic.getEqualsCondition();

            // 比较所有 == 的情况
//            for (Map.Entry<String, String> entry : equalsCondition.entrySet()) {
//                final String conditionKey = entry.getKey();
//                final String conditionValue = entry.getValue();
//
//                final Object paramValue = parameters.get(conditionKey);
//                if ("null".equalsIgnoreCase(conditionValue) && null == paramValue) {
//                    // 当期望值和参数值同为null时eq条件成立
//                    eqR = true;
//                } else if (paramValue instanceof String && conditionValue.equals(paramValue)) {
//                    // 当参数值为字符串且和期望值相同时eq条件成立
//                    eqR = true;
//                } else if (null != paramValue
//                        && StringUtils.isNumeric(paramValue.toString())
//                        && StringUtils.isNumeric(conditionValue)
//                        && paramValue.toString().equals(conditionValue)) {
//                    eqR = true;
//                } else if (null != paramValue
//                        && LogicResponderHandlerChain.isJson(JSON.toJSONString(paramValue))
//                        && LogicResponderHandlerChain.isJson(conditionValue)
//                        && JsonFormatUtils.againFormatJson(conditionValue).equals(JSON.toJSONString(paramValue))) {
//                    eqR = true;
//                } else {
//                    eqR = false;
//                }
//            }
//
//            // 当==条件全部通过或没有==条件时,比较所有 != 的情况
//            if (null == eqR || Boolean.TRUE.equals(eqR)) {
//                final Map<String, String> differentCondition = logic.getDifferentCondition();
//
//                for (Map.Entry<String, String> entry : differentCondition.entrySet()) {
//                    final String diffKey = entry.getKey();
//                    final String diffValue = entry.getValue();
//
//                    final Object paramValue = parameters.get(diffKey);
//                    if ("null".equalsIgnoreCase(diffValue) && null != paramValue) {
//                        // 当paramValue不为null时条件成立  null!=value
//                        eqR = true;
//                    } else if (paramValue instanceof String && !diffValue.equals(paramValue)) {
//                        // a != b
//                        eqR = true;
//                    } else if (null != paramValue
//                            && StringUtils.isNumeric(paramValue.toString())
//                            && StringUtils.isNumeric(diffValue)
//                            && !paramValue.toString().equals(diffValue)) {
//                        eqR = true;
//                    } else if (null != paramValue
//                            && LogicResponderHandlerChain.isJson(JSON.toJSONString(paramValue))
//                            && LogicResponderHandlerChain.isJson(diffValue)
//                            && !JsonFormatUtils.againFormatJson(diffValue).equals(JSON.toJSONString(paramValue))) {
//                        eqR = true;
//                    } else {
//                        eqR = false;
//                    }
//                }
//            }
//
//            // 当有一个条件达成时直接返回
//            if (Boolean.TRUE.equals(eqR)) {
//                if (logic.isThrow()) {
//                    final RuntimeException ex = new RuntimeException(resultKey);
//                    responderBody.setEx(ex);
//                    log.error("logic error occurred !", ex);
//                } else {
//                    responderBody.setLogicResult(resultKey);
//                    log.info("logic correct ! ");
//                }
//                return responderBody;
//            }
//        }
//
//        if (null == responderBody.getLogicResult()) {
//            if (null == responderBody.getDefaultResult()) {
//                responderBody.setEx(new RuntimeException("没有匹配到任何符合条件的返回值"));
//            } else {
//                responderBody.setLogicResult(JSON.toJSONString(responderBody.getDefaultResult()));
//            }
//        }
//
//        return responderBody;
        return null;
    }

    private static boolean isJson(String str) {
        boolean result = false;
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            if ((str.startsWith("{") && str.endsWith("}"))
                    || (str.startsWith("[") && str.endsWith("]"))) {
                result = true;
            }
        }
        if (result) {
            try {
                JSON.parse(str);
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }


}
