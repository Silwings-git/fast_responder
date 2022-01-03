package com.silwings.db.db_editor.bean.dto;

import com.silwings.db.db_editor.bean.param.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName QueryReqConfigParam
 * @Description QueryReqConfigParam
 * @Author Silwings
 * @Date 2021/8/7 22:27
 * @Version V1.0
 **/
@Setter
@Getter
public class QueryReqConfigDto extends PageParam {

    /**
     * 该配置信息对应的请求地址
     */
    private String keyUrl;

}
