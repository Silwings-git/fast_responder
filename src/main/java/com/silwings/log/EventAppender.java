package com.silwings.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.silwings.responder.event.ResponderEventManager;
import com.silwings.responder.event.ResponderEventPack;
import com.silwings.responder.event.ResponderEventType;
import com.silwings.responder.utils.SpringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName EventAppender
 * @Description 将日志作为事件输出
 * @Author Silwings
 * @Date 2021/1/9 16:04
 * @Version V1.0
 **/
@Getter
@Setter
public class EventAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /**
     * 格式
     */
    private Layout<ILoggingEvent> layout;

    @Override
    public void append(final ILoggingEvent event) {
        if (event == null || !isStarted()) {
            return;
        }

        final ResponderEventManager eventManager = SpringUtils.getBean(ResponderEventManager.class);
        if (null != eventManager) {
            eventManager.notify(new ResponderEventPack<>(ResponderEventType.PROJECT_LOG, ResponderEventPack.simpleEventData(this.layout.doLayout(event))));
        }
    }
}