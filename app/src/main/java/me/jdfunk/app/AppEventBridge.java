package me.jdfunk.app;

import me.jdfunk.api.annotations.DomainEvent;
import me.jdfunk.api.event.*;
import me.jdfunk.api.event.Subscription;

import java.util.ArrayList;
import java.util.List;

public class AppEventBridge {
    private final EventBus apiEventBus; //The shared API EventBus
    private final List<Subscription> subscriptions = new ArrayList<>();

    public AppEventBridge(EventBus apiEventBus) {
        this.apiEventBus = apiEventBus;
    }

    //Subscribe to all domain events from the Engine
    public void subscribeToEngineDomainEvents(EventBus appEventBus) {
        // Subscribe to the base Event class but filter for domain events
        subscriptions.add(apiEventBus.subscribeToDomainEvent(Event.class, event -> {
            // Only forward events marked with @DomainEvent
            if (event.getClass().isAnnotationPresent(DomainEvent.class)) {
                appEventBus.publish(event);
            }
        }));
    }

    //Publish events to Engine
    public void publish(Event event) {
        apiEventBus.publish(event);
    }

    //Cleanup
    public void shutdown() {
        subscriptions.forEach(Subscription::unsubscribe);
        subscriptions.clear();
    }
}