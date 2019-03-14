package io.intino.alexandria.ui.displays.blocks;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.MethodParameter;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.displays.MethodsDisplay;
import io.intino.alexandria.ui.displays.PropertiesDisplay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class DateBlock extends AbstractDateBlock<UiFrameworkBox> {

    public DateBlock(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        dateA.update(Instant.now());
        addPropertiesDisplay();
        addMethodsDisplay();
        addEventsDisplay();
    }

    private void addPropertiesDisplay() {
        PropertiesDisplay display = new PropertiesDisplay(box());
        display.properties(propertyList());
        dateProperties.set(display);
        dateProperties.refresh();
    }

    private void addMethodsDisplay() {
        MethodsDisplay display = new MethodsDisplay(box());
        display.methods(methodList());
        dateMethods.set(display);
        dateMethods.refresh();
    }

    private void addEventsDisplay() {
        EventsDisplay display = new EventsDisplay(box());
        display.events(eventList());
        dateEvents.set(display);
        dateEvents.refresh();
    }

    private List<Property> propertyList() {
        return new ArrayList<Property>() {{
           add(property("format", Property.Type.Text, "used to...", "dd/MM/YYYY"));
           add(property("mode", Property.Type.Word, "used to...", "FromNow", "ToNow"));
        }};
    }

    private List<Method> methodList() {
        return new ArrayList<Method>() {{
            add(method("get", emptyList(), "returns ....", "java.time.Instant"));
            add(method("update", singletonList(methodParameter("instant", "java.time.Instant")), "updates ...", "void"));
        }};
    }

    private List<Event> eventList() {
        return null;
    }

    private Property property(String name, Property.Type type, String description, String... values) {
        return new Property().name(name).type(type).description(description).values(Arrays.asList(values));
    }

    private Method method(String name, List<MethodParameter> params, String description, String returnType) {
        return new Method().name(name).params(params).description(description).returnType(returnType);
    }

    private MethodParameter methodParameter(String name, String type) {
        return new MethodParameter().name(name).type(type);
    }
}