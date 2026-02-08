package me.jdfunk.api.event.domain;


public class EngineStopEvent extends AbstractDomainEvent {
    private boolean suppressWarning;


    public EngineStopEvent() {
        suppressWarning = false;
    }
    public EngineStopEvent(boolean suppressWarning) {
        this.suppressWarning = suppressWarning;
    }

    public boolean isSuppressWarning() {
        return suppressWarning;
    }
}
