package com.silwings.db.mysql.service.impl;

import com.alibaba.fastjson.JSON;
import com.silwings.db.bean.result.PageData;
import com.silwings.db.enums.EnableStatus;
import com.silwings.db.enums.LogicDelete;
import com.silwings.db.execption.DbException;
import com.silwings.db.mysql.dao.RequestConfigInfoEntity;
import com.silwings.db.mysql.dao.mapper.RequestConfigInfoMapper;
import com.silwings.db.mysql.dto.RequestConfigInfoDto;
import com.silwings.db.mysql.service.RequestConfigInfoService;
import com.silwings.responder.core.config.RequestConfigInfo;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.responder.utils.ConvertUtils;
import com.silwings.responder.utils.RequestConfigInfos;
import com.silwings.responder.utils.RowBoundsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import tk.mybatis.mapper.entity.Example;

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
@Slf4j
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
                .setEnableStatus(EnableStatus.ENABLE.number())
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
                .setEnableStatus(EnableStatus.ENABLE.number())
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

    @Override
    public void insert(final RequestConfigInfoDto insertInfo) {

        if (null == insertInfo) {
            return;
        }

        final RequestConfigInfoEntity entity = BeanCopyUtils.jsonCopyBean(insertInfo, RequestConfigInfoEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        this.requestConfigInfoMapper.insertSelective(entity);
    }

    @Override
    public void updateById(final RequestConfigInfoDto updateInfo) {

        if (null == updateInfo || null == updateInfo.getId()) {
            log.error("更新请求配置信息失败. 参数: {}", null == updateInfo ? "参数为空" : JSON.toJSONString(updateInfo));
            throw new DbException("更新失败");
        }

        final RequestConfigInfoEntity entity = BeanCopyUtils.jsonCopyBean(updateInfo, RequestConfigInfoEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        final Example example = new Example(RequestConfigInfoEntity.class);
        example.createCriteria()
                .andEqualTo(RequestConfigInfoEntity.C_ID, ConvertUtils.toObj(updateInfo.getId(), -1L));

        this.requestConfigInfoMapper.updateByConditionSelective(entity, example);
    }

    @Override
    public PageData<RequestConfigInfoDto> query(final RequestConfigInfoDto queryInfo) {

        if (null == queryInfo) {
            return PageData.empty();
        }

        final Example example = new Example(RequestConfigInfoEntity.class);
        example.createCriteria()
                .andEqualTo(RequestConfigInfoEntity.C_ID, ConvertUtils.toObj(queryInfo.getId()))
                .andEqualTo(RequestConfigInfoEntity.C_CLASS_NAME, ConvertUtils.toObj(queryInfo.getClassName()))
                .andEqualTo(RequestConfigInfoEntity.C_NAME, ConvertUtils.toObj(queryInfo.getName()))
                .andEqualTo(RequestConfigInfoEntity.C_KEY_URL, ConvertUtils.toObj(queryInfo.getKeyUrl()))
                .andEqualTo(RequestConfigInfoEntity.C_REQUEST_METHOD, ConvertUtils.toObj(queryInfo.getRequestMethod()))
                .andEqualTo(RequestConfigInfoEntity.C_ENABLE_STATUS, ConvertUtils.toObj(queryInfo.getEnableStatus()))
                .andEqualTo(RequestConfigInfoEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        final int count = this.requestConfigInfoMapper.selectCountByCondition(example);
        if (0 == count) {
            return PageData.empty();
        }

        final List<RequestConfigInfoEntity> entityList = this.requestConfigInfoMapper.selectByExampleAndRowBounds(example, RowBoundsUtils.build(queryInfo));

        final List<RequestConfigInfoDto> dtoList = BeanCopyUtils.jsonCopyList(entityList, RequestConfigInfoDto.class);

        return new PageData<>(dtoList, (long) count);
    }

    @Override
    public RequestConfigInfoDto findById(final Long id) {

        if (null == id) {
            return null;
        }

        final RequestConfigInfoEntity entity = this.requestConfigInfoMapper.selectByPrimaryKey(id);

        return BeanCopyUtils.jsonCopyBean(entity, RequestConfigInfoDto.class);
    }

    @Override
    public void enableConfig(final Long id, final EnableStatus enableStatus) {

        if (null == id || null == enableStatus) {
            throw new DbException("启用/禁用失败");
        }

        if (EnableStatus.ENABLE.equals(enableStatus)) {
            this.enableConfig(id);
        } else {
            this.disableConfig(id);
        }
    }

    private void enableConfig(final Long id) {

        final RequestConfigInfoDto configInfo = this.findById(id);

        if (null == configInfo) {
            throw new DbException("配置信息不存在");
        }

        final RequestConfigInfos.Result result = RequestConfigInfos.checkRequestConfigInfo(configInfo.getDataJson());
        if (!result.isWhole()) {
            throw new DbException("配置信息检查不通过. " + result.getMsg());
        }

        final RequestConfigInfoDto enableParam = new RequestConfigInfoDto();
        enableParam
                .setId(id)
                .setEnableStatus(EnableStatus.ENABLE.number());

        this.updateById(enableParam);
    }

    private void disableConfig(final Long id) {

        final RequestConfigInfoDto enableParam = new RequestConfigInfoDto();
        enableParam
                .setId(id)
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.updateById(enableParam);
    }

    @Override
    public void deleteById(final Long id) {

        if (null == id) {
            return;
        }

        final Example example = new Example(RequestConfigInfoEntity.class);
        example.createCriteria()
                .andEqualTo(RequestConfigInfoEntity.C_ID, ConvertUtils.toObj(id, -1L))
                .andEqualTo(RequestConfigInfoEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        final RequestConfigInfoEntity delete = new RequestConfigInfoEntity();
        delete.setLogicDelete(LogicDelete.DELETED.number());

        this.requestConfigInfoMapper.updateByConditionSelective(delete, example);
    }

}
