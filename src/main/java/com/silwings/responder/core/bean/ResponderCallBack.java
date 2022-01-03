package com.silwings.responder.core.bean;

import com.silwings.responder.commons.bean.BaseBean;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ResponderCallBack
 * @Description 回调
 * @Author Silwings
 * @Date 2021/8/7 11:27
 * @Version V1.0
 **/
public class ResponderCallBack extends BaseBean {

    /**
     * 回调地址
     */
    private String callbackUrl;

    /**
     * 回调参数
     */
    private String param;

    /**
     * 回调请求体
     */
    private Map<? super Object, ? super Object> body;

    /**
     * 回调请求方式
     */
    private String method;


    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public String getParam() {
        return null == this.param ? "" : this.param;
    }

    public Map getBody() {
        if (null == this.body) {
            return new HashMap<>();
        }
        return new HashMap<>(this.body);
    }

    public String getMethod() {
        return this.method;
    }

    public void setCallbackUrl(String callbackUrl) {
        if (null != this.callbackUrl) {
            throw new IllegalStateException("回调地址不可修改");
        }
        this.callbackUrl = callbackUrl;
    }

    public void setParam(String param) {
        if (null != this.param) {
            throw new IllegalStateException("回调参数不可修改");
        }
        this.param = param;
    }

    public void setBody(Map body) {
        if (null != this.body) {
            throw new IllegalStateException("回调请求体不可修改");
        }
        this.body = body;
    }

    public void setMethod(String method) {
        if (null != this.method) {
            throw new IllegalStateException("回调请求方式不可修改");
        }
        this.method = method;
    }

    public boolean hasCallback() {
        return StringUtils.isNotBlank(this.getCallbackUrl());
    }

}
