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
import com.silwings.web.mysql.dao.ResponderInfoConfigEntity;
import com.silwings.web.mysql.dao.mapper.ResponderInfoConfigMapper;
import com.silwings.web.mysql.dto.ResponderInfoConfigDto;
import com.silwings.web.mysql.service.ResponderInfoConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ResponderInfoConfigServiceImpl
 * @Description 请求配置service
 * @Author Silwings
 * @Date 2022/1/8 15:07
 * @Version V1.0
 **/
@Slf4j
@Service
public class ResponderInfoConfigServiceImpl implements ResponderInfoConfigService {

    @Autowired
    private ResponderInfoConfigMapper responderInfoConfigMapper;

    @Override
    public List<ResponderInfo> queryByKeyUrl(final String url) {

        if (StringUtils.isBlank(url)) {
            return Collections.emptyList();
        }

        final ResponderInfoConfigEntity infoEntity = new ResponderInfoConfigEntity();
        infoEntity
                .setKeyUrl(url)
                .setEnableStatus(EnableStatus.ENABLE.value())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<ResponderInfoConfigEntity> selectInfos = this.responderInfoConfigMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }


    @Override
    public List<ResponderInfo> queryRestConfigByMethod(final HttpMethod httpMethod) {
        if (null == httpMethod) {
            return Collections.emptyList();
        }

        final ResponderInfoConfigEntity infoEntity = new ResponderInfoConfigEntity();
        infoEntity
                .setHttpMethod(httpMethod.toString())
                .setEnableStatus(EnableStatus.ENABLE.value())
                .setLogicDelete(LogicDelete.NORMAL.number());

        final List<ResponderInfoConfigEntity> selectInfos = this.responderInfoConfigMapper.select(infoEntity);

        return this.entity2ConfigInfo(selectInfos);
    }

