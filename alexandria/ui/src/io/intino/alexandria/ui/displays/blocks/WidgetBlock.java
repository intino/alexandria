package io.intino.alexandria.ui.displays.blocks;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.displays.MethodsDisplay;
import io.intino.alexandria.ui.displays.PropertiesDisplay;

import java.util.ArrayList;
import java.util.List;

public class WidgetBlock extends AbstractWidgetBlock<UiFrameworkBox> {
    private List<Property> properties = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public WidgetBlock(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        dateProperties.set(new PropertiesDisplay(box()));
        dateMethods.set(new MethodsDisplay(box()));
        dateEvents.set(new EventsDisplay(box()));
    }

    public WidgetBlock properties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public WidgetBlock methods(List<Method> methods) {
        this.methods = methods;
        return this;
    }

    public WidgetBlock events(List<Event> events) {
        this.events = events;
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
        dateProperties.<PropertiesDisplay>get().properties(properties);
        dateProperties.refresh();
    }

    private void refreshMethodsDisplay() {
        dateMethods.<MethodsDisplay>get().methods(methods);
        dateMethods.refresh();
    }

    private void refreshEventsDisplay() {
        dateEvents.<EventsDisplay>get().events(events);
        dateEvents.refresh();
    }

}