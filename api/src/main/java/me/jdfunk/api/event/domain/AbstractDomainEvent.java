package me.jdfunk.api.event.domain;

import me.jdfunk.api.annotations.DomainEvent;
import me.jdfunk.api.event.Event;

@DomainEvent
public abstract class AbstractDomainEvent extends Event {

    public AbstractDomainEvent() {
    }
}
