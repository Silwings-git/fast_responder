package com.silwings.db.db_editor.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName VirtualServiceConfigDto
 * @Description VirtualServiceConfigDto
 * @Author Silwings
 * @Date 2021/8/8 12:20
 * @Version V1.0
 **/
@Setter
@Getter
public class VirtualServiceConfigResult {

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
    private String createTime;

    /**
     * update time
     */
    private String updateTime;


}
