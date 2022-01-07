package com.silwings.responder.interfaces;

import com.silwings.responder.core.config.RequestConfigInfo;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName RequestConfigRepository
 * @Description 请求配置仓库
 * @Author Silwings
 * @Date 2022/1/3 19:19
 * @Version V1.0
 **/
public interface RequestConfigRepository {

    List<RequestConfigInfo> queryRestConfigByMethod(RequestMethod requestMethod);

    List<RequestConfigInfo> queryByKeyUrl(String url);
}
