package me.jdfunk.api;

import me.jdfunk.api.event.EventBus;

public interface IEngine {
    boolean isRunning();
    EventBus getEventBus();

    void start(EventBus appEventBus);
    void stop(boolean suppressWarning);
}