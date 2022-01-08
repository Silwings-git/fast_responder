package com.silwings.db.mysql.service.impl;

import com.alibaba.fastjson.JSON;
import com.silwings.db.mysql.dao.RequestConfigInfoEntity;
import com.silwings.db.mysql.dao.mapper.RequestConfigInfoMapper;
import com.silwings.db.mysql.enums.EnableStatus;
import com.silwings.db.mysql.enums.LogicDelete;
import com.silwings.db.mysql.service.RequestConfigInfoService;
import com.silwings.responder.core.config.RequestConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RequestConfigInfoServiceImpl
 * @Description 请求配置service
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
@Service
public class RequestConfigInfoServiceImpl implements RequestConfigInfoService {

    @Autowired
    private RequestConfigInfoMapper requestConfigInfoMapper;

    @Override
    public List<RequestConfigInfo> queryByKeyUrl(final String url) {

        if (StringUtils.isBlank(url)) {
            return Collections.emptyList();
        }

        final RequestConfigInfoEntity infoEntity = new RequestConfigInfoEntity();
        infoEntity
                .setKeyUrl(url)
                .setEnableStatus(EnableStatus.ENABLE.code())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<RequestConfigInfoEntity> selectInfos = this.requestConfigInfoMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }


    @Override
    public List<RequestConfigInfo> queryRestConfigByMethod(final RequestMethod requestMethod) {
        if (null == requestMethod) {
            return Collections.emptyList();
        }

        final RequestConfigInfoEntity infoEntity = new RequestConfigInfoEntity();
        infoEntity
                .setRequestMethod(requestMethod.toString())
                .setEnableStatus(EnableStatus.ENABLE.code())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<RequestConfigInfoEntity> selectInfos = this.requestConfigInfoMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }


    private List<RequestConfigInfo> entity2ConfigInfo(final List<RequestConfigInfoEntity> selectInfos) {
        return selectInfos.stream()
                .map(RequestConfigInfoEntity::getDataJson)
                .filter(StringUtils::isNotBlank)
                .map(dataJson -> JSON.parseObject(dataJson, RequestConfigInfo.class))
                .collect(Collectors.toList());
    }

}
