package com.silwings.responder.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName RequestParamsAndBody
 * @Description 请求参数和请求体
 * @Author Silwings
 * @Date 2022/1/4 22:58
 * @Version V1.0
 **/
@Slf4j
@Setter
@Getter
public class RequestParamsAndBody {

    public static final RequestParamsAndBody EMPTY_OBJ = new RequestParamsAndBody(Collections.emptyMap(), Collections.emptyMap(), new JSONObject());

    /**
     * url参数
     */
    private Map<String, String[]> params;

    /**
     * rest参数
     */
    private Map<String, String> pathParams;

    /**
     * 请求体
     */
    private JSONObject body;

    RequestParamsAndBody(final Map<String, String[]> params, final Map<String, String> pathParams, final JSONObject body) {
        this.params = params;
        this.pathParams = pathParams;
        this.body = body;
    }

    public JSONObject getBody() {
        return null == this.body ? new JSONObject() : this.body;
    }

    /**
     * description: 从全部请求参数中获取参数信息
     * 该接口会依次尝试从url参数,rest参数,请求体中获取paramName对应的值.
     * version: 1.0
     * date: 2022/1/4 21:51
     * author: Silwings
     *
     * @param paramName 参数名
     * @return java.lang.Object 参数值
     */
    public Object getParam(final String paramName) {

        if (StringUtils.isBlank(paramName)) {
            log.info("查找 {} ,未发现匹配值.", paramName);
            return null;
        }

        if (this.getParams().containsKey(paramName)) {
            // param需要特殊处理
            final String[] paramArray = this.getParams().get(paramName);
            if (ArrayUtils.isEmpty(paramArray)) {
                return null;
            }
            if (paramArray.length == 1) {
                log.info("查找 {} ,匹配到length为1的数组,返回0角标值: {}", paramName, paramArray[0]);
                return paramArray[0];
            }

            log.info("查找 {} ,匹配到length大于1的数组,返回整个数组对象: {}", paramName, Arrays.toString(paramArray));
            return paramArray;
        }

        if (this.getPathParams().containsKey(paramName)) {
            log.info("查找 {} ,匹配到 Path Param 中元素: {}", paramName, this.getPathParams().get(paramName));
            return this.getPathParams().get(paramName);
        }

        // 尝试从请求体中获取参数
        final Object simpleJsonPathResult = this.simpleJsonPathEval(this.getBody(), paramName);
        log.info("查找 {} ,简单 Json Path匹配结果: {}", paramName, null == simpleJsonPathResult ? null : JSON.toJSONString(simpleJsonPathResult));

        final Object evalResult = JSONPath.eval(this.getBody(), paramName);
        log.info("查找 {} ,FastJson 标准 Json Path匹配结果: {}", paramName, null == evalResult ? null : JSON.toJSONString(evalResult));

        // 简单path和标准path同时查找,如果标准path有值,优先返回标准path结果
        return null == evalResult ? simpleJsonPathResult : evalResult;
    }

    /**
     * description: 通过自定义的简单Json Path 规则查找
     * 规则
     * 1.单层级直接指定key名
     * 2.复数层级直接使用逗号连接
     * 3.数组取值在key名后添加[],[]之间填写角标
     * version: 1.0
     * date: 2022/1/19 23:18
     * author: Silwings
     *
     * @param jsonObject json对象
     * @param simplePath 自定义path
     * @return java.lang.Object 结果
     */
    private Object simpleJsonPathEval(final JSONObject jsonObject, final String simplePath) {

        Object result = jsonObject;

        try {
            final String[] paramArray = simplePath.split("\\.");

            for (int i = 0; i <= paramArray.length - 1; i++) {

                if (null == result) {
                    break;
                }

                if (this.isArrayIndex(paramArray[i])) {

                    final String arrayKey = paramArray[i].replaceAll("\\[[0-9]+]", "");
                    if (StringUtils.isNotBlank(arrayKey)) {
                        // 如果指定了key,那么middleValue就必须是JSONObject类型,否则就应该退出循环,这里通过异常退出循环
                        result = ((JSONObject) result).get(arrayKey);
                    }

                    // 如果middleValue不是JSONArray,通过异常退出循环
                    final String indexStr = paramArray[i].replaceAll("\\S*\\[", "").replace("]", "");
                    result = ((JSONArray) result).toArray()[Integer.parseInt(indexStr)];

                } else {

                    // 如果middleValue不是JSONObject,通过异常退出循环
                    result = ((JSONObject) result).get(paramArray[i]);
                }
            }
        } catch (Exception e) {
            log.warn("使用简单JSON path搜索字段失败.失败原因: {}", e.getMessage());
        }

        return result;
    }

    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("\\S*\\[[0-9]+]");

    private boolean isArrayIndex(final String arrayIndex) {
        final Matcher matcher = ARRAY_INDEX_PATTERN.matcher(arrayIndex);
        return matcher.matches();
    }

}
