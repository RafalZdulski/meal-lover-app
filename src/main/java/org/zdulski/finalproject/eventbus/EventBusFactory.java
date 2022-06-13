package org.zdulski.finalproject.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusFactory {
    private static final  EventBus eventBus = new EventBus();

    public static EventBus getEventBus() {
        return eventBus;
    }

}
