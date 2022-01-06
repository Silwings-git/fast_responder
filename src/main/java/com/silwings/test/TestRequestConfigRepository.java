package com.silwings.test;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.interfaces.RequestConfigRepository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName TestRequestConfigRepository
 * @Description 测试用
 * @Author Silwings
 * @Date 2022/1/3 21:34
 * @Version V1.0
 **/
public class TestRequestConfigRepository implements RequestConfigRepository {

    @Override
    public RequestConfigInfo findByKeyUrl(String keyUrl) {
        return JSON.parseObject(getStr(), RequestConfigInfo.class);
    }

    @Override
    public List<RequestConfigInfo> queryRestConfigByMethod(RequestMethod requestMethod) {
        return Collections.emptyList();
    }

    private static String getStr() {
        return "{\n" +
                "    \"name\":\"示例配置01\",\n" +
                "    \"keyUrl\":\"/silwings\",\n" +
                "    \"requestMethod\":\"GET\",\n" +
                "    \"urlType\":\"REST_FULL\",\n" +
                "    \"tasks\":[\n" +
                "        {\n" +
                "            \"name\":\"我的回调任务\",\n" +
                "            \"delayTime\":10,\n" +
                "            \"conditions\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"content\":{\n" +
                "                \"requestMethod\":\"POST\",\n" +
                "                \"requestUrl\":\"http://127.0.0.1:8899/payment/${id}/notify\",\n" +
                "                \"params\":{\n" +
                "                    \"age\":[\n" +
                "                        \"18\",\n" +
                "                        \"20\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"restParams\":{\n" +
                "                    \"id\":\"20\"\n" +
                "                },\n" +
                "                \"body\":{\n" +
                "                    \"paymentCallback\":{\n" +
                "                        \"param01\":\"${orderId}\",\n" +
                "                        \"param02\":{\n" +
                "                            \"orderId\":\"111\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"paymentCode\":\"UUID()\",\n" +
                "                    \"orderId\":\"${orderId}\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"resultConditions\":[\n" +
                "        {\n" +
                "            \"resultName\":\"resA\",\n" +
                "            \"conditions\":[\n" +
                "                \"1 == ${id}\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"results\":[\n" +
                "        {\n" +
                "            \"resultName\":\"default\",\n" +
                "            \"msg\":\"hello word\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"resultName\":\"resA\",\n" +
                "            \"body\":{\n" +
                "                \"value\":\"${id}\",\n" +
                "                \"key\":33\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

}
