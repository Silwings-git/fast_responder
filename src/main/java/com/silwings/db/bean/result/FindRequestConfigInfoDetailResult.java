package com.silwings.db.bean.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName FindRequestConfigInfoDetailResult
 * @Description 查询配置信息详情接口返回值
 * @Author Silwings
 * @Date 2022/1/8 17:05
 * @Version V1.0
 **/
@Setter
@Getter
public class FindRequestConfigInfoDetailResult {

    private Long id;

    private RequestConfigInfoResult configInfo;

}
