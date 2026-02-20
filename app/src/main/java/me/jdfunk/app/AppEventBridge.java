package me.jdfunk.app;

import me.jdfunk.api.event.Event;
import me.jdfunk.api.event.EventBus;
import me.jdfunk.api.event.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridges events between the Engine bus and the App bus.
 *
 * Recommended default: Engine -> App for domain events only.
 * (This avoids feedback loops unless you explicitly add an App -> Engine bridge too.)
 */
public final class AppEventBridge implements AutoCloseable {
    private final EventBus engineBus;
    private final EventBus appBus;
    private final List<Subscription> subscriptions = new ArrayList<>();

    public AppEventBridge(EventBus engineBus, EventBus appBus) {
        this.engineBus = engineBus;
        this.appBus = appBus;
    }

    /**
     * Forward Engine domain events to the App bus.
     *
     * This relies on EventBus.publish() delivering events to subscribers of Event.class.
     */
    public void bridgeEngineDomainEventsToApp() {
        subscriptions.add(engineBus.subscribeToDomainEvent(Event.class, appBus::publish));
    }

    /**
     * Optional: forward *all* events App->Engine (not domain-filtered).
     * Only enable this if you need it, and be careful about loops.
     */
    public void bridgeAllAppEventsToEngine() {
        subscriptions.add(appBus.subscribeToEvent(Event.class, engineBus::publish));
    }

    /**
     * Convenience: publish to engine side (commands, requests, etc.)
     */
    public void publishToEngine(Event event) {
        engineBus.publish(event);
    }

    /**
     * Convenience: publish to app side (UI notifications, etc.)
     */
    public void publishToApp(Event event) {
        appBus.publish(event);
    }

    @Override
    public void close() {
        subscriptions.forEach(Subscription::unsubscribe);
        subscriptions.clear();
    }
}