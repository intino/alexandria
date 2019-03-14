package io.intino.alexandria.ui.displays;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsDisplay extends AbstractEventsDisplay<UiFrameworkBox> {
    private List<Event> eventList = new ArrayList<>();

    public EventsDisplay(UiFrameworkBox box) {
        super(box);
    }

    public EventsDisplay events(List<Event> eventList) {
        this.eventList = eventList;
        return this;
    }

    @Override
    public void init() {
        super.init();
        notifier.refresh(eventList);
    }
}