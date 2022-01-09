package com.silwings.web.mysql.service;

import com.silwings.responder.interfaces.ResponderInfoRepository;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.mysql.dto.ResponderInfoConfigDto;

/**
 * @ClassName ResponderInfoConfigService
 * @Description ResponderInfoConfigService
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
public interface ResponderInfoConfigService extends ResponderInfoRepository {

    void insert(ResponderInfoConfigDto insertInfo);

    void updateById(ResponderInfoConfigDto updateInfo);

    PageData<ResponderInfoConfigDto> query(ResponderInfoConfigDto queryInfo);

    ResponderInfoConfigDto findById(Long id);

    void enableConfig(Long id, EnableStatus enableStatus);

    void deleteById(Long id);
}
