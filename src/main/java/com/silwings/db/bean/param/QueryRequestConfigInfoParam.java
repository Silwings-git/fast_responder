package com.silwings.db.bean.param;

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
public class QueryRequestConfigInfoParam extends PageParam{

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
    private String requestMethod;

    /**
     * 启用状态
     */
    private Integer enableStatus;

}
