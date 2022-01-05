package com.silwings.responder.core.chain;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.commons.exception.ExceptionThreadLocal;
import com.silwings.responder.core.bean.HttpTaskInfo;
import com.silwings.responder.core.bean.RequestParamsAndBody;
import com.silwings.responder.core.operator.ResponderReplaceOperator;
import com.silwings.responder.task.HttpTaskFactory;
import com.silwings.responder.task.HttpTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName ResponderBodyHandlerManager
 * @Description ResponderBodyHandlerManager
 * @Author Silwings
 * @Date 2021/8/7 14:54
 * @Version V1.0
 **/
@Slf4j
public class ResponderBodyHandlerManager {

    private HttpTaskFactory httpTaskFactory;
    private HttpTaskManager httpTaskManager;

    public ResponderBodyHandlerManager(final HttpTaskFactory httpTaskFactory, final HttpTaskManager httpTaskManager) {
        this.httpTaskFactory = httpTaskFactory;
        this.httpTaskManager = httpTaskManager;
    }

    public ResponderBody handle(final ResponderBody responderBody) {

        this.performTask(responderBody.getTasks(), responderBody.getRequestContext().getRequestParamsAndBody());

        return null;
    }

    private void performTask(final List<HttpTaskInfo> tasks, final RequestParamsAndBody requestParamsAndBody) {

        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        tasks.stream()
                // 筛选符合运行条件的任务配置
                .filter(e -> e.findCondition().meet(requestParamsAndBody))
                // 创建任务
                .map(taskInfo -> this.httpTaskFactory.create(taskInfo, requestParamsAndBody))
                // 加入执行队列等待执行
                .forEach(this.httpTaskManager::add);

        ExceptionThreadLocal.get().forEach(e -> log.error("错误信息:", e));

    }


    public static void main(String[] args) {
        final RequestParamsAndBody paramsAndBody = new RequestParamsAndBody(Collections.emptyMap(), Collections.emptyMap(), JSON.parseObject("{\n" +
                "    \"userId\":{\n" +
                "        \"id\":[\n" +
                "            1,2,3\n" +
                "        ]\n" +
                "    },\n" +
                "    \"age\":10,\n" +
                "    \"sex\":1\n" +
                "}"));

        final Object replace = ResponderReplaceOperator.SEARCH_REPLACE.replace("https://localhost:8080/${userId.id}/${age}/${sex}?name=张三", paramsAndBody);
        System.out.println("replace = " + replace);
    }

    public static void main0(String[] args) {

        String input = "${ADFSGSabc}&${iiii}";
        final String regex = "\\$\\{[\\S]+?}";

        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            System.out.println(matcher.group());
            input = matcher.replaceFirst("222");
            matcher = pattern.matcher(input);
        }
        System.out.println("input = " + input);
    }


}
