package com.silwings.web.bean.result;

import com.silwings.web.bean.param.PageParam;
import com.silwings.web.enums.EnableStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName SaveResponderInfoConfigParam
 * @Description 新增请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 16:10
 * @Version V1.0
 **/
@Setter
@Getter
public class QueryResponderInfoConfigResult extends PageParam{

    private Long id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 配置的自定义名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String keyUrl;

    /**
     * 请求方式
     */
    private String httpMethod;

    /**
     * 启用状态
     */
    private Boolean enableStatus;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    public String getEnableStatusDesc() {
        final EnableStatus enableStatusEnum = EnableStatus.valueOfCode(this.enableStatus);
        if (null == enableStatusEnum) {
            return null;
        }
        return enableStatusEnum.desc();
    }

}
