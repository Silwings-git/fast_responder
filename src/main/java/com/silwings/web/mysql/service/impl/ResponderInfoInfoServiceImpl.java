package com.silwings.web.mysql.service.impl;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.core.config.ResponderInfo;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.responder.utils.ConvertUtils;
import com.silwings.responder.utils.ResponderInfoUtils;
import com.silwings.responder.utils.RowBoundsUtils;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.enums.LogicDelete;
import com.silwings.web.execption.DbException;
import com.silwings.web.mysql.dao.ResponderInfoEntity;
import com.silwings.web.mysql.dao.mapper.ResponderInfoMapper;
import com.silwings.web.mysql.dto.ResponderInfoDto;
import com.silwings.web.mysql.service.ResponderInfoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ResponderInfoInfoServiceImpl
 * @Description 请求配置service
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
@Slf4j
@Service
public class ResponderInfoInfoServiceImpl implements ResponderInfoInfoService {

    @Autowired
    private ResponderInfoMapper responderInfoMapper;

    @Override
    public List<ResponderInfo> queryByKeyUrl(final String url) {

        if (StringUtils.isBlank(url)) {
            return Collections.emptyList();
        }

        final ResponderInfoEntity infoEntity = new ResponderInfoEntity();
        infoEntity
                .setKeyUrl(url)
                .setEnableStatus(EnableStatus.ENABLE.number())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<ResponderInfoEntity> selectInfos = this.responderInfoMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }


    @Override
    public List<ResponderInfo> queryRestConfigByMethod(final HttpMethod httpMethod) {
        if (null == httpMethod) {
            return Collections.emptyList();
        }

        final ResponderInfoEntity infoEntity = new ResponderInfoEntity();
        infoEntity
                .setHttpMethod(httpMethod.toString())
                .setEnableStatus(EnableStatus.ENABLE.number())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<ResponderInfoEntity> selectInfos = this.responderInfoMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }

    private List<ResponderInfo> entity2ConfigInfo(final List<ResponderInfoEntity> selectInfos) {
        return selectInfos.stream()
                .map(ResponderInfoEntity::getDataJson)
                .filter(StringUtils::isNotBlank)
                .map(dataJson -> JSON.parseObject(dataJson, ResponderInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public void insert(final ResponderInfoDto insertInfo) {

        if (null == insertInfo) {
            return;
        }

        final ResponderInfoEntity entity = BeanCopyUtils.jsonCopyBean(insertInfo, ResponderInfoEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        this.responderInfoMapper.insertSelective(entity);
    }

    @Override
    public void updateById(final ResponderInfoDto updateInfo) {

        if (null == updateInfo || null == updateInfo.getId()) {
            log.error("更新请求配置信息失败. 参数: {}", null == updateInfo ? "参数为空" : JSON.toJSONString(updateInfo));
            throw new DbException("更新失败");
        }

        final ResponderInfoEntity entity = BeanCopyUtils.jsonCopyBean(updateInfo, ResponderInfoEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        final Example example = new Example(ResponderInfoEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoEntity.C_ID, ConvertUtils.toObj(updateInfo.getId(), -1L));

        this.responderInfoMapper.updateByConditionSelective(entity, example);
    }

    @Override
    public PageData<ResponderInfoDto> query(final ResponderInfoDto queryInfo) {

        if (null == queryInfo) {
            return PageData.empty();
        }

        final Example example = new Example(ResponderInfoEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoEntity.C_ID, ConvertUtils.toObj(queryInfo.getId()))
                .andEqualTo(ResponderInfoEntity.C_CATEGORY_NAME, ConvertUtils.toObj(queryInfo.getCategoryName()))
                .andEqualTo(ResponderInfoEntity.C_NAME, ConvertUtils.toObj(queryInfo.getName()))
                .andEqualTo(ResponderInfoEntity.C_KEY_URL, ConvertUtils.toObj(queryInfo.getKeyUrl()))
                .andEqualTo(ResponderInfoEntity.C_HTTP_METHOD, ConvertUtils.toObj(queryInfo.getHttpMethod()))
                .andEqualTo(ResponderInfoEntity.C_ENABLE_STATUS, ConvertUtils.toObj(queryInfo.getEnableStatus()))
                .andEqualTo(ResponderInfoEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        final int count = this.responderInfoMapper.selectCountByCondition(example);
        if (0 == count) {
            return PageData.empty();
        }

        final List<ResponderInfoEntity> entityList = this.responderInfoMapper.selectByExampleAndRowBounds(example, RowBoundsUtils.build(queryInfo));

        final List<ResponderInfoDto> dtoList = BeanCopyUtils.jsonCopyList(entityList, ResponderInfoDto.class);

        return new PageData<>(dtoList, (long) count);
    }

    @Override
    public ResponderInfoDto findById(final Long id) {

        if (null == id) {
            return null;
        }

        final ResponderInfoEntity entity = this.responderInfoMapper.selectByPrimaryKey(id);

        return BeanCopyUtils.jsonCopyBean(entity, ResponderInfoDto.class);
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

        final ResponderInfoDto configInfo = this.findById(id);

        if (null == configInfo) {
            throw new DbException("配置信息不存在");
        }

        final ResponderInfoUtils.CheckResult checkResult = ResponderInfoUtils.checkResponderInfo(configInfo.getDataJson());
        if (!checkResult.isWhole()) {
            throw new DbException("配置信息检查不通过. " + checkResult.getMsg());
        }

        final ResponderInfoDto enableParam = new ResponderInfoDto();
        enableParam
                .setId(id)
                .setEnableStatus(EnableStatus.ENABLE.number());

        this.updateById(enableParam);
    }

    private void disableConfig(final Long id) {

        final ResponderInfoDto enableParam = new ResponderInfoDto();
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

        final Example example = new Example(ResponderInfoEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoEntity.C_ID, ConvertUtils.toObj(id, -1L))
                .andEqualTo(ResponderInfoEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        final ResponderInfoEntity delete = new ResponderInfoEntity();
        delete.setLogicDelete(LogicDelete.DELETED.number());

        this.responderInfoMapper.updateByConditionSelective(delete, example);
    }

}
