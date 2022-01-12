package com.silwings.web.bean.result;

import com.silwings.responder.core.config.ResponderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName SaveResponderInfoConfigParam
 * @Description 新增请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 16:10
 * @Version V1.0
 **/
@Setter
@Getter
public class ResponderInfoConfigResult extends ResponderInfo {

    /**
     * 分类名称
     */
    private String categoryName;

}
