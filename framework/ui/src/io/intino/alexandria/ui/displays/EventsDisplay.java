package io.intino.alexandria.ui.displays;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Method;

import java.util.ArrayList;
import java.util.List;

public class EventsDisplay extends AbstractEventsDisplay<UiFrameworkBox> {
    private List<Method> eventList = new ArrayList<>();

    public EventsDisplay(UiFrameworkBox box) {
        super(box);
    }

    public EventsDisplay events(List<Method> eventList) {
        this.eventList = eventList;
        return this;
    }

    @Override
    public void init() {
        super.init();
        notifier.refresh(eventList);
    }
}