package com.silwings.web.bean.param;

import com.silwings.responder.core.config.RequestConfigInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName SaveRequestConfigInfoParam
 * @Description 新增请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 16:10
 * @Version V1.0
 **/
@Setter
@Getter
public class SaveRequestConfigInfoParam extends RequestConfigInfo {

    /**
     * 分类名称
     */
    private String categoryName;

}
