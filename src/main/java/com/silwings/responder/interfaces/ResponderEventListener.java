package com.silwings.responder.interfaces;

import com.silwings.responder.event.ResponderEventPack;
import com.silwings.responder.event.ResponderEventType;

/**
 * @ClassName ResponderEventListener
 * @Description ResponderEventListener
 * @Author Silwings
 * @Date 2022/1/9 13:00
 * @Version V1.0
 **/
public interface ResponderEventListener {

    ResponderEventType getEventType();

    void doEvent(ResponderEventPack<?> eventPack);

}
