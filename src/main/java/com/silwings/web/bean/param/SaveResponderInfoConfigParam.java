package com.silwings.web.bean.param;

import com.silwings.responder.core.config.ResponderInfo;
import com.silwings.web.execption.DbException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

/**
 * @ClassName SaveResponderInfoConfigParam
 * @Description 新增请求配置接口参数
 * @Author Silwings
 * @Date 2022/1/8 16:10
 * @Version V1.0
 **/
@Setter
@Getter
public class SaveResponderInfoConfigParam extends ResponderInfo {

    /**
     * 分类名称
     */
    private String categoryName;

    public void validate() {

        if (StringUtils.isBlank(this.getName())) {
            throw new DbException("请输入name");
        }

    }

    public void setHttpMethod(final String httpMethodStr) {

        if (StringUtils.isBlank(httpMethodStr)) {
            return;
        }

        this.setHttpMethod(HttpMethod.valueOf(httpMethodStr.toUpperCase()));
    }

}
