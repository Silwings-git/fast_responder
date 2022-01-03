package com.silwings.db.db_editor.service.impl;

import com.alibaba.fastjson.JSON;
import com.silwings.db.db_editor.bean.param.InsertOrUpdateConfigParam;
import com.silwings.db.db_editor.service.RequestConfigService;
import com.silwings.responder.commons.exception.ResponderException;
import com.silwings.responder.core.bean.RequestConfigInfo;
import com.silwings.responder.core.bean.ResponderCallBack;
import com.silwings.responder.core.bean.ResponderLogic;
import com.silwings.responder.core.bean.ResponderParam;
import com.silwings.responder.core.bean.ResponderResult;
import com.silwings.db.db_editor.bean.dto.QueryReqConfigDto;
import com.silwings.db.db_editor.bean.dto.VirtualServiceConfigDto;
import com.silwings.db.db_editor.bean.vo.PageData;
import com.silwings.db.db_editor.dao.entity.VirtualServiceConfigEntity;
import com.silwings.db.db_editor.dao.mapper.VirtualServiceConfigMapper;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.responder.utils.ConvertUtils;
import com.silwings.responder.utils.PageHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName RequestConfigServiceImpl
 * @Description RequestConfigServiceImpl
 * @Author Silwings
 * @Date 2021/8/6 23:12
 * @Version V1.0
 **/
@Service
public class RequestConfigServiceImpl implements RequestConfigService {

    private VirtualServiceConfigMapper virtualServiceConfigMapper;

    public RequestConfigServiceImpl(VirtualServiceConfigMapper virtualServiceConfigMapper) {
        this.virtualServiceConfigMapper = virtualServiceConfigMapper;
    }

    @Override
    public RequestConfigInfo findConfigByUrl(final String lookupPath) {

        final VirtualServiceConfigEntity virtualServiceConfigEntity =
                this.virtualServiceConfigMapper.selectOne(new VirtualServiceConfigEntity().setKeyUrl(lookupPath).setLogicDelete(0));

        return null == virtualServiceConfigEntity ? null : JSON.parseObject(virtualServiceConfigEntity.getReqConfigInfo(), RequestConfigInfo.class);
    }

    @Override
    public PageData<VirtualServiceConfigDto> queryReqConfigPage(final QueryReqConfigDto queryParam) {

        final Example example = new Example(VirtualServiceConfigEntity.class, true, false);
        example.createCriteria()
                .andEqualTo(VirtualServiceConfigEntity.C_KEY_URL, ConvertUtils.toString(queryParam.getKeyUrl()))
                .andEqualTo(VirtualServiceConfigEntity.C_LOGIC_DELETE, 0);

        final int count = this.virtualServiceConfigMapper.selectCountByCondition(example);

        final List<VirtualServiceConfigEntity> configEntityList =
                this.virtualServiceConfigMapper.selectByExampleAndRowBounds(example, PageHelper.buildRowBounds(queryParam));

        final List<VirtualServiceConfigDto> virtualServiceConfigDtoList = BeanCopyUtils.jsonCopyList(configEntityList, VirtualServiceConfigDto.class);

        return new PageData<>(virtualServiceConfigDtoList, (long) count);
    }

    @Override
    public String insertOrUpdateConfig(final InsertOrUpdateConfigParam param) {
        if (null == param) {
            throw new ResponderException("configInfo 参数为空");
        }

        if (StringUtils.isBlank(param.getKeyUrl())) {
            throw new ResponderException("keyUrl 不可为空");
        }

        final VirtualServiceConfigEntity virtualServiceConfigEntity = new VirtualServiceConfigEntity();
        virtualServiceConfigEntity
                .setKeyUrl(param.getKeyUrl())
                .setReqConfigInfo(JSON.toJSONString(param));

        final RequestConfigInfo configByUrl = this.findConfigByUrl(param.getKeyUrl());
        if (null == configByUrl) {
            this.virtualServiceConfigMapper.insertSelective(virtualServiceConfigEntity);
        } else {
            final Example example = new Example(VirtualServiceConfigEntity.class, true, false);
            example.createCriteria()
                    .andEqualTo(VirtualServiceConfigEntity.C_KEY_URL, param.getKeyUrl());

            this.virtualServiceConfigMapper.updateByConditionSelective(virtualServiceConfigEntity, example);
        }

        return param.getKeyUrl();
    }

    @Override
    public String delReqConfigByKeyUrl(String keyUrl) {

        if (StringUtils.isBlank(keyUrl)) {
            return "";
        }

        final Example example = new Example(VirtualServiceConfigEntity.class, true, false);
        example.createCriteria()
                .andEqualTo(VirtualServiceConfigEntity.C_KEY_URL, ConvertUtils.toString(keyUrl, ""))
                .andEqualTo(VirtualServiceConfigEntity.C_LOGIC_DELETE, 0);

        final int updateRow = this.virtualServiceConfigMapper.updateByConditionSelective(new VirtualServiceConfigEntity().setLogicDelete(0), example);

        return updateRow > 0 ? keyUrl : "";
    }

    @Data
    public static class User {
        private String name;

        public User(String name) {
            this.name = name;
        }
    }

    private RequestConfigInfo getRequestConfigInfo() {

        final HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("name", "Silwings");
        nameMap.put("age", "100");


        final ResponderParam<? super Object, ? super Object> responderParam = new ResponderParam<>();
        responderParam.put("name", nameMap);

        final ResponderResult responderResult = new ResponderResult();
        responderResult.put("result1", "r1");
        responderResult.put("result2", "r2");
        responderResult.put("def", new User("xiaom"));

        final ResponderLogic responderLogic = new ResponderLogic();
//        responderLogic.setLogic("name != whh throw 这个不能错\nreqname == 123 return result2\r\n");
//        responderLogic.setLogic("name != whh throw 这个不能错");
        responderLogic.setLogic("reqname == 123 return result2\r\n");

        final ResponderCallBack responderCallBack = new ResponderCallBack();
        responderCallBack.setCallbackUrl("http://127.0.0.1:8899/demo/aaaa");
        final HashMap<String, Object> stringStringHashMap = new HashMap<>();
        final HashMap<String, Object> map = new HashMap<>();

        map.put("abc", "lzl");
        final HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", "${reqname}");
        map.put("efg", map1);

        stringStringHashMap.put("whh", map);
        responderCallBack.setBody(stringStringHashMap);
        responderCallBack.setMethod("POST");


//        return new RequestConfigInfo("/silwings", responderParam, responderResult, responderLogic, responderCallBack);
        return null;
    }

}
