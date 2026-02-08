package me.jdfunk.app;

import me.jdfunk.api.IEngine;
import me.jdfunk.api.annotations.DomainEvent;
import me.jdfunk.api.event.Event;
import me.jdfunk.api.event.EventBus;
import me.jdfunk.api.event.domain.AbstractDomainEvent;
import me.jdfunk.api.event.domain.EngineStartEvent;
import me.jdfunk.api.log.Logger;

import javax.swing.*;
import java.util.ServiceLoader;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        //ResourceManager.initialize();
        //System.out.println("JAR path = " + ResourceManager.getJarDirectory());
        SwingUtilities.invokeLater(Window::new);

        EventBus eventBus = new EventBus();

        IEngine engine = ServiceLoader.load(IEngine.class).findFirst().orElseThrow(() -> new RuntimeException("No engine runtime found."));
        engine.getEventBus().publish(new EngineStartEvent());
        if (!engine.isRunning()) {
            logger.error("Engine failed to start");
        }

        eventBus.subscribeToDomainEvent(AbstractDomainEvent.class, event -> {
            //Only forward events marked with @DomainEvent
            if (event.getClass().isAnnotationPresent(DomainEvent.class)) {
                eventBus.publish(event);
            }
        });
    }

    public static Logger getLogger() {
        return logger;
    }
}