    private List<ResponderInfo> entity2ConfigInfo(final List<ResponderInfoConfigEntity> selectInfos) {
        return selectInfos.stream()
                .map(ResponderInfoConfigEntity::getDataJson)
                .filter(StringUtils::isNotBlank)
                .map(dataJson -> JSON.parseObject(dataJson, ResponderInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long insert(final ResponderInfoConfigDto insertInfo) {

        if (null == insertInfo) {
            return -1L;
        }

        final ResponderInfoConfigEntity countCondition = new ResponderInfoConfigEntity()
                .setKeyUrl(insertInfo.getKeyUrl())
                .setHttpMethod(insertInfo.getHttpMethod())
                .setLogicDelete(LogicDelete.NORMAL.number());
        final int count = this.responderInfoConfigMapper.selectCount(countCondition);
        if (count > 0) {
            // 数据库是逻辑删，不方便使用唯一键
            throw new DbException("应答器地址和请求方式的组合重复,请检查并调整后重试");
        }

        final ResponderInfoConfigEntity entity = BeanCopyUtils.jsonCopyBean(insertInfo, ResponderInfoConfigEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        this.responderInfoConfigMapper.insertSelective(entity);

        return entity.getId();
    }

    @Override
    public void updateById(final ResponderInfoConfigDto updateInfo) {

        if (null == updateInfo || null == updateInfo.getId()) {
            log.error("更新请求配置信息失败. 参数: {}", null == updateInfo ? "参数为空" : JSON.toJSONString(updateInfo));
            throw new DbException("更新失败");
        }

        final ResponderInfoConfigEntity entity = BeanCopyUtils.jsonCopyBean(updateInfo, ResponderInfoConfigEntity.class);
        entity.setId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(null);

        final Example example = new Example(ResponderInfoConfigEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoConfigEntity.C_ID, ConvertUtils.toObj(updateInfo.getId(), -1L));

        try {
            this.responderInfoConfigMapper.updateByConditionSelective(entity, example);
        } catch (DuplicateKeyException e) {
            throw new DbException("应答器地址和请求方式的组合重复,请检查并调整后重试");
        }
    }

    @Override
    public PageData<ResponderInfoConfigDto> query(final ResponderInfoConfigDto queryInfo) {

        if (null == queryInfo) {
            return PageData.empty();
        }

        final String queryCategoryName = StringUtils.isBlank(queryInfo.getCategoryName()) ? null : "%" + queryInfo.getCategoryName() + "%";
        final String queryName = StringUtils.isBlank(queryInfo.getName()) ? null : "%" + queryInfo.getName() + "%";
        final String queryKeyUrl = StringUtils.isBlank(queryInfo.getKeyUrl()) ? null : "%" + queryInfo.getKeyUrl() + "%";

        final Example example = new Example(ResponderInfoConfigEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoConfigEntity.C_ID, ConvertUtils.toObj(queryInfo.getId()))
                .andLike(ResponderInfoConfigEntity.C_CATEGORY_NAME, queryCategoryName)
                .andLike(ResponderInfoConfigEntity.C_NAME, queryName)
                .andLike(ResponderInfoConfigEntity.C_KEY_URL, queryKeyUrl)
                .andEqualTo(ResponderInfoConfigEntity.C_HTTP_METHOD, ConvertUtils.toString(queryInfo.getHttpMethod()))
                .andEqualTo(ResponderInfoConfigEntity.C_ENABLE_STATUS, ConvertUtils.toObj(queryInfo.getEnableStatus()))
                .andEqualTo(ResponderInfoConfigEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        example.orderBy(ResponderInfoConfigEntity.C_UPDATE_TIME).desc();

        final int count = this.responderInfoConfigMapper.selectCountByCondition(example);
        if (0 == count) {
            return PageData.empty();
        }

        final List<ResponderInfoConfigEntity> entityList = this.responderInfoConfigMapper.selectByExampleAndRowBounds(example, RowBoundsUtils.build(queryInfo));

        final List<ResponderInfoConfigDto> dtoList = BeanCopyUtils.jsonCopyList(entityList, ResponderInfoConfigDto.class);

        return new PageData<>(dtoList, (long) count);
    }

    @Override
    public ResponderInfoConfigDto findById(final Long id) {

        if (null == id) {
            return null;
        }

        final ResponderInfoConfigEntity entity = this.responderInfoConfigMapper.selectByPrimaryKey(id);

        return BeanCopyUtils.jsonCopyBean(entity, ResponderInfoConfigDto.class);
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

        final ResponderInfoConfigDto configInfo = this.findById(id);

        if (null == configInfo) {
            throw new DbException("配置信息不存在");
        }

        final ResponderInfoUtils.CheckResult checkResult = ResponderInfoUtils.checkResponderInfo(configInfo.getDataJson());
        if (!checkResult.isWhole()) {
            throw new DbException("配置信息检查不通过. " + checkResult.getMsg());
        }

        final ResponderInfoConfigDto enableParam = new ResponderInfoConfigDto();
        enableParam
                .setId(id)
                .setEnableStatus(EnableStatus.ENABLE.value());

        this.updateById(enableParam);
    }

    private void disableConfig(final Long id) {

        final ResponderInfoConfigDto enableParam = new ResponderInfoConfigDto();
        enableParam
                .setId(id)
                .setEnableStatus(EnableStatus.DISABLED.value());

        this.updateById(enableParam);
    }

    @Override
    public void deleteById(final Long id) {

        if (null == id) {
            return;
        }

        final Example example = new Example(ResponderInfoConfigEntity.class);
        example.createCriteria()
                .andEqualTo(ResponderInfoConfigEntity.C_ID, ConvertUtils.toObj(id, -1L))
                .andEqualTo(ResponderInfoConfigEntity.C_LOGIC_DELETE, LogicDelete.NORMAL.number());

        final ResponderInfoConfigEntity delete = new ResponderInfoConfigEntity();
        delete.setLogicDelete(LogicDelete.DELETED.number());

        this.responderInfoConfigMapper.updateByConditionSelective(delete, example);
    }

}
