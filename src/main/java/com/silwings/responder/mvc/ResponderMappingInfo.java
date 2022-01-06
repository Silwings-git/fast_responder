package com.silwings.responder.mvc;


import com.silwings.responder.core.ResponderContext;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ResponderMappingInfo
 * @Description 请求映射信息
 * @Author Silwings
 * @Date 2021/8/6 18:55
 * @Version V1.0
 **/
@Setter
@Getter
public class ResponderMappingInfo {

    private ResponderContext responderContext;

    public ResponderMappingInfo() {
    }

    public ResponderMappingInfo(ResponderContext responderContext) {
        this.responderContext = responderContext;
    }

    /**
     * 处理方法的处理类型
     */
    public ResponderMappingInfo getMatchingCondition(final ResponderContext responderContext) {
        return new ResponderMappingInfo(responderContext);
    }

    /**
     * description: 比较两个ResponderMappingInfo优先级
     * 在当前环境中,只有一个处理方法对ResponderBody进行处理,所有该compareTo可以直接返回固定值1
     * version: 1.0
     * date: 2022/1/3 22:29
     * author: Silwings
     */
    public int compareTo(final ResponderMappingInfo info, final ResponderContext body) {
        return 1;
    }

}
