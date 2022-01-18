package com.silwings.responder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.silwings.responder.core.config.ResponderInfo;
import com.silwings.responder.core.result.Result;
import com.silwings.responder.task.HttpTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DemoDataTest
 * @Description DemoDataTest
 * @Author Silwings
 * @Date 2022/1/17 1:00
 * @Version V1.0
 **/
@Slf4j
public class DemoDataTest {

    /**
     * description: 打印一个示例json,用于复制
     * version: 1.0
     * date: 2022/1/22 1:06
     * author: Silwings
     */
    @Test
    public void printDemoJson() {
        final ResponderInfo responderInfo = new ResponderInfo();
        responderInfo.setName("Demo config");
        responderInfo.setKeyUrl("/demo/url");
        responderInfo.setHttpMethod(HttpMethod.GET);
        final HttpTaskInfo taskInfo = new HttpTaskInfo();
        taskInfo.setName("My http task");
        taskInfo.setDelayTime(2000L);
        taskInfo.setConditions(Arrays.asList("1 == 1", "${age} >= 10", "name =IsNotBlank="));
        final HttpTaskInfo.HttpTaskContent taskContent = new HttpTaskInfo.HttpTaskContent();
        taskContent.setHttpMethod(HttpMethod.GET);
        taskContent.setRequestUrl("http://localhost:8088/hello/word");
        final HashMap<String, String> headers = new HashMap<>();
        headers.put("authToken", "8888888888");
        taskContent.setHeaders(headers);
        final Map<String, String[]> params = new HashMap<>();
        params.put("keyA", new String[]{"key_Av1", "keyA_v2"});
        params.put("keyB", new String[]{"keyB_v1"});
        taskContent.setParams(params);
        final JSONObject body = new JSONObject();
        body.put("name", "${name}");
        body.put("id", "-UUID()-");
        body.put("timestemp", "-TSNow(ms)-");
        taskContent.setBody(body);
        taskInfo.setContent(taskContent);
        responderInfo.setTasks(Collections.singletonList(taskInfo));
        final Result resultA = new Result();
        resultA.setResultName("My Result A");
        resultA.setMsg("Hello Word !");
        resultA.setConditions(Arrays.asList("${age} == 18"));
        final Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("authToken", "-RDInt(10，100)-");
        resultA.setHeaders(responseHeader);
        final Result resultB = new Result();
        resultB.setResultName("My Result B");
        resultB.setConditions(Arrays.asList("${age} == 15"));
        final Map<String, String> responseHeaderB = new HashMap<>();
        responseHeader.put("authToken", "-TSFNow(yyyy-MM-dd HH:mm:ss)-");
        resultB.setHeaders(responseHeaderB);
        final JSONObject resultBBody = new JSONObject();
        resultBBody.put("fast", "responder");
        resultB.setBody(resultBBody);
        responderInfo.setResults(Arrays.asList(resultA, resultB));

        log.info(JSON.toJSONString(responderInfo, SerializerFeature.WriteMapNullValue));
    }

}
