package com.silwings.responder.core.chain;

import com.silwings.responder.core.bean.Condition;
import com.silwings.responder.core.bean.HttpTask;
import com.silwings.responder.core.bean.RequestParamsAndBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @ClassName ResponderBodyHandlerManager
 * @Description ResponderBodyHandlerManager
 * @Author Silwings
 * @Date 2021/8/7 14:54
 * @Version V1.0
 **/
@Slf4j
public class ResponderBodyHandlerManager {

    public ResponderBody handle(final ResponderBody responderBody) {

        this.performTask(responderBody.getTasks(), responderBody.getRequestContext().getRequestParamsAndBody());

        return null;
    }

    private void performTask(final List<HttpTask> tasks, final RequestParamsAndBody requestParamsAndBody) {

        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 判断执行条件
        for (HttpTask task : tasks) {

            final Condition condition = task.findConditions();

            boolean reach = condition.calculate(requestParamsAndBody);

            if (reach) {
                // TODO_Silwings: 2022/1/4 创建任务
                log.info("task条件满足");
            }else {
                log.error("task条件不满足");
            }

        }


        // 创建task任务


    }
}
