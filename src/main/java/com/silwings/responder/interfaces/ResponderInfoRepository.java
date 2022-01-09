package com.silwings.responder.interfaces;

import com.silwings.responder.core.config.ResponderInfo;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName ResponderInfoRepository
 * @Description 请求配置仓库
 * @Author Silwings
 * @Date 2022/1/3 19:19
 * @Version V1.0
 **/
public interface ResponderInfoRepository {

    List<ResponderInfo> queryByKeyUrl(String url);

    List<ResponderInfo> queryRestConfigByMethod(HttpMethod httpMethod);

}
