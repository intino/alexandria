package io.intino.alexandria.ui.displays;

import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.ui.AlexandriaUiBox;

import java.util.ArrayList;
import java.util.List;

public class EventsDisplay extends AbstractEventsDisplay<AlexandriaUiBox> {
    private List<Method> eventList = new ArrayList<>();

    public EventsDisplay(AlexandriaUiBox box) {
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