package com.silwings.db.db_editor.bean.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName VirtualServiceConfigDto
 * @Description VirtualServiceConfigDto
 * @Author Silwings
 * @Date 2021/8/8 12:20
 * @Version V1.0
 **/
@Setter
@Getter
public class VirtualServiceConfigDto {

    /**
     * 请求地址
     */
    private String keyUrl;

    /**
     * 服务配置信息
     */
    private String reqConfigInfo;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;


}
