package com.silwings.responder.core.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Result
 * @Description 响应
 * @Author Silwings
 * @Date 2022/1/3 18:55
 * @Version V1.0
 **/
@Setter
@Getter
public class Result {

    /**
     * 返回值名称
     */
    private String resultName;

    /**
     * 响应体
     */
    private JSONObject body;

    /**
     * 返回信息(仅当body不存在时尝试取该字段)
     */
    private String msg;

}
