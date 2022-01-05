package com.silwings.responder.core.bean;


import com.silwings.responder.commons.enums.UrlType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

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
     * url的类型
     */
    private UrlType urlType;

    /**
     * 请求的任务集
     */
    private List<HttpTaskInfo> tasks;

    /**
     * 返回值过滤
     */
    private List<FilterResult> filterResult;

    /**
     * 可用返回值
     */
    private List<Result> results;

    public boolean matchUrl(final String url, final PathMatcher pathMatcher) {
        return pathMatcher.match(this.keyUrl, url);
    }

}
