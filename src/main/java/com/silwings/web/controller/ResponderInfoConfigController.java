package com.silwings.web.controller;

import com.alibaba.fastjson.JSON;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.web.bean.param.EnableResponderInfoParam;
import com.silwings.web.bean.param.QueryResponderInfoConfigParam;
import com.silwings.web.bean.param.SaveResponderInfoConfigParam;
import com.silwings.web.bean.result.FindResponderInfoConfigDetailResult;
import com.silwings.web.bean.result.PageData;
import com.silwings.web.bean.result.PageResult;
import com.silwings.web.bean.result.QueryResponderInfoConfigResult;
import com.silwings.web.bean.result.ResponderInfoConfigResult;
import com.silwings.web.bean.result.WebResult;
import com.silwings.web.enums.EnableStatus;
import com.silwings.web.execption.DbException;
import com.silwings.web.mysql.dto.ResponderInfoConfigDto;
import com.silwings.web.mysql.service.ResponderInfoConfigService;
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
    private ResponderInfoConfigService responderInfoConfigService;

    @PostMapping("/insert")
    public WebResult<Void> insert(@RequestBody SaveResponderInfoConfigParam param) {

        log.info("/responder/request/crud/insert param: {}", JSON.toJSONString(param));

        final ResponderInfoConfigDto insertInfo = new ResponderInfoConfigDto();
        insertInfo
                .setName(param.getName())
                .setCategoryName(param.getCategoryName())
                .setKeyUrl(param.getKeyUrl())
                .setHttpMethod(param.getHttpMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.responderInfoConfigService.insert(insertInfo);

        return WebResult.ok();
    }


    @PostMapping("/query")
    public PageResult<QueryResponderInfoConfigResult> query(@RequestBody QueryResponderInfoConfigParam param) {

        log.info("/responder/request/crud/query param: {}", JSON.toJSONString(param));

        final ResponderInfoConfigDto queryInfo = BeanCopyUtils.jsonCopyBean(param, ResponderInfoConfigDto.class);

        final PageData<ResponderInfoConfigDto> pageData = this.responderInfoConfigService.query(queryInfo);

        final List<QueryResponderInfoConfigResult> infoResultList = BeanCopyUtils.jsonCopyList(pageData.getList(), QueryResponderInfoConfigResult.class);

        return PageResult.ok(infoResultList, pageData.getTotal());

    }

    @GetMapping("/find/{id}")
    public WebResult<FindResponderInfoConfigDetailResult> find(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/find/{id} id: {}", id);

        final ResponderInfoConfigDto infoDto = this.responderInfoConfigService.findById(id);

        if (null == infoDto) {
            throw new DbException("配置信息不存在");
        }

        final FindResponderInfoConfigDetailResult detailResult = new FindResponderInfoConfigDetailResult();
        detailResult.setId(infoDto.getId());
        detailResult.setConfigInfo(JSON.parseObject(infoDto.getDataJson(), ResponderInfoConfigResult.class));

        return WebResult.ok(detailResult);

    }

    @PutMapping("/update/{id}")
    public WebResult<Void> update(@PathVariable("id") Long id, @RequestBody SaveResponderInfoConfigParam param) {

        log.info("/responder/request/crud/updateById/{id} id: {}  param: {}", id, JSON.toJSONString(param));

        final ResponderInfoConfigDto updateInfo = new ResponderInfoConfigDto();
        updateInfo
                .setId(id)
                .setName(param.getName())
                .setCategoryName(param.getCategoryName())
                .setKeyUrl(param.getKeyUrl())
                .setHttpMethod(param.getHttpMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.responderInfoConfigService.updateById(updateInfo);

        return WebResult.ok();

    }

    @PutMapping("/enableConfig")
    public WebResult<Void> enableConfig(@RequestBody EnableResponderInfoParam param) {

        log.info("/responder/request/crud/enableConfig/{id} param: {}", JSON.toJSONString(param));

        this.responderInfoConfigService.enableConfig(param.getId(), EnableStatus.valueOfCode(param.getEnableStatus()));

        return WebResult.ok();

    }

    @DeleteMapping("/delete/{id}")
    public WebResult<Void> delete(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/delete/{id} id: {}", id);

        this.responderInfoConfigService.deleteById(id);

        return WebResult.ok();

    }

}
