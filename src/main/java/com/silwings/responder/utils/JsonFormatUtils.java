package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.silwings.responder.commons.exception.ResponderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName JsonFormatUtils
 * @Description JsonFormatUtils
 * @Author Silwings
 * @Date 2021/8/8 20:11
 * @Version V1.0
 **/
@Slf4j
public class JsonFormatUtils {
    /**
     * description: 将json数据格式化
     * version: 1.0
     * date: 2021/7/13 23:38
     * author: Silwings
     *
     * @param param json格式的字符串
     * @return java.lang.String
     */
    public static String formatJsonStretch(final String param) {
        try {
            final JSONObject jsonObject = JSON.parseObject(param);
            return JSON.toJSONString(jsonObject,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
        } catch (JSONException e) {
            throw new ResponderException("json格式错误");
        }
    }

    /**
     * description: 将json数据格式化
     * version: 1.0
     * date: 2021/7/13 23:38
     * author: Silwings
     *
     * @param param json格式的字符串
     * @return java.lang.String
     */
    public static String againFormatJson(final String param) {
        if (StringUtils.isBlank(param)) {
            return param;
        }
        try {
            final JSONObject jsonObject = JSON.parseObject(param);
            return JSON.toJSONString(jsonObject);
        } catch (JSONException e) {
            log.error("againFormatJson()json转换异常", e);
            return param;
        }
    }

}
