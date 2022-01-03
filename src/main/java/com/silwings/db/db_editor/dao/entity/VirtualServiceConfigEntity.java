package com.silwings.db.db_editor.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
@Table(name = "virtual_service_config")
public class VirtualServiceConfigEntity {

    public static final String C_ID = "id";
    public static final String C_KEY_URL = "keyUrl";
    public static final String C_REQ_CONFIG_INFO = "reqConfigInfo";
    public static final String C_CREATE_TIME = "createTime";
    public static final String C_UPDATE_TIME = "updateTime";
    public static final String C_LOGIC_DELETE = "logicDelete";


    /**
     * primary key
     */
    @Id
    private Long id;
    /**
     * 请求地址
     */
    @Column(name = "key_url")
    private String keyUrl;
    /**
     * 服务配置信息
     */
    @Column(name = "req_config_info")
    private String reqConfigInfo;
    /**
     * create time
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * update time
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * logic delete (0-normal，1-deleted)
     */
    @Column(name = "logic_delete")
    private Integer logicDelete;

}
