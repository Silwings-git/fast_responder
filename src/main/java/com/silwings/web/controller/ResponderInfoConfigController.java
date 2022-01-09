package com.silwings.web.controller;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.web.bean.param.EnableConfigParam;
import com.silwings.web.bean.param.QueryResponderInfoParam;
import com.silwings.web.bean.param.SaveResponderInfoParam;
import com.silwings.web.bean.result.FindResponderInfoDetailResult;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.bean.result.QueryResponderInfoResult;
import com.silwings.web.bean.result.ResponderInfoResult;
import com.silwings.web.bean.result.ResponderPageResult;
import com.silwings.web.bean.result.ResponderResult;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.execption.DbException;
import com.silwings.web.mysql.dto.ResponderInfoDto;
import com.silwings.web.mysql.service.ResponderInfoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ResponderInfoConfigController
 * @Description 请求配置信息web接口
 * @Author Silwings
 * @Date 2022/1/8 15:26
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/responder/request/crud")
public class ResponderInfoConfigController {

    @Autowired
    private ResponderInfoInfoService responderInfoInfoService;

    @PostMapping("/insert")
    public ResponderResult<Void> insert(@RequestBody SaveResponderInfoParam param) {

        log.info("/responder/request/crud/insert param: {}", JSON.toJSONString(param));

        final ResponderInfoDto insertInfo = new ResponderInfoDto();
        insertInfo
                .setName(param.getName())
                .setCategoryName(param.getCategoryName())
                .setKeyUrl(param.getKeyUrl())
                .setHttpMethod(param.getHttpMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.responderInfoInfoService.insert(insertInfo);

        return ResponderResult.ok();
    }


    @GetMapping("/query")
    public ResponderPageResult<QueryResponderInfoResult> query(@RequestBody QueryResponderInfoParam param) {

        log.info("/responder/request/crud/query param: {}", JSON.toJSONString(param));

        final ResponderInfoDto queryInfo = BeanCopyUtils.jsonCopyBean(param, ResponderInfoDto.class);

        final PageData<ResponderInfoDto> pageData = this.responderInfoInfoService.query(queryInfo);

        final List<QueryResponderInfoResult> infoResultList = BeanCopyUtils.jsonCopyList(pageData.getList(), QueryResponderInfoResult.class);

        return ResponderPageResult.ok(infoResultList, pageData.getTotal());

    }

    @GetMapping("/find/{id}")
    public ResponderResult<FindResponderInfoDetailResult> find(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/find/{id} id: {}", id);

        final ResponderInfoDto infoDto = this.responderInfoInfoService.findById(id);

        if (null == infoDto) {
            throw new DbException("配置信息不存在");
        }

        final FindResponderInfoDetailResult detailResult = new FindResponderInfoDetailResult();
        detailResult.setId(infoDto.getId());
        detailResult.setConfigInfo(JSON.parseObject(infoDto.getDataJson(), ResponderInfoResult.class));

        return ResponderResult.ok(detailResult);

    }

    @PutMapping("/update/{id}")
    public ResponderResult<Void> update(@PathVariable("id") Long id, @RequestBody SaveResponderInfoParam param) {

        log.info("/responder/request/crud/updateById/{id} id: {}  param: {}", id, JSON.toJSONString(param));

        final ResponderInfoDto updateInfo = new ResponderInfoDto();
        updateInfo
                .setId(id)
                .setName(param.getName())
                .setCategoryName(param.getCategoryName())
                .setKeyUrl(param.getKeyUrl())
                .setHttpMethod(param.getHttpMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.responderInfoInfoService.updateById(updateInfo);

        return ResponderResult.ok();

    }

    @PutMapping("/enableConfig")
    public ResponderResult<Void> enableConfig(@RequestBody EnableConfigParam param) {

        log.info("/responder/request/crud/enableConfig/{id} param: {}", JSON.toJSONString(param));

        this.responderInfoInfoService.enableConfig(param.getId(), EnableStatus.valueOfCode(param.getEnableStatus()));

        return ResponderResult.ok();

    }

    @DeleteMapping("/delete/{id}")
    public ResponderResult<Void> delete(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/delete/{id} id: {}", id);

        this.responderInfoInfoService.deleteById(id);

        return ResponderResult.ok();

    }

}
