package me.jdfunk.api.event;

import me.jdfunk.api.annotations.DomainEvent;
import me.jdfunk.api.annotations.EventHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private static final Map<Class<?>, List<MethodHandler>> handlers = new HashMap<>();
    private static final Map<Class<?>, List<Consumer<Event>>> subscribers = new HashMap<>();

    //Used to register listeners to the event bus, this listener will be notified of all events of the given type by the bus.
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {

                Class<?> eventType = method.getParameterTypes()[0];
                method.setAccessible(true);
                handlers.computeIfAbsent(eventType, c -> new ArrayList<>()).add(new MethodHandler(listener, method));
            }
        }
    }

    //Subscribe to any event type
    private <T extends Event> Subscription subscribe(Class<T> eventType, Consumer<T> handler) {
        @SuppressWarnings("unchecked")
        Consumer<Event> eventConsumer = (Consumer<Event>) handler;
        subscribers.computeIfAbsent(eventType, c -> new ArrayList<>()).add(eventConsumer);

        return () -> unsubscribe(eventType, eventConsumer);
    }

    //Subscribe only to verified domain events
    public <T extends Event> Subscription subscribeToDomainEvent(Class<T> domainEvent, Consumer<T> handler) {
        if (!domainEvent.isAnnotationPresent(DomainEvent.class)) {
            throw new IllegalArgumentException(domainEvent.getName() + " is not marked with @DomainEvent");
        }
        return subscribe(domainEvent, handler);
    }

    //Used to publish an event to the bus and thus all listeners registered to that bus.
    public static void publish(Event event) {
        //First, invoke annotation-based handlers
        List<MethodHandler> methodHandlers = handlers.get(event.getClass());
        if (methodHandlers != null) {
            for (MethodHandler mh : methodHandlers) {
                if (event instanceof Cancellable c && c.isCancelled()) {
                    break;
                }
                mh.invoke(event);
            }
        }

        //Then, invoke subscribers
        List<Consumer<Event>> eventSubscribers = subscribers.get(event.getClass());
        if (eventSubscribers != null) {
            for (Consumer<Event> subscriber : new ArrayList<>(eventSubscribers)) {
                if (event instanceof Cancellable c && c.isCancelled()) {
                    break;
                }
                try {
                    subscriber.accept(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
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
