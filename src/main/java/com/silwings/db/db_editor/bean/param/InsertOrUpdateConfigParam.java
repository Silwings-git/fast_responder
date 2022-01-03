package com.silwings.db.db_editor.bean.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName DelReqConfigParam
 * @Description InsertOrUpdateConfigParam
 * @Author Silwings
 * @Date 2021/8/8 12:42
 * @Version V1.0
 **/
@Setter
@Getter
public class InsertOrUpdateConfigParam {

    private String keyUrl;

    private Map<? super Object, ? super Object> param;

    private Map<? super Object, ? super Object> logic;

    private Map<? super Object, ? super Object> callBack;

    private Map<? super Object, ? super Object> result;


}
