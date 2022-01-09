package com.silwings.web.mysql.service;

import com.silwings.responder.interfaces.ResponderInfoRepository;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.mysql.dto.ResponderInfoDto;

/**
 * @ClassName ResponderInfoInfoService
 * @Description ResponderInfoInfoService
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
public interface ResponderInfoInfoService extends ResponderInfoRepository {

    void insert(ResponderInfoDto insertInfo);

    void updateById(ResponderInfoDto updateInfo);

    PageData<ResponderInfoDto> query(ResponderInfoDto queryInfo);

    ResponderInfoDto findById(Long id);

    void enableConfig(Long id, EnableStatus enableStatus);

    void deleteById(Long id);
}
