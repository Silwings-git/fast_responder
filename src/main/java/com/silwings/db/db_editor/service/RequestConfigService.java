package com.silwings.db.db_editor.service;

import com.silwings.db.db_editor.bean.param.InsertOrUpdateConfigParam;
import com.silwings.responder.core.bean.RequestConfigInfo;
import com.silwings.db.db_editor.bean.dto.QueryReqConfigDto;
import com.silwings.db.db_editor.bean.dto.VirtualServiceConfigDto;
import com.silwings.db.db_editor.bean.vo.PageData;

/**
 * @ClassName RequestConfigService
 * @Description RequestConfigService
 * @Author Silwings
 * @Date 2021/8/6 23:12
 * @Version V1.0
 **/
public interface RequestConfigService {
    RequestConfigInfo findConfigByUrl(String lookupPath);

    PageData<VirtualServiceConfigDto> queryReqConfigPage(QueryReqConfigDto queryParam);

    String insertOrUpdateConfig(InsertOrUpdateConfigParam param);

    String delReqConfigByKeyUrl(String keyUrl);
}
