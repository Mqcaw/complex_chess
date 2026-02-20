package me.jdfunk.api.event;

import me.jdfunk.api.annotations.DomainEvent;
import me.jdfunk.api.annotations.EventHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
    // Instance state (not singleton)
    private final Map<Class<?>, List<MethodHandler>> handlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Consumer<Event>>> subscribers = new ConcurrentHashMap<>();

    //Used to register listeners to the event bus, this listener will be notified of all events of the given type by the bus.
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)
                    && method.getParameterCount() == 1
                    && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {

                Class<?> eventType = method.getParameterTypes()[0];
                method.setAccessible(true);
                handlers.computeIfAbsent(eventType, c -> new CopyOnWriteArrayList<>())
                        .add(new MethodHandler(listener, method));
            }
        }
    }

    /**
     * Subscribe to a specific event type.
     *
     * Note: with the publish() implementation below, subscribing to Event.class receives ALL events.
     */
    public <T extends Event> Subscription subscribeToEvent(Class<T> eventType, Consumer<T> handler) {
        return subscribe(eventType, handler);
    }

    /**
     * Subscribe to domain events only.
     *
     * Important: the domain check is done on the *runtime event class*, not the base type you subscribe to,
     * so you can subscribe to Event.class and receive only @DomainEvent events.
     */
    public <T extends Event> Subscription subscribeToDomainEvent(Class<T> eventType, Consumer<T> handler) {
        return subscribe(eventType, event -> {
            if (!event.getClass().isAnnotationPresent(DomainEvent.class)) {
                return;
            }
            handler.accept(event);
        });
    }

    //Subscribe to any event type
    private <T extends Event> Subscription subscribe(Class<T> eventType, Consumer<T> handler) {
        @SuppressWarnings("unchecked")
        Consumer<Event> eventConsumer = (Consumer<Event>) handler;

        subscribers.computeIfAbsent(eventType, c -> new CopyOnWriteArrayList<>())
                .add(eventConsumer);

        return () -> unsubscribe(eventType, eventConsumer);
    }

    /**
     * Publish an event to this bus.
     *
     * Dispatch rules:
     * - Handlers/subscribers registered for the event's exact class are invoked
     * - Additionally, handlers/subscribers registered for any superclass up to Event.class are invoked
     *
     * This enables patterns like:
     * - subscribeToEvent(Event.class, ...) to observe all events (useful for bridging)
     * - @EventHandler methods that take Event or an abstract base event class
     */
    public void publish(Event event) {
        for (Class<?> type = event.getClass();
             type != null && Event.class.isAssignableFrom(type);
             type = type.getSuperclass()) {

            // First, invoke annotation-based handlers
            List<MethodHandler> methodHandlers = handlers.get(type);
            if (methodHandlers != null) {
                for (MethodHandler mh : methodHandlers) {
                    if (event instanceof Cancellable c && c.isCancelled()) {
                        return;
                    }
                    mh.invoke(event);
                }
            }

            // Then, invoke subscribers
            List<Consumer<Event>> eventSubscribers = subscribers.get(type);
            if (eventSubscribers != null) {
                for (Consumer<Event> subscriber : eventSubscribers) {
                    if (event instanceof Cancellable c && c.isCancelled()) {
                        return;
                    }
                    try {
                        subscriber.accept(event);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    //Helper method to unsubscribe
    private void unsubscribe(Class<?> eventType, Consumer<Event> handler) {
        List<Consumer<Event>> list = subscribers.get(eventType);
        if (list != null) {
            list.remove(handler);
            if (list.isEmpty()) {
                subscribers.remove(eventType);
            }
        }
    }

    public boolean isDomainEvent(Event event) {
        return event.getClass().isAnnotationPresent(DomainEvent.class);
    }

    private record MethodHandler(Object instance, Method method) {
        void invoke(Event e) {
            try { method.invoke(instance, e); }
            catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
