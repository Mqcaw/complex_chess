package me.jdfunk.app;

import me.jdfunk.api.IEngine;
import me.jdfunk.api.event.EventBus;
import me.jdfunk.api.event.domain.EngineStopEvent;
import me.jdfunk.api.log.Logger;

import javax.swing.*;
import java.util.ServiceLoader;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        //internal event bus for app events
        EventBus appBus = new EventBus();

        //load engine runtime
        IEngine engine = ServiceLoader.load(IEngine.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No EngineRuntime found."));

        //start the engine
        engine.start(appBus);

        //initialize bridge between engine and app
        AppEventBridge bridge = new AppEventBridge(engine.getEventBus(), appBus);
        bridge.bridgeEngineDomainEventsToApp();

        //on JVM shutdown, close bridge and attempt to stop engine
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                bridge.close();
            } catch (Exception ignored) {
                // ignore
            }
            try {
                engine.getEventBus().publish(new EngineStopEvent(true));
            } catch (Exception ignored) {
                // ignore
            }
        }, "shutdown"));

        if (!engine.isRunning()) {
            logger.error("Engine failed to start", new Throwable("Engine failed to start."));
            return;
        }

        //5. Start UI after engine is confirmed running
        SwingUtilities.invokeLater(Window::new);

        //register app listeners on appBus here
        // appBus.register(new SomeAppListener());
    }

    public static Logger getLogger() {
        return logger;
    }
}