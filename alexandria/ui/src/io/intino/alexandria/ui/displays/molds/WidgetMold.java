package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.displays.MethodsDisplay;

import java.util.ArrayList;
import java.util.List;

public class WidgetMold extends AbstractWidgetMold<UiFrameworkBox> {
    private List<Property> propertyList = new ArrayList<>();
    private List<Method> methodList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();

    public WidgetMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
//        propertiesDisplay.set(new PropertiesDisplay(box()));
        methods.set(new MethodsDisplay(box()));
        events.set(new EventsDisplay(box()));
    }

    public WidgetMold properties(List<Property> propertyList) {
        this.propertyList = propertyList;
        return this;
    }

    public WidgetMold methods(List<Method> methodList) {
        this.methodList = methodList;
        return this;
    }

    public WidgetMold events(List<Event> eventList) {
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
//        propertiesDisplay.<PropertiesDisplay>get().properties(propertyList);
//        propertiesDisplay.refresh();
        propertyList.forEach(p -> {
            PropertyMold block = new PropertyMold(box());
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