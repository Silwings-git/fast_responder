package com.silwings.db.mysql.service;

import com.silwings.db.bean.result.PageData;
import com.silwings.db.enums.EnableStatus;
import com.silwings.db.mysql.dto.RequestConfigInfoDto;
import com.silwings.responder.interfaces.RequestConfigRepository;

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
