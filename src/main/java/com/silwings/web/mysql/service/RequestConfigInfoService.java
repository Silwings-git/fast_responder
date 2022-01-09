package com.silwings.web.mysql.service;

import com.silwings.responder.interfaces.RequestConfigRepository;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.mysql.dto.RequestConfigInfoDto;

/**
 * @ClassName RequestConfigInfoService
 * @Description RequestConfigInfoService
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
public interface RequestConfigInfoService extends RequestConfigRepository {

    void insert(RequestConfigInfoDto insertInfo);

    void updateById(RequestConfigInfoDto updateInfo);

    PageData<RequestConfigInfoDto> query(RequestConfigInfoDto queryInfo);

    RequestConfigInfoDto findById(Long id);

    void enableConfig(Long id, EnableStatus enableStatus);

    void deleteById(Long id);
}
