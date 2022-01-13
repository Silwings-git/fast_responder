package com.silwings.web.bean.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName EnableResponderInfoParam
 * @Description 启用禁用请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 17:27
 * @Version V1.0
 **/
@Setter
@Getter
public class EnableResponderInfoParam {

    private Long id;

    private Boolean enableStatus;

}
