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
    public List<RequestConfigInfo> queryRestConfigByMethod(RequestMethod requestMethod) {
        return Collections.emptyList();
    }

    @Override
    public List<RequestConfigInfo> queryByKeyUrl(String url) {
        return Collections.singletonList(JSON.parseObject(getStr(), RequestConfigInfo.class));

    }

    private static String getStr() {
        return "{\n" +
                "    \"name\":\"示例配置01\",\n" +
                "    \"keyUrl\":\"/silwings\",\n" +
                "    \"requestMethod\":\"GET\",\n" +
                "    \"tasks\":[\n" +
                "        {\n" +
                "            \"name\":\"我的回调任务\",\n" +
                "            \"delayTime\":10,\n" +
                "            \"conditions\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"content\":{\n" +
                "                \"requestMethod\":\"POST\",\n" +
                "                \"requestUrl\":\"http://localhost:8081/getUser\",\n" +
                "                \"headers\":{\n" +
                "                    \"abc\":\"1122v\"\n" +
                "                },\n" +
                "                \"params\":{\n" +
                "                    \"id\":[\n" +
                "                        10\n" +
                "                    ],\n" +
                "                    \"whh\":[\n" +
                "                        \"白井\",\n" +
                "                        \"黑子\"\n" +
                "                    ]\n" +
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
                "                \"[\\\"1\\\",\\\"id002\\\"] == ${id}\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"resultName\":\"resB\",\n" +
                "            \"conditions\":[\n" +
                "\n" +
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
                "        },\n" +
                "        {\n" +
                "            \"resultName\":\"resB\",\n" +
                "            \"body\":{\n" +
                "                \"v1\":\"-RDBoolean()-\",\n" +
                "                \"v2\":\"-RDInt()-\",\n" +
                "                \"v3\":\"-RDInt(10)-\",\n" +
                "                \"v4\":\"-RDInt(10,12)-\",\n" +
                "                \"v5\":\"-RDLong()-\",\n" +
                "                \"v6\":\"-RDLong(10)-\",\n" +
                "                \"v7\":\"-RDLong(-10,12)-\",\n" +
                "                \"v8\":\"-RDDouble()-\",\n" +
                "                \"v9\":\"-RDDouble(10)-\",\n" +
                "                \"v10\":\"-RDDouble(-10,12)-\",\n" +
                "                \"v11\":\"-RDDouble(10.555)-\",\n" +
                "                \"v12\":\"-RDDouble(-10.0,10.2)-\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

}
