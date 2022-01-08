package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.codition.Condition;
import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.core.result.ResultCondition;
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
     * @return com.silwings.responder.utils.RequestConfigInfos.Result 结果信息.不可能为null
     */
    public static Result checkRequestConfigInfo(final String infoStr) {

        if (StringUtils.isBlank(infoStr)) {
            return Result.fail("配置信息为空.");
        }

        final RequestConfigInfo requestConfigInfo;
        try {
            requestConfigInfo = JSON.parseObject(infoStr, RequestConfigInfo.class);
        } catch (Exception e) {
            return Result.fail("配置信息格式错误.");
        }

        if (null == requestConfigInfo) {
            return Result.fail("配置信息格式错误");
        }

        if (StringUtils.isBlank(requestConfigInfo.getName())) {
            return Result.fail("配置信息缺少名称");
        }
        if (StringUtils.isBlank(requestConfigInfo.getKeyUrl())) {
            return Result.fail("配置信息缺少关键Url");
        }
        if (null == requestConfigInfo.getRequestMethod()) {
            return Result.fail("配置信息缺少请求方式");
        }

        if (CollectionUtils.isNotEmpty(requestConfigInfo.getTasks())) {
            final List<HttpTaskInfo> tasks = requestConfigInfo.getTasks();

            for (HttpTaskInfo task : tasks) {
                if (null == task) {
                    continue;
                }

                if (StringUtils.isBlank(task.getName())) {
                    return Result.fail("Http任务缺少名称.");
                }

                if (null == task.getContent()) {
                    return Result.fail("Http任务缺少内容.");
                }

                if (RequestConfigInfos.checkCondition(task.getConditions(), task.findCondition())) {
                    return Result.fail("Http任务条件无效.");
                }

                final HttpTaskInfo.HttpTaskContent content = task.getContent();

                if (null == content.getRequestMethod()) {
                    return Result.fail("Http任务缺少请求方式.");
                }
                if (StringUtils.isBlank(content.getRequestUrl())) {
                    return Result.fail("Http任务缺少请求地址.");
                }
                if (!HTTP_PATTERN.matcher(content.getRequestUrl()).matches()) {
                    return Result.fail("Http任务请求地址格式错误.");
                }
            }
        }

        if (CollectionUtils.isNotEmpty(requestConfigInfo.getResultConditions())) {
            final List<ResultCondition> resultConditions = requestConfigInfo.getResultConditions();
            for (ResultCondition resultCondition : resultConditions) {
                if (StringUtils.isBlank(resultCondition.getResultName())) {
                    return Result.fail("返回值过滤条件缺少返回值名称.");
                }
                if (RequestConfigInfos.checkCondition(resultCondition.getConditions(), resultCondition.findCondition())) {
                    return Result.fail("返回值过滤条件无效.");
                }
            }
        }

        return Result.ok();
    }

    private static boolean checkCondition(final List<String> conditions, final Condition condition) {
        if (CollectionUtils.isNotEmpty(conditions)) {
            for (Condition.Expression expression : condition.getExpressions()) {
                if (!expression.isValid()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Result {
        private boolean whole = true;
        private String msg;

        static Result ok() {
            return new Result();
        }

        static Result fail(final String msg) {
            return new Result(false, msg);
        }
    }

}
