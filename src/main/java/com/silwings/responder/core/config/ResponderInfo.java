package com.silwings.responder.core.config;


import com.alibaba.fastjson.JSONObject;
import com.silwings.responder.core.result.Result;
import com.silwings.responder.task.HttpTaskInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName ResponderInfo
 * @Description 应答器类
 * @Author Silwings
 * @Date 2021/8/7 11:19
 * @Version V1.0
 **/
@Setter
@Getter
public class ResponderInfo {

    /**
     * 配置的自定义名称
     */
    private String name;

    /**
     * 配置的url
     */
    private String keyUrl;

    /**
     * 延迟时间时间.最少延迟多久返回.单位毫秒
     */
    private Long delayTime = 0L;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 自定义参数
     */
    private JSONObject customizeParam;

    /**
     * 请求的任务集
     */
    private List<HttpTaskInfo> tasks;

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

    public List<Result> getResults() {
        return CollectionUtils.isEmpty(this.results) ? Collections.emptyList() : this.results;
    }
}
