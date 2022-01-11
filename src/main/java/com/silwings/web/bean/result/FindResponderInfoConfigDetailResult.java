package com.silwings.web.bean.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName FindResponderInfoConfigDetailResult
 * @Description 查询配置信息详情接口返回值
 * @Author Silwings
 * @Date 2022/1/8 17:05
 * @Version V1.0
 **/
@Setter
@Getter
public class FindResponderInfoConfigDetailResult {

    private Long id;

    private Integer enableStatus;

    private ResponderInfoConfigResult responderInfoDetail;

}
