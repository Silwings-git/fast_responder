package com.silwings.db.bean.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName EnableConfigParam
 * @Description 启用禁用请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 17:27
 * @Version V1.0
 **/
@Setter
@Getter
public class EnableConfigParam {

    private Long id;

    private Integer enableStatus;

}
