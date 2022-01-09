package com.silwings.responder.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @ClassName ResponderEventPack
 * @Description ResponderEventPack
 * @Author Silwings
 * @Date 2022/1/9 13:09
 * @Version V1.0
 **/
@Getter
public class ResponderEventPack<T> {

    private ResponderEventType event;

    private EventData<T> data;

    private long timeStamp;

    public ResponderEventPack(@NotNull ResponderEventType event, @NotNull EventData<T> data) {
        this.event = event;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
    }

    public static  <T> EventData<T> simpleEventData(final T t) {
        return new EventData<>(t);
    }

    // 用于扩展
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventData<T>{
        private T data;
    }
}
