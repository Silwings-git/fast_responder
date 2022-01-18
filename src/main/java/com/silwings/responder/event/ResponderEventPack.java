package com.silwings.responder.event;

import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * @ClassName ResponderEventPack
 * @Description ResponderEventPack
 * @Author Silwings
 * @Date 2022/1/9 13:09
 * @Version V1.0
 **/
@Getter
public class ResponderEventPack {

    private ResponderEventType event;

    private String msg;

    private long timeStamp;

    public ResponderEventPack(@NotNull ResponderEventType event, final String msg) {
        this.event = event;
        this.msg = msg;
        this.timeStamp = System.currentTimeMillis();
    }

}
