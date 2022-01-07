package com.silwings.responder.core.config;


import com.silwings.responder.core.result.Result;
import com.silwings.responder.core.result.ResultCondition;
import com.silwings.responder.task.HttpTaskInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

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
     * 请求的任务集
     */
    private List<HttpTaskInfo> tasks;

    /**
     * 返回值过滤
     */
    private List<ResultCondition> resultConditions;

    /**
     * 可用返回值
     */
    private List<Result> results;

    public boolean matchUrl(final String url, final PathMatcher pathMatcher) {
        return pathMatcher.match(this.keyUrl, url);
    }

    public List<HttpTaskInfo> getTasks() {
        return CollectionUtils.isEmpty(this.tasks) ? Collections.emptyList() : this.tasks;
    }

    public List<ResultCondition> getResultConditions() {
        return CollectionUtils.isEmpty(this.resultConditions) ? Collections.emptyList() : this.resultConditions;
    }

    public List<Result> getResults() {
        return CollectionUtils.isEmpty(this.results) ? Collections.emptyList() : this.results;
    }
}
