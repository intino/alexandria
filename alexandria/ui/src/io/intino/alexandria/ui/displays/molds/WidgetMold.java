package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.displays.EventsDisplay;
import io.intino.alexandria.ui.documentation.model.DateWidget;
import io.intino.alexandria.ui.documentation.model.ImageWidget;
import io.intino.alexandria.ui.documentation.model.TextWidget;

public class WidgetMold extends AbstractWidgetMold<UiFrameworkBox> {

    public WidgetMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        events.set(new EventsDisplay(box()));
    }

    @Override
    public void refresh() {
        super.refresh();
        updateExamplesVisibility();
        refreshPropertiesDisplay();
        refreshMethodsDisplay();
        refreshEventsDisplay();
    }

    private void updateExamplesVisibility() {
        textExamples.visible(widget instanceof TextWidget);
        imageExamples.visible(widget instanceof ImageWidget);
        dateExamples.visible(widget instanceof DateWidget);
    }

    private void refreshPropertiesDisplay() {
        widget.propertyList().forEach(this::propertyMoldOf);
    }

    private void propertyMoldOf(Property property) {
        PropertyMold mold = new PropertyMold(box());
        mold.property = property;
        properties.addPropertyMold(mold);
        mold.refresh();
    }

    private void refreshMethodsDisplay() {
        widget.methodList().forEach(this::methodMoldOf);
    }

    private void methodMoldOf(Method method) {
        MethodMold mold = new MethodMold(box());
        mold.method = method;
        methods.addMethodMold(mold);
        mold.refresh();
    }

    private void refreshEventsDisplay() {
        events.<EventsDisplay>get().events(widget.eventList());
        events.refresh();
    }

}