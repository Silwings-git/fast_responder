package com.silwings.db.controller;

import com.alibaba.fastjson.JSON;
import com.silwings.db.bean.param.EnableConfigParam;
import com.silwings.db.bean.param.QueryRequestConfigInfoParam;
import com.silwings.db.bean.param.SaveRequestConfigInfoParam;
import com.silwings.db.bean.result.FindRequestConfigInfoDetailResult;
import com.silwings.db.bean.result.PageData;
import com.silwings.db.bean.result.QueryRequestConfigInfoResult;
import com.silwings.db.bean.result.RequestConfigInfoResult;
import com.silwings.db.bean.result.ResponderPageResult;
import com.silwings.db.bean.result.ResponderResult;
import com.silwings.db.enums.EnableStatus;
import com.silwings.db.execption.DbException;
import com.silwings.db.mysql.dto.RequestConfigInfoDto;
import com.silwings.db.mysql.service.RequestConfigInfoService;
import com.silwings.responder.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @ClassName RequestConfigInfoController
 * @Description 请求配置信息web接口
 * @Author Silwings
 * @Date 2022/1/8 15:26
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/responder/request/crud")
public class RequestConfigInfoController {

    @Autowired
    private RequestConfigInfoService requestConfigInfoService;

    @PostMapping("/insert")
    public ResponderResult<Void> insert(@RequestBody SaveRequestConfigInfoParam param) {

        log.info("/responder/request/crud/insert param: {}", JSON.toJSONString(param));

        final RequestConfigInfoDto insertInfo = new RequestConfigInfoDto();
        insertInfo
                .setKeyUrl(param.getKeyUrl())
                .setRequestMethod(param.getRequestMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.requestConfigInfoService.insert(insertInfo);

        return ResponderResult.ok();
    }


    @GetMapping("/query")
    public ResponderPageResult<QueryRequestConfigInfoResult> query(@RequestBody QueryRequestConfigInfoParam param) {

        log.info("/responder/request/crud/query param: {}", JSON.toJSONString(param));

        final RequestConfigInfoDto queryInfo = BeanCopyUtils.jsonCopyBean(param, RequestConfigInfoDto.class);

        final PageData<RequestConfigInfoDto> pageData = this.requestConfigInfoService.query(queryInfo);

        final List<QueryRequestConfigInfoResult> infoResultList = BeanCopyUtils.jsonCopyList(pageData.getList(), QueryRequestConfigInfoResult.class);

        return ResponderPageResult.ok(infoResultList, pageData.getTotal());

    }

    @GetMapping("/find/{id}")
    public ResponderResult<FindRequestConfigInfoDetailResult> find(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/find/{id} id: {}", id);

        final RequestConfigInfoDto infoDto = this.requestConfigInfoService.findById(id);

        if (null == infoDto) {
            throw new DbException("配置信息不存在");
        }

        final FindRequestConfigInfoDetailResult detailResult = new FindRequestConfigInfoDetailResult();
        detailResult.setId(infoDto.getId());
        detailResult.setConfigInfo(JSON.parseObject(infoDto.getDataJson(), RequestConfigInfoResult.class));

        return ResponderResult.ok(detailResult);

    }

    @PutMapping("/update/{id}")
    public ResponderResult<Void> update(@PathVariable("id") Long id, @RequestBody SaveRequestConfigInfoParam param) {

        log.info("/responder/request/crud/updateById/{id} id: {}  param: {}", id, JSON.toJSONString(param));

        final RequestConfigInfoDto updateInfo = new RequestConfigInfoDto();
        updateInfo
                .setId(id)
                .setKeyUrl(param.getKeyUrl())
                .setRequestMethod(param.getRequestMethod().toString())
                .setDataJson(JSON.toJSONString(param))
                .setEnableStatus(EnableStatus.DISABLED.number());

        this.requestConfigInfoService.updateById(updateInfo);

        return ResponderResult.ok();

    }

    @PutMapping("/enableConfig")
    public ResponderResult<Void> enableConfig(@RequestBody EnableConfigParam param) {

        log.info("/responder/request/crud/enableConfig/{id} param: {}", JSON.toJSONString(param));

        this.requestConfigInfoService.enableConfig(param.getId(), EnableStatus.valueOfCode(param.getEnableStatus()));

        return ResponderResult.ok();

    }

    @DeleteMapping("/delete/{id}")
    public ResponderResult<Void> delete(@PathVariable("id") Long id) {

        log.info("/responder/request/crud/delete/{id} id: {}", id);

        this.requestConfigInfoService.deleteById(id);

        return ResponderResult.ok();

    }

}
