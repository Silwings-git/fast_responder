package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.core.result.Result;
import com.silwings.responder.task.HttpTaskInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @ClassName RequestConfigInfos
 * @Description 请求配置工具
 * @Author Silwings
 * @Date 2022/1/8 18:17
 * @Version V1.0
 **/
public class RequestConfigInfos {

    private static final Pattern HTTP_PATTERN = Pattern.compile("^http(s)?://.+$");

    /**
     * description: 检查请求配置信息是否完整
     * version: 1.0
     * date: 2022/1/8 19:20
     * author: Silwings
     *
     * @param infoStr 请求配置信息文本
     * @return com.silwings.responder.utils.RequestConfigInfos.CheckResult 结果信息.不可能为null
     */
    public static CheckResult checkRequestConfigInfo(final String infoStr) {

        if (StringUtils.isBlank(infoStr)) {
            return CheckResult.fail("配置信息为空.");
        }

        final RequestConfigInfo requestConfigInfo;
        try {
            requestConfigInfo = JSON.parseObject(infoStr, RequestConfigInfo.class);
        } catch (Exception e) {
            return CheckResult.fail("配置信息格式错误.");
        }

        if (null == requestConfigInfo) {
            return CheckResult.fail("配置信息格式错误");
        }

        if (StringUtils.isBlank(requestConfigInfo.getName())) {
            return CheckResult.fail("配置信息缺少名称");
        }
        if (StringUtils.isBlank(requestConfigInfo.getKeyUrl())) {
            return CheckResult.fail("配置信息缺少关键Url");
        }
        if (null == requestConfigInfo.getHttpMethod()) {
            return CheckResult.fail("配置信息缺少请求方式");
        }

        if (CollectionUtils.isNotEmpty(requestConfigInfo.getTasks())) {
            final List<HttpTaskInfo> tasks = requestConfigInfo.getTasks();

            for (HttpTaskInfo task : tasks) {
                if (null == task) {
                    continue;
                }

                if (StringUtils.isBlank(task.getName())) {
                    return CheckResult.fail("Http任务缺少名称.");
                }

                if (null == task.getContent()) {
                    return CheckResult.fail("Http任务缺少内容.");
                }

                if (RequestConfigInfos.invalidCondition(task.findCondition())) {
                    return CheckResult.fail("Http任务条件无效.");
                }

                final HttpTaskInfo.HttpTaskContent content = task.getContent();

                if (null == content.getHttpMethod()) {
                    return CheckResult.fail("Http任务缺少请求方式.");
                }
                if (StringUtils.isBlank(content.getRequestUrl())) {
                    return CheckResult.fail("Http任务缺少请求地址.");
                }
                if (!HTTP_PATTERN.matcher(content.getRequestUrl()).matches()) {
                    return CheckResult.fail("Http任务请求地址格式错误.");
                }
            }
        }

        if (CollectionUtils.isNotEmpty(requestConfigInfo.getResults())) {
            final List<Result> results = requestConfigInfo.getResults();

            for (Result result : results) {
                if (RequestConfigInfos.invalidCondition(result.findCondition())) {
                    return CheckResult.fail("返回值过滤条件无效.");
                }
            }
        }

        return CheckResult.ok();
    }

    /**
     * description: 检查条件是否无效
     * version: 1.0
     * date: 2022/1/8 22:31
     * author: Silwings
     *
     * @param condition 条件集
     * @return boolean true-无效 ,false-有效
     */
    private static boolean invalidCondition(final Condition condition) {
        for (Condition.Expression expression : condition.getExpressions()) {
            if (!expression.isValid()) {
                return true;
            }
        }
        return false;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CheckResult {
        private boolean whole = true;
        private String msg;

        static CheckResult ok() {
            return new CheckResult();
        }

        static CheckResult fail(final String msg) {
            return new CheckResult(false, msg);
        }
    }

}
