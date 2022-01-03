package com.silwings.responder.core.chain;


/**
 * @ClassName ResponderMappingInfo
 * @Description 请求映射信息
 * @Author Silwings
 * @Date 2021/8/6 18:55
 * @Version V1.0
 **/
public class ResponderMappingInfo {

    /**
     * 处理方法的处理类型
     */
    public ResponderMappingInfo getMatchingCondition(final ResponderBody responderBody) {
//        if (null != responderBody && this.getType().equals(responderBody.getType())) {
//            return new ResponderMappingInfo(responderBody.getType());
//        }
        return null;
    }

    // 两个匹配结果比较，只根据匹配内容比较
    public int compareTo(final ResponderMappingInfo info, final ResponderBody body) {
//        return info.getType().equals(body.getType()) ? 1 : 0;
        return 1;
    }

    protected Object getType() {
        return null;
    }
}
