package com.silwings.responder.core.chain;


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

    private ResponderBody responderBody;

    public ResponderMappingInfo() {
    }

    public ResponderMappingInfo(ResponderBody responderBody) {
        this.responderBody = responderBody;
    }

    /**
     * 处理方法的处理类型
     */
    public ResponderMappingInfo getMatchingCondition(final ResponderBody responderBody) {
        return new ResponderMappingInfo(responderBody);
    }

    /**
     * description: 比较两个ResponderMappingInfo优先级
     * 在当前环境中,ResponderMappingInfo始终取实际请求时创建的,所有固定返回1即可
     * version: 1.0
     * date: 2022/1/3 22:29
     * author: Silwings
     */
    public int compareTo(final ResponderMappingInfo info, final ResponderBody body) {
        return 1;
    }

}
