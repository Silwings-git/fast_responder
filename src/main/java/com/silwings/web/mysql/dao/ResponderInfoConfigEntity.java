package com.silwings.web.mysql.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName ResponderInfoConfigEntity
 * @Description 请求信息配置实体
 * @Author Silwings
 * @Date 2022/1/8 14:57
 * @Version V1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "responder_info_config")
public class ResponderInfoConfigEntity {

    public static final String C_ID = "id";
    public static final String C_CATEGORY_NAME = "categoryName";
    public static final String C_NAME = "name";
    public static final String C_KEY_URL = "keyUrl";
    public static final String C_HTTP_METHOD = "httpMethod";
    public static final String C_ENABLE_STATUS = "enableStatus";
    public static final String C_LOGIC_DELETE = "logicDelete";

    @Id
    private Long id;

    /**
     * 分类名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 配置的自定义名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 请求地址
     */
    @Column(name = "key_url")
    private String keyUrl;

    /**
     * 请求方式
     */
    @Column(name = "http_method")
    private String httpMethod;

    /**
     * 启用状态
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 删除状态
     */
    @Column(name = "logic_delete")
    private Integer logicDelete;

    /**
     * 实际数据
     */
    @Column(name = "data_json")
    private String dataJson;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}
