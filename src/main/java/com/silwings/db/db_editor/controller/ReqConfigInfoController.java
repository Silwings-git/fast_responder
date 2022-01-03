package com.silwings.db.db_editor.controller;

import com.alibaba.fastjson.JSON;
import com.silwings.db.db_editor.bean.param.DelReqConfigParam;
import com.silwings.db.db_editor.bean.param.InsertOrUpdateConfigParam;
import com.silwings.db.db_editor.bean.param.InsertOrUpdateConfigParamV2;
import com.silwings.db.db_editor.bean.param.QueryReqConfigParam;
import com.silwings.db.db_editor.service.RequestConfigService;
import com.silwings.responder.core.bean.RequestConfigInfo;
import com.silwings.db.db_editor.bean.dto.QueryReqConfigDto;
import com.silwings.db.db_editor.bean.dto.VirtualServiceConfigDto;
import com.silwings.db.db_editor.bean.vo.PageData;
import com.silwings.db.db_editor.bean.vo.PageResult;
import com.silwings.db.db_editor.bean.vo.Result;
import com.silwings.db.db_editor.bean.vo.VirtualServiceConfigResult;
import com.silwings.responder.utils.BeanCopyUtils;
import com.silwings.responder.utils.JsonFormatUtils;
import com.silwings.responder.utils.SimpleDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.silwings.responder.utils.SimpleDateUtils.YYYYMMDD_HHMMSS;

/**
 * @ClassName ReqConfigInfoController
 * @Description 请求配置编辑
 * @Author Silwings
 * @Date 2021/8/7 22:20
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/reqConf")
public class ReqConfigInfoController {

    private RequestConfigService requestConfigService;

    public ReqConfigInfoController(RequestConfigService requestConfigService) {
        this.requestConfigService = requestConfigService;
    }

    @PostMapping("/queryReqConfigPage")
    public PageResult<VirtualServiceConfigResult> queryReqConfigPage(@RequestBody QueryReqConfigParam param) {

        final PageData<VirtualServiceConfigDto> pageData =
                this.requestConfigService.queryReqConfigPage(BeanCopyUtils.jsonCopyBean(param, QueryReqConfigDto.class));

        final List<VirtualServiceConfigResult> resultList = pageData.getList().stream()
                .map(e -> {
                    final VirtualServiceConfigResult result = new VirtualServiceConfigResult();
                    result.setKeyUrl(e.getKeyUrl());
                    result.setReqConfigInfo(JsonFormatUtils.formatJsonStretch(e.getReqConfigInfo()));
                    result.setUpdateTime(SimpleDateUtils.format(e.getUpdateTime(), YYYYMMDD_HHMMSS));
                    result.setCreateTime(SimpleDateUtils.format(e.getCreateTime(), YYYYMMDD_HHMMSS));
                    return result;
                })
                .collect(Collectors.toList());

        return PageResult.ok(resultList, pageData.getTotal());
    }


    @PostMapping("/insertOrUpdateConfig")
    public Result<String> insertOrUpdateConfig(@RequestBody InsertOrUpdateConfigParam param) {

        final String urlKey = this.requestConfigService.insertOrUpdateConfig(param);

        return Result.ok(urlKey);
    }

    @PostMapping("/insertOrUpdateConfigV2")
    public Result<String> insertOrUpdateConfigV2(@RequestBody InsertOrUpdateConfigParamV2 paramV2) {

        if (StringUtils.isBlank(paramV2.getConfStr())) {
            return Result.failed("没有提交任何内容");
        }

        final InsertOrUpdateConfigParam insertOrUpdateConfigParam =
                JSON.parseObject(paramV2.getConfStr(), InsertOrUpdateConfigParam.class);

        final String urlKey = this.requestConfigService.insertOrUpdateConfig(insertOrUpdateConfigParam);

        return Result.ok(urlKey);
    }

    @PostMapping("/delReqConfig")
    public Result<String> delReqConfig(@RequestBody DelReqConfigParam param) {

        final String urlKey = this.requestConfigService.delReqConfigByKeyUrl(param.getKeyUrl());

        return Result.ok(urlKey);
    }

    public static void main(String[] args) {
        String str = getStr();
        final RequestConfigInfo requestConfigInfo = JSON.parseObject(str, RequestConfigInfo.class);
        System.out.println("requestConfigInfo = " + requestConfigInfo);
    }

    private static String getStr() {
        return "{\n" +
                "    \"keyUrl\": \"/silwings\",\n" +
                "    \"param\": {\n" +
                "        \"name\": {\n" +
                "            \"name\": \"Silwings\",\n" +
                "            \"age\": \"100\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"logic\": {\n" +
                "        \"logic\": \"reqname == 123 return result2\"\n" +
                "    },\n" +
                "    \"callBack\": {\n" +
                "        \"body\": {\n" +
                "            \"whh\": {\n" +
                "                \"abc\": \"lzl\",\n" +
                "                \"efg\": {\n" +
                "                    \"name\": \"${reqname}\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"callbackUrl\": \"http://127.0.0.1:8899/demo/aaaa\",\n" +
                "        \"method\": \"POST\",\n" +
                "        \"param\": \"\"\n" +
                "    },\n" +
                "    \"result\": {\n" +
                "        \"def\": {\n" +
                "            \"name\": \"xiaom\"\n" +
                "        },\n" +
                "        \"result2\": \"r2\",\n" +
                "        \"result1\": \"r1\"\n" +
                "    },\n" +
                "    \"type\": \"BASE\"\n" +
                "}";
    }

}
