package me.jdfunk.core;

import me.jdfunk.api.IEngine;
import me.jdfunk.api.annotations.EventHandler;
import me.jdfunk.api.event.EventBus;
import me.jdfunk.api.event.Subscription;
import me.jdfunk.api.event.domain.EngineStartEvent;
import me.jdfunk.api.event.domain.EngineStopEvent;
import me.jdfunk.api.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine implements IEngine {
    private static final Logger logger = Logger.getLogger(Engine.class);

    private final EventBus eventBus;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Engine() {
        eventBus = new EventBus();
        eventBus.register(this);
        logger.info("Engine initialized");
    }

    @Override
    public void start(EventBus appEventBus) {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Engine.start() called, but engine is already running");
            return;
        }
        eventBus.publish(new EngineStartEvent());
    }

    @Override
    public void stop(boolean suppressWarning) {
        if (!running.compareAndSet(true, false)) {
            logger.warn("Engine.stop() called, but engine is not running");
            return;
        }
        eventBus.publish(new EngineStopEvent(suppressWarning));
    }

    @EventHandler
    public void onEngineStart(EngineStartEvent event) {
        logger.info("Engine started");
    }

    @EventHandler
    public void onEngineStop(EngineStopEvent event) {
        if (event.isSuppressWarning()) {
            logger.info("Engine stopped.");
        } else {
            logger.warn("Engine stopped. Was this an error?");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    public static Logger getLogger() {
        return logger;
    }
}