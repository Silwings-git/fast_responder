package com.silwings.responder.event;

import com.silwings.responder.interfaces.ResponderEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ResponderEventManager
 * @Description ResponderEventManager
 * @Author Silwings
 * @Date 2022/1/9 13:02
 * @Version V1.0
 **/
@Slf4j
public class ResponderEventManager implements ApplicationContextAware {

    private EnumMap<ResponderEventType, List<ResponderEventListener>> eventListeners = new EnumMap<>(ResponderEventType.class);

    @Async
    public void notify(final ResponderEventPack<?> responderEventPack) {
        if (null == responderEventPack || null == responderEventPack.getEvent() || null == responderEventPack.getData()) {
            return;
        }

        this.eventListeners.get(responderEventPack.getEvent())
                .forEach(listener -> {
                    try {
                        listener.doEvent(responderEventPack);
                    } catch (Exception e) {
                        log.error("ResponderEventManager#notify error.", e);
                    }
                });
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {

        final Map<String, ResponderEventListener> beansOfType = applicationContext.getBeansOfType(ResponderEventListener.class);

        for (ResponderEventListener eventListener : beansOfType.values()) {

            final List<ResponderEventListener> responderEventListenerList = this.eventListeners.computeIfAbsent(eventListener.getEventType(), k -> new ArrayList<>());

            responderEventListenerList.add(eventListener);
        }
    }
}
