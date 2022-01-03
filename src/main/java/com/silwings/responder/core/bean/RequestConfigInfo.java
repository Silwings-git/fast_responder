package com.silwings.responder.core.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.commons.enums.UrlType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RequestConfigInfo
 * @Description 请求配置映射类
 * @Author Silwings
 * @Date 2021/8/7 11:19
 * @Version V1.0
 **/
@Setter
@Getter
public class RequestConfigInfo {

    /**
     * 配置的自定义名称
     */
    private String name;

    /**
     * 配置的url
     */
    private String keyUrl;

    /**
     * 请求方式
     */
    private RequestMethod requestMethod;

    /**
     * url的类型
     */
    private UrlType urlType;

    /**
     * 请求的任务集
     */
    private List<HttpTask> tasks;

    /**
     * 返回值过滤
     */
    private List<FilterResult> filterResult;

    /**
     * 可用返回值
     */
    private List<Result> results;


    public static void main(String[] args) {
        final RequestConfigInfo requestConfigInfo = JSON.parseObject(getStr(), RequestConfigInfo.class);

        System.out.println("requestConfigInfo = " + requestConfigInfo);
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
                "                \"\"\n" +
                "            ],\n" +
                "            \"content\":{\n" +
                "                \"requestMethod\":\"POST\",\n" +
                "                \"requestUrl\":\"http://127.0.0.1:8899/payment/{id}/notify\",\n" +
                "                \"params\":{\n" +
                "                    \"age\":\"18\"\n" +
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
                "    \"filterResult\":[\n" +
                "        {\n" +
                "            \"resultName\":\"defailt\",\n" +
                "            \"conditions\":[\n" +
                "                \"\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"results\":[\n" +
                "        {\n" +
                "            \"resultName\":\"default\",\n" +
                "            \"body\":{\n" +
                "                \"value\":\"CB{orderId}\",\n" +
                "                \"key\":\"CB{paymentCode}\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }


}
