package me.jdfunk.core;

import me.jdfunk.api.IEngine;
import me.jdfunk.api.annotations.EventHandler;
import me.jdfunk.api.event.*;
import me.jdfunk.api.event.domain.EngineStopEvent;
import me.jdfunk.api.log.Logger;
import me.jdfunk.api.event.domain.EngineStartEvent;

public class Engine implements IEngine {
    private static final Logger logger = Logger.getLogger(Engine.class);
    private EventBus eventBus;
    private static boolean running = false;

    public Engine() {
        eventBus = new EventBus();
        eventBus.register(this);
        logger.info("Engine initialized and listening for events");
    }

    @EventHandler
    public void onEngineStart(EngineStartEvent event) {
        running = true;
        logger.info("Engine started");
    }

    @EventHandler
    public void onEngineStop(EngineStopEvent event) {
        running = false;
        if (event.isSuppressWarning()) {
            logger.info("Engine stopped.");
        } else {
            logger.warn("Engine stopped. Was this an error?");
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    public static Logger getLogger() {
        return logger;
    }

}
