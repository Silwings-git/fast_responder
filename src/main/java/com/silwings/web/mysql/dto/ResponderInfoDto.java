package com.silwings.web.mysql.dto;

import com.silwings.web.bean.param.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @ClassName ResponderInfoEntity
 * @Description 请求信息配置实体
 * @Author Silwings
 * @Date 2022/1/8 14:57
 * @Version V1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class ResponderInfoDto extends PageParam {

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
    private Integer enableStatus;

    /**
     * 删除状态
     */
    private Integer logicDelete;

    /**
     * 实际数据
     */
    private String dataJson;

}
