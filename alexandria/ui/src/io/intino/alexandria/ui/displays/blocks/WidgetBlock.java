package io.intino.alexandria.ui.displays.blocks;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.displays.MethodsDisplay;

import java.util.ArrayList;
import java.util.List;

public class WidgetBlock extends AbstractWidgetBlock<UiFrameworkBox> {
    private List<Property> propertyList = new ArrayList<>();
    private List<Method> methodList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();

    public WidgetBlock(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        methods.set(new MethodsDisplay(box()));
        events.set(new EventsDisplay(box()));
    }

    public WidgetBlock properties(List<Property> propertyList) {
        this.propertyList = propertyList;
        return this;
    }

    public WidgetBlock methods(List<Method> methodList) {
        this.methodList = methodList;
        return this;
    }

    public WidgetBlock events(List<Event> eventList) {
        this.eventList = eventList;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshPropertiesDisplay();
        refreshMethodsDisplay();
        refreshEventsDisplay();
    }

    private void refreshPropertiesDisplay() {
        propertyList.forEach(p -> {
            PropertyBlock block = new PropertyBlock(box());
            block.property(p);
            properties.addProperty(block);
            block.refresh();
        });
    }

    private void refreshMethodsDisplay() {
        methods.<MethodsDisplay>get().methods(methodList);
        methods.refresh();
    }

    private void refreshEventsDisplay() {
        events.<EventsDisplay>get().events(eventList);
        events.refresh();
    }

}