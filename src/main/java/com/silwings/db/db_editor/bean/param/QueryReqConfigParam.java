package com.silwings.db.db_editor.bean.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName QueryReqConfigParam
 * @Description 查询配置信息请求参数
 * @Author Silwings
 * @Date 2021/8/7 22:27
 * @Version V1.0
 **/
@Setter
@Getter
public class QueryReqConfigParam extends PageParam {

    /**
     * 该配置信息对应的请求地址
     */
    private String keyUrl;

}
